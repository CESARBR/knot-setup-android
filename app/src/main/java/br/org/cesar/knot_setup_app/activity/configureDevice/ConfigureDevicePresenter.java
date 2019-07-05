package br.org.cesar.knot_setup_app.activity.configureDevice;
import android.content.Context;
import android.util.Log;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import br.org.cesar.knot_setup_app.activity.configureDevice.ConfigureDeviceContract.Presenter;
import br.org.cesar.knot_setup_app.activity.configureDevice.ConfigureDeviceContract.ViewModel;
import br.org.cesar.knot_setup_app.data.DataManager;
import br.org.cesar.knot_setup_app.domain.callback.DeviceCallback;
import br.org.cesar.knot_setup_app.KnotSetupApplication;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;
import br.org.cesar.knot_setup_app.model.Gateway;
import br.org.cesar.knot_setup_app.model.Openthread;
import br.org.cesar.knot_setup_app.model.Thing;
import br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper;
import br.org.cesar.knot_setup_app.utils.Constants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ConfigureDevicePresenter implements Presenter{
    private ViewModel mViewModel;
    private int gatewayID;
    private boolean operation;
    private BluetoothWrapper bluetoothWrapper;
    private BluetoothDevice device;
    private Context context;

    private Gateway gateway;
    private Thing thing;

    private int readCount = 0;
    private int writeCount = 0;
    private boolean readDone = false;
    private boolean writeDone = false;

    private static DataManager dataManager;

    ConfigureDevicePresenter(ViewModel viewModel,int gatewayID, boolean operation,Context context){
        this.mViewModel = viewModel;
        this.gatewayID = gatewayID;
        this.operation = operation;
        this.bluetoothWrapper = KnotSetupApplication.getBluetoothWrapper();
        this.device = KnotSetupApplication.getBluetoothDevice();
        this.context = context;
        Log.d("DEV-LOG","getOpenthreadConfgig starting");
        getOpenthreadConfig();
    }

    private void getOpenthreadConfig(){
        String bearer,ip,port,request;
        bearer = dataManager.getInstance()
                .getPersistentPreference()
                .getSharedPreferenceString(context,"token");

        ip = dataManager.getInstance()
                .getPreference()
                .getSharedPreferenceString(context,"ip");

        port = dataManager.getInstance()
                .getPreference().getSharedPreferenceString(context,"port");

        request = "http://" + ip + ":" + port +"/api/radio/openthread";
        Log.d("DEV-LOG",bearer);
        Log.d("DEV-LOG","request= " + request);

        dataManager.getInstance().getService().getOpenthreadConfig(request,bearer)
                .timeout(Constants.GET_OPENTHREAD_TIMEOUT, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getOpenThreadSucceeded,
                        this::onErrorHandler);

    }

    private void getOpenThreadSucceeded(Openthread openthread){

        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"netname",openthread.getNetworkName());

        Log.d("DEV-LOG","netname= " + getValue("netname"));

        
        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"channel",openthread.getChannel());

        Log.d("DEV-LOG","channel= " + getValue("channel"));

        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"xpanid",openthread.getXpanId());

        Log.d("DEV-LOG","xpanid= " + getValue("xpanid"));


        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"panid", openthread.getPanId());

        Log.d("DEV-LOG","panid= " + getValue("panid"));


        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"ipv6",openthread.getMeshIpv6());

        Log.d("DEV-LOG","ipv6= " + getValue("ipv6"));


        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"masterkey",openthread.getMasterKey());

        Log.d("DEV-LOG","masterkey= " + getValue("masterkey"));

        Log.d("DEV-LOG","callbackflux starting");

        callbackFlux();
    }

    private void onErrorHandler(Throwable throwable){
        Log.d("DEV-LOG", "onErrorHandler: " + throwable.getMessage());
    }

    private void callbackFlux(){

        Log.d("DEV-LOG","CallbackFlux");
        bluetoothWrapper.waitForBonding(device, new DeviceCallback() {
            @Override
            public void onConnect() {
                mViewModel.callbackOnConnected();
                Log.d("DEV-LOG","OnConnect");
                bluetoothWrapper.discoverServices();
            }

            @Override
            public void onCharacteristicChanged(){
                Log.d("DEV-LOG","Characteristic changed?");
            }

            @Override
            public void onDisconnect(){
                Log.d("DEV-LOG","Disconnected");
                bluetoothWrapper.closeGatt();
                mViewModel.callbackOnDisconnected();
            }

            @Override
            public void onServiceDiscoveryComplete(){
                Log.d("DEV-LOG","Services discovered");
                if(operation){
                    thingConfigWrite();
                }
                else{
                    gateway = new Gateway();
                    gatewayConfigRead();
                }
            }

            @Override
            public void onServiceDiscoveryFail(){
                Log.d("DEV-LOG","Service discovery failed");
            }

            @Override
            public void onCharacteristicWriteComplete(){
                Log.d("DEV-LOG","Characteristic writen");
                mViewModel.callbackOnOperation(writeCount);

                if(writeDone){
                    bluetoothWrapper.closeConn();
                }
                else {
                    writeCount++;
                    thingConfigWrite();
                }
            }

            @Override
            public void onCharacteristicWriteFail(){
                Log.d("DEV-LOG","Characteristic write failed");
            }

            @Override
            public void onReadRssiComplete(int rssi){
                Log.d("DEV-LOG","Rssi read: " + rssi);
            }

            @Override
            public void onReadRssiFail(){
                Log.d("DEV-LOG","Rssi read failed");
            }

            @Override
            public void onCharacteristicReadComplete(byte[] value){
                String valueRead;

                if(value[0] < 97){valueRead = bytesToHex(value);}

                else {valueRead = new String(value);}

                Log.d("DEV-LOG","Characteristic read: " + valueRead);
                mViewModel.callbackOnOperation(readCount);

                if(readDone){
                    bluetoothWrapper.closeConn();
                }

                else {
                    readCount++;
                    gatewayConfigRead();
                }
            }

            @Override
            public void onCharacteristicReadFail(){
                Log.d("DEV-LOG","Characteristic read failed");
            }

        });
    }



    private void writeWrapper(UUID service, UUID characteristic, String valtoWrite){
        this.bluetoothWrapper.write(service,characteristic,valtoWrite);
    }


    private void writeWrapper(UUID service, UUID characteristic, byte[] valtoWrite){
        this.bluetoothWrapper.write(service,characteristic,valtoWrite);
    }

    private void readWrapper(UUID service, UUID characteristic){
        this.bluetoothWrapper.readCharacteristic(service,characteristic);
    }

    private void thingConfigWrite(){
        String value;
        byte[] valueBytes;

        switch (writeCount){
            case 0:
                value = getValue("channel");
                Log.d("DEV-LOG", "Write Wrapper: Channel Value: " + value);
                valueBytes = stringToByteArrLittleEndian(value,Constants.CHANNEL_CHARACTERISTIC_BYTE_SIZE);
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.CHANNEL_CHARACTERISTIC, valueBytes);
                break;

            case 1:
                value = getValue("netname");
                Log.d("DEV-LOG", "WriteWrapper: NetName + Value: " + value);
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.NET_NAME_CHARACTERISTIC,value);
                break;

            case 2:
                value = getValue("panid");
                Log.d("DEV-LOG", "WriteWrapper: PanID Value: "+ value );
                valueBytes  = stringToByteArrLittleEndian(value,Constants.PANID_CHARACTERISTIC_BYTE_SIZE);
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.PAN_ID_CHARACTERISTIC,valueBytes);
                break;

            case 3:
                value = getValue("xpanid");
                Log.d("DEV-LOG", "WriteWrapper: XpanID Value: " + value);
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.XPANID_CHARACTERISTIC,value);
                break;


            case 4:
                value = getValue("masterkey");
                Log.d("DEV-LOG", "WriteWrapper: Masterkey Value: " + value);
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.MASTER_KEY_CHARACTERISTIC,value);
                break;

            case 5:
                value = getValue("ipv6");
                Log.d("DEV-LOG", "WriteWrapper: IPV6 Value: " + value);
                writeWrapper(Constants.IPV6_SERVICE,Constants.IPV6_CHARACTERISTIC,value);
                writeDone = true;
        }
        
    }

    private String getValue(String characteristic){
        return  DataManager
                .getInstance()
                .getPreference()
                .getSharedPreferenceString(context,characteristic);
    }

    private void gatewayConfigRead(){
        switch (readCount){
            case 0:
                Log.d("DEV-LOG", "ReadWrapper: Channel" );
                readWrapper(Constants.OT_SETTINGS_SERVICE_GATEWAY,Constants.CHANNEL_CHARACTERISTIC_GATEWAY);
                break;
            case 1:
                Log.d("DEV-LOG", "ReadWrapper: NetName");
                readWrapper(Constants.OT_SETTINGS_SERVICE_GATEWAY,Constants.NET_NAME_CHARACTERISTIC_GATEWAY);
                break;
            case 2:
                Log.d("DEV-LOG", "ReadWrapper: PanID");
                readWrapper(Constants.OT_SETTINGS_SERVICE_GATEWAY,Constants.PAN_ID_CHARACTERISTIC_GATEWAY);
                break;
            case 3:
                Log.d("DEV-LOG", "ReadWrapper: XpanID");
                readWrapper(Constants.OT_SETTINGS_SERVICE_GATEWAY,Constants.XPANID_CHARACTERISTIC_GATEWAY);
                break;

            case 4:
                Log.d("DEV-LOG", "ReadWrapper: Masterkey");
                readWrapper(Constants.OT_SETTINGS_SERVICE_GATEWAY,Constants.MASTER_KEY_CHARACTERISTIC_GATEWAY);
                break;

            case 5:
                Log.d("DEV-LOG", "ReadWrapper: IPV6");
                readWrapper(Constants.OT_SETTINGS_SERVICE_GATEWAY,Constants.IPV6_CHARACTERISTIC_GATEWAY);
                readDone = true;
        }
    }

    private static String bytesToHex(byte[] hashInBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    private static byte[] stringToByteArrLittleEndian(String str, int arrSize) {
        int value = Integer.valueOf(str);
        byte[] byteArr = new byte[arrSize];
        int endianess = 0;
        for (int i = 0; i < arrSize; i++) {
            endianess = arrSize - i - 1;
            byteArr[i] = (byte) (value >> 8 * endianess);
        }
        return byteArr;
    }

}