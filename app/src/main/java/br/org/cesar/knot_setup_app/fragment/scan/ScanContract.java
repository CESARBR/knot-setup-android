package br.org.cesar.knot_setup_app.fragment.scan;

import java.util.List;
import java.util.UUID;

import br.org.cesar.knot_setup_app.model.BluetoothDevice;

public interface ScanContract {

    interface ViewModel {
        void onDeviceFound(List<BluetoothDevice> deviceList);
        void onScanFail();
        void onBluetoothPermissionRequired();
        void onGatewayWifiConfiguration();
        void onThingSelected();
    }

    interface Presenter {
        void onFocus();
        void onFocusLost();
        void onDeviceSelected(BluetoothDevice device);
    }
}
