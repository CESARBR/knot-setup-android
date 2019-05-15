package br.org.cesar.knot_setup_app.activity.configureDevice;
import android.content.Context;
import android.util.Log;

import java.util.UUID;

import br.org.cesar.knot_setup_app.activity.configureDevice.ConfigureDeviceContract.Presenter;
import br.org.cesar.knot_setup_app.activity.configureDevice.ConfigureDeviceContract.ViewModel;
import br.org.cesar.knot_setup_app.data.DataManager;
import br.org.cesar.knot_setup_app.domain.callback.DeviceCallback;
import br.org.cesar.knot_setup_app.KnotSetupApplication;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;
import br.org.cesar.knot_setup_app.model.Gateway;
import br.org.cesar.knot_setup_app.model.Thing;
import br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper;
import br.org.cesar.knot_setup_app.utils.Constants;

public class ConfigureDevicePresenter implements Presenter{
    private ViewModel mViewModel;
    private int gatewayID;
    private boolean operation;
    private BluetoothWrapper bluetoothWrapper;
    private BluetoothDevice device;
    private Context context;

    private Gateway gateway;
    private Thing thing;

    private int read_count = 0;
    private int write_count = 0;
    private boolean readDone = false;
    private boolean writeDone = false;


    ConfigureDevicePresenter(ViewModel viewModel,int gatewayID, boolean operation,Context context){
        this.mViewModel = viewModel;
        this.gatewayID = gatewayID;
        this.operation = operation;
        this.bluetoothWrapper = KnotSetupApplication.getBluetoothWrapper();
        this.device = KnotSetupApplication.getBluetoothDevice();
        this.context = context;
        callbackFlux();
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
                mViewModel.callbackOnOperation(write_count);

                if(writeDone){
                    bluetoothWrapper.closeConn();
                }
                else {
                    write_count++;
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
                mViewModel.callbackOnOperation(read_count);

                if(readDone){
                    bluetoothWrapper.closeConn();
                }

                else {
                    read_count++;
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

        switch (write_count){
            case 0:
                Log.d("DEV-LOG", "Write Wrapper: Channel" );
                value = getValue("channel");
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.CHANNEL_CHARACTERISTIC,value.getBytes());
                break;

            case 1:
                Log.d("DEV-LOG", "WriteWrapper: NetName");

                value = getValue("netname");
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.NET_NAME_CHARACTERISTIC,value);
                break;

            case 2:
                Log.d("DEV-LOG", "WriteWrapper: PanID");
                value = getValue("panid");
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.PAN_ID_CHARACTERISTIC,value.getBytes());
                break;

            case 3:
                Log.d("DEV-LOG", "WriteWrapper: XpanID");
                value = getValue("xpanid");
                writeWrapper(Constants.OT_SETTINGS_SERVICE,Constants.XPANID_CHARACTERISTIC,value);
                break;

            case 4:
                Log.d("DEV-LOG", "WriteWrapper: IPV6");
                value = getValue("ipv6");
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
        switch (read_count){
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

}