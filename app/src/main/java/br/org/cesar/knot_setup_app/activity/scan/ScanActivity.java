package br.org.cesar.knot_setup_app.activity.scan;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.domain.adapter.ScanAdapter;
import br.org.cesar.knot_setup_app.activity.configureDevice.ConfigureDeviceActivity;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;

public class ScanActivity extends AppCompatActivity implements  ScanContract.ViewModel {

    private ScanAdapter adapter;
    private ScanContract.Presenter mPresenter;
    private List<BluetoothDevice> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_for_devices);
        Boolean operation =  getIntent().getBooleanExtra("operation",false);
        int gatewayID = getIntent().getIntExtra("gatewayID",0);

        mPresenter = new ScanPresenter(this,operation,gatewayID);
        mPresenter.startScan();

        Toast.makeText(getApplicationContext(), "Start configuring your device...", Toast
                .LENGTH_LONG).show();

        setupAdapter();
    }

    /**
     * Setup device list adapter
     *
     */
    private void setupAdapter() {
        //Define list view and adapter
        deviceList = new ArrayList<>();
        adapter = new ScanAdapter(this, R.layout.item_device, deviceList);

        ListView listView = findViewById(R.id.device_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //When device is clicked we must connect to it
                final BluetoothDevice device = deviceList.get(position);
                mPresenter.connectToDevice(device);
            }
        });
    }

    @Override
    public void callbackOnGatewaySelected(int gatewayID, boolean operation){
        this.mPresenter.stopScan();
        Intent intent = new Intent(ScanActivity.this, ConfigureDeviceActivity.class);
        intent.putExtra("gatewayID",gatewayID);
        intent.putExtra("operation",operation);
        startActivity(intent);
        finish();
    }

    @Override
    public void callbackOnThingSelected(boolean operation) {
        this.mPresenter.stopScan();
        Intent intent = new Intent(ScanActivity.this, ConfigureDeviceActivity.class);
        intent.putExtra("operation",operation);
        startActivity(intent);
        finish();
    }

    @Override
    public void callbackOnDeviceFound(List<BluetoothDevice> deviceList){
        this.deviceList.clear();
        this.deviceList.addAll(deviceList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("DEV-LOG","notifyDatasetChanged");
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void callbackOnScanFail(){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Toast.makeText(ScanActivity.this, "Scan for device" +
                        " has failed. Check if your bluetooth is available.", Toast.LENGTH_LONG).show();
            }
        });

        finish();
    }

    @Override
    public void callbackOnBluetoothPermissionRequired() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        mPresenter.stopScan();
    }

}
