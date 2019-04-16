package br.org.cesar.knot_setup_app.activity.configureDevice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.activity.configureDevice.ConfigureDeviceContract.ViewModel;
import br.org.cesar.knot_setup_app.activity.configureDevice.ConfigureDeviceContract.Presenter;
import br.org.cesar.knot_setup_app.persistence.mysqlDatabase.DBHelper;

public class ConfigureDeviceActivity extends AppCompatActivity implements ViewModel {
    private Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_gateway);
        DBHelper dbHelper = new DBHelper(this);

        int gatewayID = getIntent().getIntExtra("gatewayID",0);
        boolean operation = (boolean) getIntent().getBooleanExtra("operation",false);
        this.mPresenter = new ConfigureDevicePresenter(this,gatewayID,operation,dbHelper);

    }

    @Override
    public void callbackOnDisconnected(){
        finish();
    }

    @Override
    public void callbackOnConnected(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Scan for device " +
                        "successful!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
