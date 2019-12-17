package br.org.cesar.knot_setup_app.view.scan;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;
import android.view.View;

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
import br.org.cesar.knot_setup_app.wrapper.LogWrapper;

public class ScanPresenter implements  Presenter {

    private ViewModel viewModel;
    private BluetoothWrapper bluetoothWrapper;
    private List<BluetoothDevice> deviceList;
    private UUID service;
    private BroadcastReceiver bluetoothBroadcastReceiver;
    private Context context;
    private ScannerCallback scannerCallback;
    private ScanCallback scanCallback;

    ScanPresenter(ViewModel viewModel, UUID service, Context context) {
        this.viewModel = viewModel;
        this.bluetoothWrapper = KnotSetupApplication.getBluetoothWrapper();
        this.service = service;

        scannerCallback = new ScannerCallback() {
            @Override
            public void onScanComplete(android.bluetooth.le.ScanResult result) {
                //Add to adapter list as a connecting option
                //Check if current device was already inserted on list
                String resMAC = result.getDevice().getAddress();
                for (BluetoothDevice device : deviceList) {
                    if (device.getDevice().getAddress().equals(resMAC)) {
                        deviceList.remove(device);
                        break;
                    }
                }
                deviceList.add(new BluetoothDevice(result.getDevice(),result.getRssi()));
                Collections.sort(deviceList);
                viewModel.onDeviceFound(deviceList);
            }

            @Override
            public void onScanFail() {
                viewModel.onScanFail();
            }
        };

        scanCallback  = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                android.bluetooth.BluetoothDevice device = result.getDevice();
                if(device.getName() != null) {
                    scannerCallback.onScanComplete(result);
                }
            }
            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                scannerCallback.onScanFail();
            }
        };

        setBluetoothBroadcastReceiver();
        this.context = context;
    }

    @Override
    public void onFocus() {
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(bluetoothBroadcastReceiver, filter);
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
        context.unregisterReceiver(bluetoothBroadcastReceiver);
    }

    private void setUUID() {
        bluetoothWrapper.setScanUUID(this.service);
    }

    private void startScan() {
        deviceList = new ArrayList<>();
        if (bluetoothWrapper.checkBluetoothHardware()) {
            bluetoothWrapper.scanForDevice(scanCallback);
        }

        else {
            viewModel.onBluetoothPermissionRequired();
        }
    }

    private void stopScan() {
        this.bluetoothWrapper.stopScan(scanCallback);
    }

    public void onDeviceSelected(BluetoothDevice device) {
        KnotSetupApplication.setBluetoothDevice(device);

        if(service == Constants.WIFI_CONFIGURATION_SERVICE_GATEWAY) {
            viewModel.onGatewayWifiConfiguration(device.getDevice().getName());
        } else if (service == Constants.OT_SETTINGS_SERVICE) {
            viewModel.onThingSelected(device.getDevice().getName());
        }
    }

    public void setBluetoothBroadcastReceiver() {
        bluetoothBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    switch(state) {
                        case BluetoothAdapter.STATE_OFF:
                            LogWrapper.Log("Bluetooth off", Log.DEBUG);
                            viewModel.setBluetoothFeedback(View.VISIBLE);
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            LogWrapper.Log("Bluetooth turning off", Log.DEBUG);
                            clearBluetoothDeviceList();
                            break;
                        case BluetoothAdapter.STATE_ON:
                            LogWrapper.Log("Bluetooth on", Log.DEBUG);
                            startScan();
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            LogWrapper.Log("Bluetooth turning on", Log.DEBUG);
                            viewModel.setBluetoothFeedback(View.INVISIBLE);
                            break;
                    }
                }
            }
        };

    }

    private void clearBluetoothDeviceList() {
        //empties the bluetoooth list
        deviceList.clear();
        //warns the view that the bluetooth list has been emptied
        viewModel.onDeviceFound(deviceList);
    }

}
