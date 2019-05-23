package br.org.cesar.knot_setup_app.activity.configureDevice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.activity.configureDevice.ConfigureDeviceContract.ViewModel;
import br.org.cesar.knot_setup_app.activity.configureDevice.ConfigureDeviceContract.Presenter;

public class ConfigureDeviceActivity extends AppCompatActivity implements ViewModel {
    private Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_gateway);

        TextView headerTitle = (TextView)findViewById(R.id.list_title);
        headerTitle.setText("Open Thread Configuration");
        ImageView image = (ImageView) findViewById(R.id.imageView1);
        image.setImageResource(R.drawable.knot);

        int gatewayID = getIntent().getIntExtra("gatewayID",0);
        boolean operation = (boolean) getIntent().getBooleanExtra("operation",false);
        this.mPresenter = new ConfigureDevicePresenter(this,gatewayID,operation,this);

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
                Toast.makeText(getApplicationContext(), "Successfully paired" +
                        "with device!!!", Toast.LENGTH_LONG).show();            }
        });
    }

    @Override
    public void callbackOnOperation(int val){
        switch (val){
            case 0:
                this.removeProgressBar("channel");
                break;
            case 1:
                this.removeProgressBar("net_name");
                break;
            case 2:
                this.removeProgressBar("pan_id");
                break;
            case 3:
                this.removeProgressBar("xpan_id");
                break;
            case 4:
                this.removeProgressBar("IPV6");
                break;
        }
    }

    public void removeProgressBar(String progressBarID){
        int resID = (int) getResources().getIdentifier(progressBarID + "_progress_bar","id",getPackageName());
        ProgressBar progressBar = findViewById(resID);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
