package br.org.cesar.knot_setup_app.views;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.persistence.mysqlDatabase.DBHelper;

public class thingsListActivity extends AppCompatActivity {
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_list);

        dbHelper = new DBHelper(this);


        ArrayList<String> Things = dbHelper.getAllGatewayThings();

        for(String thing : Things){
            Log.d("DEV-LOG","This gateway has: " + thing);
        }


        FloatingActionButton addButton = findViewById(R.id.add_thing);

        final int gatewayID = getIntent().getIntExtra("gatewayID",0);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Pass something that sinalizes the UUID i want to filter by
                Intent i = new Intent(thingsListActivity.this,scanDeviceActivity.class );
                i.putExtra("operation",true);
                i.putExtra("gatewayID",gatewayID);
                startActivity(i);
            }
        });


    }
}
