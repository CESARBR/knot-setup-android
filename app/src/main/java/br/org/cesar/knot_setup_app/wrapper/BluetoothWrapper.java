package br.org.cesar.knot_setup_app.wrapper;

import android.Manifest;
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

import java.util.Collections;
import java.util.UUID;

import br.org.cesar.knot_setup_app.domain.callback.DeviceCallback;
import br.org.cesar.knot_setup_app.domain.callback.ScannerCallback;
import br.org.cesar.knot_setup_app.wrapper.LogWrapper;

import static android.bluetooth.le.ScanSettings.MATCH_NUM_FEW_ADVERTISEMENT;

public class BluetoothWrapper {

    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private ScanCallback scanCallback;
    private BluetoothGatt myGatt = null;
    private UUID scanUUID;


    private String TAG = "br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper";

    //Create class that instantiates a class with these characteristics

    public static UUID STATE_CHARACTERISTIC  = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d32");

    public BluetoothWrapper(Context context) {
        this.context = context;
    }

    public void setScanUUID(UUID uuid){
        LogWrapper.Log("UUID: " + uuid.toString(), Log.DEBUG);
        this.scanUUID = uuid;
    }

    public boolean checkBluetoothHardware() {

        //Init bluetooth and check if it's enabled''
        boolean bluetoothEnabled = this.initBluetooth();

        //Check if user permission to user bluetooth
        if (!bluetoothEnabled) {
            //Request for user permission to enable bluetooth
        } else {
            //Check if android version requires location permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Request for user permission to use fine and coarse location
                int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED){
                    return false;
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
                    LogWrapper.Log("NAME: " + device.getName(), Log.DEBUG);
                    if(result.getScanRecord().getServiceUuids() != null) {
                        for (ParcelUuid uuid :result.getScanRecord().getServiceUuids()) {
                            LogWrapper.Log("      UUID: " + uuid.getUuid().toString(), Log.DEBUG);
                        }
                    }
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
            LogWrapper.Log("Searching for: "
                    + scanUUID.toString(), Log.DEBUG);
            ScanFilter filter = new ScanFilter.Builder()
                    .setServiceUuid(new ParcelUuid(scanUUID)).build();
            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(MATCH_NUM_FEW_ADVERTISEMENT).build();
            bluetoothAdapter.getBluetoothLeScanner().startScan(
                    Collections.singletonList(filter),settings,scanCallback);
        }
    }

    public void stopScan(){
        if(bluetoothAdapter != null  && bluetoothAdapter.isEnabled()
                && bluetoothAdapter.getScanMode() == bluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE
                && scanCallback != null) {
            bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
        }
    }

    public boolean isConnected(){
        if(myGatt == null){
            return false;
        }
        return true;
    }


    /**
     * Connect directly with the smart device
     * @param device bluetooth device found
     */
    public void connectToDevice(final br.org.cesar.knot_setup_app.model.BluetoothDevice device, final DeviceCallback callback) {

        myGatt = device.getDevice().connectGatt(context, true, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                LogWrapper.Log("onConnectionStateChange", Log.DEBUG);

                if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                    LogWrapper.Log("callback.onConnect", Log.DEBUG);
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
                    LogWrapper.Log("Write successfully", Log.DEBUG);
                    callback.onCharacteristicWriteComplete();
                }

                else {
                    LogWrapper.Log("Error during the write operation", Log.DEBUG);
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
        myGatt.readRemoteRssi();
    }


    public void write(UUID service ,UUID characteristic, String valToWrite){
        BluetoothGattCharacteristic writeCharacteristic = myGatt.getService(service).getCharacteristic(characteristic);
        writeCharacteristic.setValue(valToWrite.getBytes());
        myGatt.writeCharacteristic(writeCharacteristic);
    }

    public void write(UUID service ,UUID characteristic, byte[] valToWrite){
        BluetoothGattCharacteristic writeCharacteristic = myGatt.getService(service).getCharacteristic(characteristic);
        writeCharacteristic.setValue(valToWrite);
        myGatt.writeCharacteristic(writeCharacteristic);
    }

    public void readCharacteristic(UUID service ,UUID characteristic){
        BluetoothGattCharacteristic stateCharacteristic = myGatt.getService(service).getCharacteristic(characteristic);
        myGatt.readCharacteristic(stateCharacteristic);
    }

    public void closeConn(){
        myGatt.disconnect();
    }

    public void closeGatt(){
        myGatt.close();
    }

    public void discoverServices(){
        myGatt.discoverServices();
    }

    /**
     * Wait for device to be bonded before connecting and writing characteristic
     * @param device bluetooth device
     * @param callback device callback
     */
    public void waitForBonding(final br.org.cesar.knot_setup_app.model.BluetoothDevice device, final DeviceCallback callback) {
        LogWrapper.Log("Waiting for bonding", Log.DEBUG);
        if (device.getDevice().getBondState() == BluetoothDevice.BOND_BONDED) {
            LogWrapper.Log("BOND-STATE BONDED", Log.DEBUG);
            connectToDevice(device, callback);
        } else {
            LogWrapper.Log("Installation use case", Log.DEBUG);
            //Wait for pin insertion request
            IntentFilter pinFilter = new IntentFilter();
            pinFilter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (device.getPin() != null) { device.getDevice().setPin(device.getPin().getBytes()); }
                    context.unregisterReceiver(this);
                    LogWrapper.Log("PIN " + device.getPin() + " inserted on device " + device.getDevice().getName(), Log.DEBUG);
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
