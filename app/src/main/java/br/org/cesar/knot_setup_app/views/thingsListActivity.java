package br.org.cesar.knot_setup_app.views;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import br.org.cesar.knot_setup_app.R;

public class thingsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_list);


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
