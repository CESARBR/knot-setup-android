package br.org.cesar.knot_setup_app.activity.scan;

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.knot_setup_app.model.BluetoothDevice;

public interface ScanContract {

    interface ViewModel {
        void callbackOnDeviceFound(List<BluetoothDevice> deviceList);
        void callbackOnScanFail();
        void callbackOnBluetoothPermissionRequired();
        void callbackOnGatewayWifiConfiguration(int gatewayID, int operation);
        void callbackOnThingSelected(int operation);
        }

    interface Presenter {
        void startScan();
        void connectToDevice(BluetoothDevice device);
        void stopScan();
    }

}
