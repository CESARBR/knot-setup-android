package br.org.cesar.knot_setup_app.activity.gatewayList;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.activity.scan.ScanActivity;
import br.org.cesar.knot_setup_app.activity.splash.SplashActivity;
import br.org.cesar.knot_setup_app.persistence.mysqlDatabase.DBHelper;
import br.org.cesar.knot_setup_app.domain.adapter.DeviceAdapter;
import br.org.cesar.knot_setup_app.activity.thingList.ThingActivity;

public class GatewayActivity extends AppCompatActivity implements  GatewayContract.ViewModel{

    private DeviceAdapter adapter;
    private GatewayPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_gateway);

        mPresenter = new GatewayPresenter(this,new DBHelper(this));

        //Add new device button interaction
        FloatingActionButton addButton = findViewById(R.id.add_gateway);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GatewayActivity.this, ScanActivity.class );
                startActivity(i);
            }
        });

        mPresenter.getAllGateways();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getAllGateways();
    }


    /**
     * Setup current devices list with smart devices saved on database
     */

    @Override
    public void callbackOnGatewaysFound(ArrayList<String> gatewayList) {
        final ArrayList<String> deviceList;
        deviceList = gatewayList;
        adapter = new DeviceAdapter(GatewayActivity.this, R.layout.item_gateway, deviceList);
        ListView deviceListView = findViewById(R.id.gateway_list);
        deviceListView.setAdapter(adapter);
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.getGateway(deviceList.get(position));
            }
        });
    }

    @Override
    public void callBackOnGatewayFound(int gatewayID){
        Intent i = new Intent(GatewayActivity.this, SplashActivity.class);
        i.putExtra("gatewayID", gatewayID);
        startActivity(i);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
