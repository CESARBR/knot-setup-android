package br.org.cesar.knot_setup_app.activity.gatewayList;

import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.activity.scan.ScanActivity;
import br.org.cesar.knot_setup_app.activity.splash.SplashActivity;
import br.org.cesar.knot_setup_app.domain.adapter.GatewayAdapter;

public class GatewayActivity extends AppCompatActivity implements  GatewayContract.ViewModel{

    private GatewayAdapter adapter;
    private GatewayPresenter mPresenter;
    private ArrayList<NsdServiceInfo> deviceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_gateway);

        setAdapter();

        TextView headerTitle = (TextView)findViewById(R.id.list_title);
        headerTitle.setText("Gateways");
        ImageView image = (ImageView) findViewById(R.id.imageView1);
        image.setImageResource(R.drawable.knot);



        mPresenter = new GatewayPresenter(this,
                (NsdManager) this.getSystemService(Context.NSD_SERVICE)
                ,this);

        FloatingActionButton addButton = findViewById(R.id.add_gateway);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.stopScanning();
                Intent i = new Intent(GatewayActivity.this, ScanActivity.class );
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mPresenter.clearNsdServiceList();
        this.mPresenter.initializeDiscoveryListener();
    }


    /**
     * Setup current devices list with smart devices saved on database
     */
    @Override
    public void callbackOnGatewaysFound(ArrayList<NsdServiceInfo> deviceListReceived){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                deviceList.clear();
                deviceList.addAll(deviceListReceived );
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void setAdapter() {
        deviceList = new ArrayList<>();
        adapter = new GatewayAdapter(GatewayActivity.this, R.layout.item_gateway, deviceList);
        Log.d("DEV-LOG","CREATING ADAPTER");
        ListView deviceListView = findViewById(R.id.gateway_list);
        deviceListView.setAdapter(adapter);
        Log.d("DEV-LOG","ADAPTER SET");
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.stopScanning();
                Intent i = new Intent(GatewayActivity.this, SplashActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void callbackOnMissingCharacteristic(){
        Toast.makeText(this,"" +
                "Some configuration has been forgotten",Toast.LENGTH_LONG);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mPresenter.stopScanning();
    }
}
