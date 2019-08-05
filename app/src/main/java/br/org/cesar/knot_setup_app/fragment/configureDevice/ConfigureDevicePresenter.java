package br.org.cesar.knot_setup_app.fragment.configureDevice;

import android.content.Context;
import android.util.Log;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import br.org.cesar.knot_setup_app.KnotSetupApplication;
import br.org.cesar.knot_setup_app.data.DataManager;
import br.org.cesar.knot_setup_app.domain.callback.DeviceCallback;
import br.org.cesar.knot_setup_app.fragment.configureDevice.ConfigureDeviceContract.Presenter;
import br.org.cesar.knot_setup_app.fragment.configureDevice.ConfigureDeviceContract.ViewModel;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;
import br.org.cesar.knot_setup_app.model.Gateway;
import br.org.cesar.knot_setup_app.model.Openthread;
import br.org.cesar.knot_setup_app.model.Thing;
import br.org.cesar.knot_setup_app.utils.Constants;
import br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper;
import br.org.cesar.knot_setup_app.wrapper.LogWrapper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ConfigureDevicePresenter implements Presenter {

    private ViewModel mViewModel;
    private int gatewayID;
    private int operation;
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

    ConfigureDevicePresenter(ViewModel viewModel, int gatewayID, int operation, Context context) {
        this.mViewModel = viewModel;
        this.gatewayID = gatewayID;
        this.operation = operation;
        this.bluetoothWrapper = KnotSetupApplication.getBluetoothWrapper();
        this.device = KnotSetupApplication.getBluetoothDevice();
        this.context = context;
        LogWrapper.Log("getOpenthreadConfgig starting", Log.DEBUG);
    }

    @Override
    public void onResume(){
        getOpenthreadConfig();
    }

    private void getOpenthreadConfig() {
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
        LogWrapper.Log("bearer= " + bearer, Log.DEBUG);
        LogWrapper.Log("request= " + request, Log.DEBUG);

        dataManager.getInstance().getService().getOpenthreadConfig(request,bearer)
                .timeout(Constants.GET_OPENTHREAD_TIMEOUT, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getOpenThreadSucceeded,
                        this::onErrorHandler);

    }

    private void getOpenThreadSucceeded(Openthread openthread) {

        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"netname",openthread.getNetworkName());

        LogWrapper.Log("netname= " + getValue("netname"), Log.DEBUG);

        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"channel",openthread.getChannel());

        LogWrapper.Log("channel= " + getValue("channel"), Log.DEBUG);

        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"xpanid",openthread.getXpanId());

        LogWrapper.Log("xpanid= " + getValue("xpanid"), Log.DEBUG);


        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"panid", openthread.getPanId());

        LogWrapper.Log("panid= " + getValue("panid"), Log.DEBUG);


        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"ipv6",openthread.getMeshIpv6());

        LogWrapper.Log("ipv6= " + getValue("ipv6"), Log.DEBUG);


        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"masterkey",openthread.getMasterKey());

        LogWrapper.Log("masterkey= "
                + getValue("masterkey"), Log.DEBUG);

        LogWrapper.Log("callbackflux starting", Log.DEBUG);

        callbackFlux();
    }

    private void onErrorHandler(Throwable throwable) {
        LogWrapper.Log("onErrorHandler: " + throwable.getMessage(), Log.DEBUG);
        mViewModel.onErrorHandler(throwable);
    }

    private void callbackFlux() {

        LogWrapper.Log("CallbackFlux", Log.DEBUG);
        bluetoothWrapper.waitForBonding(device, new DeviceCallback() {
            @Override
            public void onConnect() {
                mViewModel.onConnected();
                LogWrapper.Log("OnConnect", Log.DEBUG);
                bluetoothWrapper.discoverServices();
            }

            @Override
            public void onCharacteristicChanged() {
                LogWrapper.Log("Characteristic changed", Log.DEBUG);
            }

            @Override
            public void onDisconnect() {
                LogWrapper.Log("Disconnected", Log.DEBUG);
                bluetoothWrapper.closeGatt();
                mViewModel.onDisconnected();
            }

            @Override
            public void onServiceDiscoveryComplete() {
                LogWrapper.Log("Services discovered", Log.DEBUG);
                if(operation == Constants.CONFIGURE_THING_OPENTHREAD) {
                    thingConfigWrite();
                }
                //Todo: check if the logic of this else makes sense
                else {
                    gateway = new Gateway();
                    gatewayConfigRead();
                }
            }

            @Override
            public void onServiceDiscoveryFail() {
                LogWrapper.Log("Service discovery failed", Log.DEBUG);
            }

            @Override
            public void onCharacteristicWriteComplete() {
                LogWrapper.Log("Characteristic writen", Log.DEBUG);
                mViewModel.onWriteSucceeded(writeCount);

                if(writeDone) {
                    bluetoothWrapper.closeConn();
                }
                else {
                    writeCount++;
                    thingConfigWrite();
                }
            }

            @Override
            public void onCharacteristicWriteFail() {
                LogWrapper.Log("Characteristic write failed", Log.DEBUG);
                mViewModel.onWriteFailed(writeCount);
            }

            @Override
            public void onReadRssiComplete(int rssi){
                LogWrapper.Log("rssi= " + rssi, Log.DEBUG);
            }

            @Override
            public void onReadRssiFail(){
                LogWrapper.Log("Rssi read failed", Log.DEBUG);
            }

            @Override
            public void onCharacteristicReadComplete(byte[] value) {
                String valueRead;

                if(value[0] < 97){valueRead = bytesToHex(value);}

                else {valueRead = new String(value);}

                LogWrapper.Log("Characteristic read: " + valueRead, Log.DEBUG);
                mViewModel.onWriteSucceeded(readCount);

                if(readDone) {
                    bluetoothWrapper.closeConn();
                }

                else {
                    readCount++;
                    gatewayConfigRead();
                }
            }

            @Override
            public void onCharacteristicReadFail() {
                LogWrapper.Log("Characteristic read failed", Log.DEBUG);
            }

        });
    }



    private void writeWrapper(UUID service, UUID characteristic, String valtoWrite) {
        this.bluetoothWrapper.write(service,characteristic,valtoWrite);
    }


    private void writeWrapper(UUID service, UUID characteristic, byte[] valtoWrite) {
        this.bluetoothWrapper.write(service,characteristic,valtoWrite);
    }

    private void readWrapper(UUID service, UUID characteristic) {
        this.bluetoothWrapper.readCharacteristic(service,characteristic);
    }

    private void thingConfigWrite() {
        String value;
        byte[] valueBytes;

        switch (writeCount) {
            case 0:
                value = getValue("channel");
                LogWrapper.Log("channel= " + value, Log.DEBUG);
                valueBytes = stringToByteArrLittleEndian(value,Constants.CHANNEL_CHARACTERISTIC_BYTE_SIZE);
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.CHANNEL_CHARACTERISTIC, valueBytes);
                break;

            case 1:
                value = getValue("netname");
                LogWrapper.Log("netname= " + value, Log.DEBUG);
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.NET_NAME_CHARACTERISTIC,value);
                break;

            case 2:
                value = getValue("panid");
                LogWrapper.Log("panid= "+ value , Log.DEBUG);
                valueBytes  = stringToByteArrLittleEndian(value,Constants.PANID_CHARACTERISTIC_BYTE_SIZE);
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.PAN_ID_CHARACTERISTIC,valueBytes);
                break;

            case 3:
                value = getValue("xpanid");
                LogWrapper.Log("xpanid= " + value, Log.DEBUG);
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.XPANID_CHARACTERISTIC,value);
                break;


            case 4:
                value = getValue("masterkey");
                LogWrapper.Log("masterkey= " + value, Log.DEBUG);
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.MASTER_KEY_CHARACTERISTIC,value);
                break;

            case 5:
                value = getValue("ipv6");
                LogWrapper.Log("ipv6= " + value, Log.DEBUG);
                writeWrapper(Constants.IPV6_SERVICE,Constants.IPV6_CHARACTERISTIC,value);
                writeDone = true;
        }

    }

    private String getValue(String characteristic) {
        return  DataManager
                .getInstance()
                .getPreference()
                .getSharedPreferenceString(context,characteristic);
    }

    private void gatewayConfigRead() {
        switch (readCount){
            case 0:
                LogWrapper.Log("reading channel", Log.DEBUG);
                readWrapper(Constants.OT_SETTINGS_SERVICE_GATEWAY,Constants.CHANNEL_CHARACTERISTIC_GATEWAY);
                break;
            case 1:
                LogWrapper.Log("reading netname", Log.DEBUG);
                readWrapper(Constants.OT_SETTINGS_SERVICE_GATEWAY,Constants.NET_NAME_CHARACTERISTIC_GATEWAY);
                break;
            case 2:
                LogWrapper.Log("reading panid", Log.DEBUG);
                readWrapper(Constants.OT_SETTINGS_SERVICE_GATEWAY,Constants.PAN_ID_CHARACTERISTIC_GATEWAY);
                break;
            case 3:
                LogWrapper.Log("reading xpanid", Log.DEBUG);
                readWrapper(Constants.OT_SETTINGS_SERVICE_GATEWAY,Constants.XPANID_CHARACTERISTIC_GATEWAY);
                break;

            case 4:
                LogWrapper.Log("reading masterkey", Log.DEBUG);
                readWrapper(Constants.OT_SETTINGS_SERVICE_GATEWAY,Constants.MASTER_KEY_CHARACTERISTIC_GATEWAY);
                break;

            case 5:
                LogWrapper.Log("reading ipv6", Log.DEBUG);
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
