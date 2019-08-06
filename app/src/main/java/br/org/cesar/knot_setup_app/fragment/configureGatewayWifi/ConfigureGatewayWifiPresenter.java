package br.org.cesar.knot_setup_app.fragment.configureGatewayWifi;

import br.org.cesar.knot_setup_app.KnotSetupApplication;
import br.org.cesar.knot_setup_app.domain.callback.DeviceCallback;
import br.org.cesar.knot_setup_app.fragment.configureGatewayWifi.ConfigureGatewayWifiContract.ViewModel;
import br.org.cesar.knot_setup_app.fragment.configureGatewayWifi.ConfigureGatewayWifiContract.Presenter;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;
import br.org.cesar.knot_setup_app.utils.Constants;
import br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper;
import br.org.cesar.knot_setup_app.wrapper.NetworkWrapper;

public class ConfigureGatewayWifiPresenter  implements Presenter{
    private ViewModel viewModel;
    private BluetoothWrapper bluetoothWrapper;
    private BluetoothDevice gateway;
    private int writeCount = 0;

    ConfigureGatewayWifiPresenter(ViewModel viewModel){
        this.viewModel = viewModel;
        gateway = KnotSetupApplication.getBluetoothDevice();
        bluetoothWrapper = KnotSetupApplication.getBluetoothWrapper();
    }

    @Override
    public void onFocus() {
        setWifiSSID();
    }

    private void setWifiSSID() {
        String wifiSSID = NetworkWrapper.getCurrentWifiName();
        viewModel.setWifiSSID(wifiSSID);
    }

    @Override
    public void onSetWifiClicked(String SSID, String pwd){
        bluetoothWrapper.waitForBonding(this.gateway, new DeviceCallback() {
            @Override
            public void onConnect() {
                bluetoothWrapper.discoverServices();
                //view callback onConnect
            }

            @Override
            public void onDisconnect() {
                bluetoothWrapper.closeGatt();
                viewModel.onDisconnected();
                //mViewModel.callbackOnDisconnected();

            }

            @Override
            public void onServiceDiscoveryComplete() {
                gatewayWifiConfigWrite(SSID);
            }

            @Override
            public void onServiceDiscoveryFail() {

            }

            @Override
            public void onCharacteristicWriteComplete() {
                if(writeCount == 1){
                    bluetoothWrapper.closeConn();
                }
                writeCount += 1;
                gatewayWifiConfigWrite(pwd);
            }

            @Override
            public void onCharacteristicWriteFail() {
                bluetoothWrapper.closeConn();
                viewModel.onWriteFailed();
            }

            @Override
            public void onReadRssiComplete(int rssi) {

            }

            @Override
            public void onReadRssiFail() {

            }

            @Override
            public void onCharacteristicReadComplete(byte[] value) {

            }

            @Override
            public void onCharacteristicReadFail() {

            }

            @Override
            public void onCharacteristicChanged() {

            }
        });
    }

    public void gatewayWifiConfigWrite(String value){
        switch(writeCount){
            case 0:
                bluetoothWrapper.write(Constants.WIFI_CONFIGURATION_SERVICE_GATEWAY,
                        Constants.WIFI_SSID_CHARACTERISTIC,
                        value);
                break;
            case 1:
                bluetoothWrapper.write(Constants.WIFI_CONFIGURATION_SERVICE_GATEWAY,
                        Constants.WIFI_PASSWORD_CHARACTERISTIC,
                        value);
        }
    }

}
