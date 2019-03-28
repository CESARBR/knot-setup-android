package br.org.cesar.knot_setup_app.views;

import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.domain.callback.DeviceCallback;
import br.org.cesar.knot_setup_app.domain.callback.ScannerCallback;
import br.org.cesar.knot_setup_app.persistence.mysqlDatabase.DBHelper;
import br.org.cesar.knot_setup_app.views.adapter.DeviceAdapter;
import br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;

public class scanDeviceActivity  extends AppCompatActivity {

    private DBHelper mydb ;

    private BluetoothWrapper bluetoothWrapper;
    private List<BluetoothDevice> deviceList;
    private DeviceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_for_devices);
        checkBluetooth();

    }


    private void checkBluetooth() {
        bluetoothWrapper = new BluetoothWrapper(this);
        //Check for bluetooth hardware and start scanning for device
        if (bluetoothWrapper.checkBluetoothHardware(scanDeviceActivity.this)) {
            startScan();
        }
    }


    /**
     * Start scanning for bluetooth devices and establish a connection to exchange keys
     *
     */
    private void startScan() {

        Toast.makeText(scanDeviceActivity.this, "Start configuring your device...", Toast
                .LENGTH_LONG).show();
        setupAdapter();

        new Thread(new Runnable() {
            @Override
            public void run() {
                bluetoothWrapper.scanForDevice(new ScannerCallback(){
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }

                            @Override
                            public void onScanFail() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(scanDeviceActivity.this, "Scan for device" +
                                                " has failed.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
            }
        }).start();
    }


    /**
     * Setup device list adapter
     *
     */
    private void setupAdapter() {

        //Define list view and adapter
        deviceList = new ArrayList<>();
        adapter = new DeviceAdapter(this, R.layout.item_device, deviceList);

        ListView listView = findViewById(R.id.device_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //When device is clicked we must connect to it
                final BluetoothDevice device = deviceList.get(position);
                //Generate bluetooth key
                bluetoothWrapper.waitForBonding(device, new DeviceCallback() {
                    @Override
                    public void onConnect() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(scanDeviceActivity.this, "Scan for device " +
                                        "successful!", Toast.LENGTH_LONG).show();

                                //Send intent to configure device
                                Intent intent = new Intent(scanDeviceActivity.this, configureGatewayActivity.class);
                                intent.putExtra("deviceName", device.getDevice().getName());

                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    @Override
                    public void onCharacteristicChanged(){
                        Log.d("DEV-LOG","Characteristic changed?");
                    }
                    @Override
                    public void onDisconnect(){
                        Log.d("DEV-LOG","Disconnected");
                    }
                    @Override
                    public void onServiceDiscoveryComplete(){
                        Log.d("DEV-LOG","Services discovered");
                    }

                    @Override
                    public void onServiceDiscoveryFail(){
                        Log.d("DEV-LOG","Service discovery failed");
                    }

                    @Override
                    public void onCharacteristicWriteComplete(){
                        Log.d("DEV-LOG","Characteristic write");

                    }

                    @Override
                    public void onCharacteristicWriteFail(){
                        Log.d("DEV-LOG","Characteristic write failed");
                    }
                    @Override
                    public void onReadRssiComplete(){
                        Log.d("DEV-LOG","Rssi read");
                    }
                    @Override
                    public void onReadRssiFail(){
                        Log.d("DEV-LOG","Rssi read failed");
                    }

                    @Override
                    public void onCharacteristicReadComplete(){
                        Log.d("DEV-LOG","Characteristic read");
                    }
                    @Override
                    public void onCharacteristicReadFail(){
                        Log.d("DEV-LOG","Characteristic read failed");
                    }

                });
            }
        });
    }

    protected void onDestroy(){
        super.onDestroy();
        //TODO: check if scan was on before...
        if(bluetoothWrapper.isConnected()){
            bluetoothWrapper.closeConn();
        }
    }

}
