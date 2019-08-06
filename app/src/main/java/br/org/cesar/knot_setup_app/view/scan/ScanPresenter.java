package br.org.cesar.knot_setup_app.view.scan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import br.org.cesar.knot_setup_app.KnotSetupApplication;
import br.org.cesar.knot_setup_app.domain.callback.ScannerCallback;
import br.org.cesar.knot_setup_app.view.scan.ScanContract.Presenter;
import br.org.cesar.knot_setup_app.view.scan.ScanContract.ViewModel;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;
import br.org.cesar.knot_setup_app.utils.Constants;
import br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper;

public class ScanPresenter implements  Presenter {

    private ViewModel viewModel;
    private BluetoothWrapper bluetoothWrapper;
    private List<BluetoothDevice> deviceList;
    private UUID service;

    ScanPresenter(ViewModel viewModel, UUID service) {
        this.viewModel = viewModel;
        this.bluetoothWrapper = KnotSetupApplication.getBluetoothWrapper();
        this.service = service;
    }

    @Override
    public void onFocus() {
        //sets the UUID to be used by the bluetoothWrapper
        setUUID();
        //starts bluetooth scan
        startScan();
    }

    @Override
    public void onFocusLost() {
        //stop scanning for bluetooth devices
        stopScan();
        //clears the list with the found devices
        clearBluetoothDeviceList();
    }

    private void setUUID() {
        bluetoothWrapper.setScanUUID(this.service);
    }

    private void startScan() {
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
                    deviceList.add(new BluetoothDevice(result.getDevice(), result.getRssi()));
                    Collections.sort(deviceList);
                    viewModel.onDeviceFound(deviceList);
                }

                @Override
                public void onScanFail() {
                    viewModel.onScanFail();
                }
            });
        }

        else {
            viewModel.onBluetoothPermissionRequired();
        }
    }

    private void stopScan() {
        this.bluetoothWrapper.stopScan();
    }

    public void onDeviceSelected(BluetoothDevice device) {
        KnotSetupApplication.setBluetoothDevice(device);

        if(service == Constants.WIFI_CONFIGURATION_SERVICE_GATEWAY) {
            viewModel.onGatewayWifiConfiguration();
        } else if (service == Constants.OT_SETTINGS_SERVICE) {
            viewModel.onThingSelected();
        }
    }

    private void clearBluetoothDeviceList() {
        //empties the bluetoooth list
        deviceList.clear();
        //warns the view that the bluetooth list has been emptied
        viewModel.onDeviceFound(deviceList);
    }

}
