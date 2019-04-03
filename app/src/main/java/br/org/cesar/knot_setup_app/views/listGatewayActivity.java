package br.org.cesar.knot_setup_app.views;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;
import br.org.cesar.knot_setup_app.persistence.mysqlDatabase.DBHelper;
import br.org.cesar.knot_setup_app.views.adapter.gatewayAdapter;

public class listGatewayActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private gatewayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_gateway);

        dbHelper = new DBHelper(this);

        //Add new device button interaction
        FloatingActionButton addButton = findViewById(R.id.add_gateway);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(listGatewayActivity.this,scanDeviceActivity.class );
                startActivity(i);
            }
        });

        setupList();
    }


    /**
     * Setup current devices list with smart devices saved on database
     */
    private void setupList() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final ArrayList<String> deviceList = dbHelper.getAllKNoTDevices();
                adapter = new gatewayAdapter(listGatewayActivity.this, R.layout.item_gateway, deviceList);

                ListView deviceListView = findViewById(R.id.gateway_list);

                deviceListView.setAdapter(adapter);
                deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String gateway = deviceList.get(position);

                        Intent i = new Intent(listGatewayActivity.this,thingsListActivity.class);
                        i.putExtra("gatewayID", Integer.parseInt(dbHelper.getData("name",gateway)));
                        startActivity(i);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
