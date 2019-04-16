package br.org.cesar.knot_setup_app.activity.thingList;

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
import br.org.cesar.knot_setup_app.persistence.mysqlDatabase.DBHelper;
import br.org.cesar.knot_setup_app.domain.adapter.DeviceAdapter;
import br.org.cesar.knot_setup_app.activity.thingList.ThingContract.Presenter;
import br.org.cesar.knot_setup_app.activity.thingList.ThingContract.ViewModel;

public class ThingActivity extends AppCompatActivity implements  ViewModel{
    private DBHelper dbHelper;
    private DeviceAdapter adapter;
    private Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_list);

        final int gatewayID = getIntent().getIntExtra("gatewayID",0);
        dbHelper = new DBHelper(this);
        mPresenter = new ThingPresenter(this,dbHelper,gatewayID);


        FloatingActionButton addButton = findViewById(R.id.add_thing);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ThingActivity.this, ScanActivity.class );
                i.putExtra("operation",true);
                i.putExtra("gatewayID",gatewayID);
                startActivity(i);
            }
        });

        mPresenter.getDeviceList();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getDeviceList();
    }


    /**
     * Setup current devices list with smart devices saved on database
     */

    @Override
    public void callbackOnDeviceList(ArrayList<String> deviceList){

        adapter = new DeviceAdapter(ThingActivity.this, R.layout.item_gateway, deviceList);

        ListView deviceListView = findViewById(R.id.thing_list);

        deviceListView.setAdapter(adapter);

        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
