package br.org.cesar.knot_setup_app.wrapper;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import br.org.cesar.knot_setup_app.domain.callback.DeviceCallback;
import br.org.cesar.knot_setup_app.domain.callback.ScannerCallback;

import static android.bluetooth.BluetoothDevice.BOND_BONDED;
import static android.bluetooth.le.ScanSettings.MATCH_NUM_FEW_ADVERTISEMENT;

public class BluetoothWrapper {

    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private ScanCallback scanCallback;
    private BluetoothGatt my_gatt = null;


    private String TAG = "br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper";

    //Create class that instantiates a class with these characteristics

    public static UUID SERVICE_UUID = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d30");
    public static UUID STATE_CHARACTERISTIC  = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d32");

    public BluetoothWrapper(Context context) {
        this.context = context;
    }


    public boolean checkBluetoothHardware(Activity activity) {

        //Init bluetooth and check if it's enabled''
        boolean bluetoothEnabled = this.initBluetooth();

        //Check if user permission to user bluetooth
        if (!bluetoothEnabled) {
            //Request for user permission to enable bluetooth
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(intent);
        } else {
            //Check if android version requires location permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Request for user permission to use fine and coarse location
                int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED){
                    activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
        return  false;
    }

    /**
     * Init bluetooth manager and adapter
     * @return if bluetooth is enable
     */
    public boolean initBluetooth() {

        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // Check if bluetooth adapter is false
        if (bluetoothAdapter == null) { return false; }
        // Check if bluetooth is enabled
        if (!bluetoothAdapter.isEnabled()) { return false; }
        return true;
    }

    /**
     * Start scanning for new bluetooth devices
     * @param callback callback to the user
     */
    public void scanForDevice(final ScannerCallback callback) {
        //Start scanning to get a response
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                BluetoothDevice device = result.getDevice();
                if(device.getName() != null) {
                    callback.onScanComplete(result);
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                callback.onScanFail();
            }

        };

        if (bluetoothAdapter != null) {
            ScanFilter filter = new ScanFilter.Builder()
                    .setServiceUuid(new ParcelUuid(SERVICE_UUID)).build();
            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(MATCH_NUM_FEW_ADVERTISEMENT).build();
            bluetoothAdapter.getBluetoothLeScanner().startScan(
                    Collections.singletonList(filter),settings,scanCallback);

        }
    }

    public void stopScan(){
        bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }

    public boolean isConnected(){
        if(my_gatt == null){
            return false;
        }
        return true;
    }

    /**
     * Connect directly with the smart device
     * @param device bluetooth device found
     */
    public void connectToDevice(final br.org.cesar.knot_setup_app.model.BluetoothDevice device, final DeviceCallback callback) {

        my_gatt = device.getDevice().connectGatt(context, true, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                Log.d("DEV-LOG","onConnectionStateChange");
                if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("DEV-LOG","callback.onConnect");
                    callback.onConnect();
                }

                else {
                    callback.onDisconnect();
                }
            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt,status);

                if(status == gatt.GATT_SUCCESS) {
                    super.onServicesDiscovered(gatt, status);
                    callback.onServiceDiscoveryComplete();
                }
                else{
                    callback.onServiceDiscoveryFail();
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d(TAG, "Write successfully");
                    callback.onCharacteristicWriteComplete();
                }

                else {
                    Log.d(TAG, "Error during the write operation");
                    callback.onCharacteristicWriteFail();
                }
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);

                if(status == BluetoothGatt.GATT_SUCCESS){
                    callback.onReadRssiComplete(rssi);
                }
                else{
                    callback.onReadRssiFail();
                }

            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt,characteristic,status);

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    callback.onCharacteristicReadComplete(characteristic.getValue());
                }

                else{callback.onCharacteristicReadFail();}
            }

            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                if (characteristic.getUuid() == STATE_CHARACTERISTIC) {
                    callback.onCharacteristicChanged();
                }
            }
        });

    }



    public void getRssi(){
        my_gatt.readRemoteRssi();
    }


    public void write(UUID service ,UUID characteristic, String valToWrite){
        BluetoothGattCharacteristic writeCharacteristic = my_gatt.getService(service).getCharacteristic(characteristic);
        writeCharacteristic.setValue(valToWrite.getBytes());
        my_gatt.writeCharacteristic(writeCharacteristic);
    }

    public void write(UUID service ,UUID characteristic, byte[] valToWrite){
        BluetoothGattCharacteristic writeCharacteristic = my_gatt.getService(service).getCharacteristic(characteristic);
        writeCharacteristic.setValue(valToWrite);
        my_gatt.writeCharacteristic(writeCharacteristic);
    }

    public void readCharacteristic(UUID service ,UUID characteristic){
        BluetoothGattCharacteristic stateCharacteristic = my_gatt.getService(service).getCharacteristic(characteristic);
        my_gatt.readCharacteristic(stateCharacteristic);
    }

    public void closeConn(){
        my_gatt.disconnect();
    }

    public void closeGatt(){
        my_gatt.close();
    }

    public void discoverServices(){
        my_gatt.discoverServices();
    }

    /**
     * Wait for device to be bonded before connecting and writing characteristic
     * @param device bluetooth device
     * @param callback device callback
     */
    public void waitForBonding(final br.org.cesar.knot_setup_app.model.BluetoothDevice device, final DeviceCallback callback) {
        Log.d("DEV-LOG", "Waiting for bonding");
        if (device.getDevice().getBondState() == BluetoothDevice.BOND_BONDED) {
            Log.d("DEV-LOG", "BOND-STATE BONDED");
            connectToDevice(device, callback);
        } else {
            Log.d("DEV-LOG","Installation use case");
            //Wait for pin insertion request
            IntentFilter pinFilter = new IntentFilter();
            pinFilter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (device.getPin() != null) { device.getDevice().setPin(device.getPin().getBytes()); }
                    context.unregisterReceiver(this);
                    Log.d(TAG, "PIN " + device.getPin() + " inserted on device " + device.getDevice().getName());
                }
            }, pinFilter);

            // Create bond
            device.getDevice().createBond();

            //Listen to broadcast when device is bonded and ready to connect
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {
                        final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                        switch(state){
                            case BluetoothDevice.BOND_BONDING:
                                break;
                            case BluetoothDevice.BOND_BONDED:
                                context.unregisterReceiver(this);
                                connectToDevice(device, callback);
                                break;

                            case BluetoothDevice.BOND_NONE:
                                break;
                        }
                    }
                }
            }, filter);
        }
    }

}
