package br.org.cesar.knot_setup_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class listGatewayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_gateway);


        //Add new device button interaction
        FloatingActionButton addButton = findViewById(R.id.add_gateway);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Pass something that sinalizes the UUID i want to filter byx
                Intent i = new Intent(listGatewayActivity.this, scanForDevicesActivity.class);
                startActivity(i);
            }
        });

        //TODO: setup list of already connected devices
        //setupList();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
