package br.org.cesar.knot_setup_app.views;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.persistence.mysqlDatabase.DBHelper;
import br.org.cesar.knot_setup_app.views.adapter.gatewayAdapter;

public class thingsListActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private gatewayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_list);

        dbHelper = new DBHelper(this);

        FloatingActionButton addButton = findViewById(R.id.add_thing);

        final int gatewayID = getIntent().getIntExtra("gatewayID",0);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(thingsListActivity.this,scanDeviceActivity.class );
                i.putExtra("operation",true);
                i.putExtra("gatewayID",gatewayID);
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

                final ArrayList<String> deviceList = dbHelper.getAllGatewayThings();

                adapter = new gatewayAdapter(thingsListActivity.this, R.layout.item_gateway, deviceList);

                ListView deviceListView = findViewById(R.id.thing_list
                );

                deviceListView.setAdapter(adapter);

                deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                });
            }
        }).start();
    }


}
