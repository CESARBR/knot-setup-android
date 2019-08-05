package br.org.cesar.knot_setup_app.activity.scan;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.org.cesar.knot_setup_app.domain.callback.ScannerCallback;
import br.org.cesar.knot_setup_app.KnotSetupApplication;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;
import br.org.cesar.knot_setup_app.utils.Constants;
import br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper;
public class ScanPresenter implements ScanContract.Presenter {

    private BluetoothWrapper bluetoothWrapper;
    private ScanContract.ViewModel mViewModel;
    private int gatewayID;
    private int operation;
    private List<BluetoothDevice> deviceList;


    ScanPresenter(ScanContract.ViewModel viewModel, int operation, int gatewayID) {
        this.mViewModel = viewModel;
        this.bluetoothWrapper = KnotSetupApplication.getBluetoothWrapper();
        this.operation = operation;

        if (operation == Constants.CONFIGURE_THING_OPENTHREAD) {
            //this is the UUID if we are searching for a thing
            bluetoothWrapper.setScanUUID(Constants.OT_SETTINGS_SERVICE);
            this.gatewayID = gatewayID;
        }

        else if (operation == Constants.CONFIGURE_GATEWAY_WIFI) {
            //this is the UUID if we are searching for a gateway
            bluetoothWrapper.setScanUUID(Constants.WIFI_CONFIGURATION_SERVICE_GATEWAY);
        }

    }

    public void startScan() {
        deviceList = new ArrayList<>();
        bluetoothWrapper.stopScan();
        if (bluetoothWrapper.checkBluetoothHardware()) {
            bluetoothWrapper.scanForDevice(new ScannerCallback() {
                @Override
                public void onScanComplete(android.bluetooth.le.ScanResult result) {
                    //Add to adapter list as a connecting option
                    //Check if current device was already inserted on list

                    for (BluetoothDevice device : deviceList) {
                        if (device.getDevice().getAddress().equals(result.getDevice()
                                .getAddress())) {
                            deviceList.remove(device);
                            break;
                        }
                    }
                    deviceList.add(new BluetoothDevice(result.getDevice(),result.getRssi()));
                    Collections.sort(deviceList);
                    mViewModel.callbackOnDeviceFound(deviceList);
                }

                @Override
                public void onScanFail() {
                    mViewModel.callbackOnScanFail();
                }
            });
        }

        else {
             mViewModel.callbackOnBluetoothPermissionRequired();
        }
    }

    public void stopScan() {
        this.bluetoothWrapper.stopScan();
    }

    public void connectToDevice(BluetoothDevice device) {
        KnotSetupApplication.setBluetoothDevice(device);
        Bundle extras;

        if(operation == Constants.CONFIGURE_GATEWAY_WIFI) {
            mViewModel.callbackOnGatewayWifiConfiguration(gatewayID,operation);
        }
        else if (operation == Constants.CONFIGURE_THING_OPENTHREAD) {
            mViewModel.callbackOnThingSelected(operation);
        }
    }

}
