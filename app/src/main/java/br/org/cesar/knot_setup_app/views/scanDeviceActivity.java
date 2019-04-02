package br.org.cesar.knot_setup_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.domain.callback.ScannerCallback;
import br.org.cesar.knot_setup_app.views.adapter.DeviceAdapter;
import br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;
import br.org.cesar.knot_setup_app.knotSetupApplication;

public class scanDeviceActivity  extends AppCompatActivity {


    private BluetoothWrapper bluetoothWrapper;
    private List<BluetoothDevice> deviceList;
    private DeviceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_for_devices);
        this.bluetoothWrapper = knotSetupApplication.getBluetoothWrapper();
        checkBluetooth();
    }


    private void checkBluetooth() {
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

        Toast.makeText(getApplicationContext(), "Start configuring your device...", Toast
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
                knotSetupApplication.setBluetoothDevice(device);

                Intent intent = new Intent(scanDeviceActivity.this, configureGatewayActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    protected void onDestroy(){
        super.onDestroy();
    }

}
