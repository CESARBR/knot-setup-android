package br.org.cesar.knot_setup_app;

import android.app.Application;

import br.org.cesar.knot_setup_app.model.BluetoothDevice;
import br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper;

// This class is used for sharing bluetooth between activities
public class knotSetupApplication extends Application {

    private static BluetoothWrapper bluetoothWrapper;
    private static BluetoothDevice bluetoothDevice;

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothWrapper = new BluetoothWrapper(this);
        bluetoothDevice = new BluetoothDevice(null,0);
    }

    public static BluetoothWrapper getBluetoothWrapper() {
        return bluetoothWrapper;
    }

    public static void setBluetoothDevice(BluetoothDevice device){bluetoothDevice = device;}

    public static BluetoothDevice getBluetoothDevice(){return bluetoothDevice;}
}
