package br.org.cesar.knot_setup_app.fragment.scan;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.activity.configureDevice.ConfigureDeviceActivity;
import br.org.cesar.knot_setup_app.activity.configureGatewayWifi.ConfigureGatewayWifiActivity;
import br.org.cesar.knot_setup_app.domain.adapter.ScanAdapter;
import br.org.cesar.knot_setup_app.fragment.scan.ScanContract.ViewModel;
import br.org.cesar.knot_setup_app.fragment.scan.ScanContract.Presenter;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;
import br.org.cesar.knot_setup_app.utils.Constants;
import br.org.cesar.knot_setup_app.wrapper.LogWrapper;

public class ScanFragment extends Fragment implements ViewModel {

    private ListView deviceListView;

    private ScanAdapter adapter;
    private Presenter presenter;
    private List<BluetoothDevice> deviceList;
    private UUID service;
    private Handler handler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container,false);

        deviceList = new ArrayList<>();
        adapter = new ScanAdapter(getActivity(), R.layout.item_device, deviceList);
        deviceListView = view.findViewById(R.id.list);

        presenter = new ScanPresenter(this, service);
        handler = new Handler(Looper.getMainLooper());

        setupAdapter();

        return view;
    }



    /**
     * Setup device list adapter
     *
     */
    private void setupAdapter() {
        //Define list view and adapter
        deviceListView.setAdapter(adapter);
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //When device is clicked we must connect to it
                final BluetoothDevice device = deviceList.get(position);
                presenter.onDeviceSelected(device);
            }
        });
    }

    @Override
    public void onGatewayWifiConfiguration() {
        Intent intent = new Intent(getActivity(), ConfigureGatewayWifiActivity.class);
        startActivity(intent);
    }

    @Override
    public void onThingSelected() {
        Intent intent = new Intent(getActivity(), ConfigureDeviceActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDeviceFound(List<BluetoothDevice> deviceList) {
        this.deviceList.clear();
        this.deviceList.addAll(deviceList);
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onScanFail() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), getString(R.string.all_scan_failed), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBluetoothPermissionRequired() {
        LogWrapper.Log("callbackOnBluetoothPermissionRequired", Log.DEBUG);
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        this.startActivity(intent);
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                Constants.BLUETOOTH_PERMISSION_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogWrapper.Log("ScanFragment onFocusLost",Log.DEBUG);
        presenter.onFocusLost();
    }

    public void setService(UUID service) {
        this.service = service;
    }

}
