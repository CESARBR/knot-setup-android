package br.org.cesar.knot_setup_app.activity.configureGatewayWifi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.org.cesar.knot_setup_app.activity.configureGatewayWifi.ConfigureGatewayWifiContract.ViewModel;
import br.org.cesar.knot_setup_app.activity.configureGatewayWifi.ConfigureGatewayWifiContract.Presenter;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.wrapper.LogWrapper;

public class ConfigureGatewayWifiActivity extends AppCompatActivity implements ViewModel {

    private TextView headerTitle;
    private ImageView headerImage;
    private EditText wifiEditText;
    private EditText passwEditText;
    private Button sendButton;

    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_gateway_wifi);

        headerTitle = findViewById(R.id.list_title);
        headerImage = findViewById(R.id.imageView1);
        wifiEditText = findViewById(R.id.wifi_editText);
        passwEditText = findViewById(R.id.password_editText);
        sendButton = findViewById(R.id.buttonSendWifiSettings);

        headerTitle.setText("Provide password");
        headerImage.setImageResource(R.drawable.knot);

        presenter = new ConfigureGatewayWifiPresenter(this);
        presenter.setWifiSSID();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButton.setEnabled(false);
                if(!(TextUtils.isEmpty(wifiEditText.getText()) ||
                        TextUtils.isEmpty(passwEditText.getText()))){
                            LogWrapper.Log("text not empty",Log.DEBUG);
                            presenter.writeGatewayWifiSettings(wifiEditText.getText().toString(),
                            passwEditText.getText().toString());
                }
            }
        });

    }

    @Override
    public void callbackOnDisconnected(){
        finish();
    }

    @Override
    public void callbackOnWriteFailed(){
        sendButton.setEnabled(true);
        Toast.makeText(this,"Failure when writing characteristic",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void callbackOnSetWifiSSID(String wifiName){
        String wifiNameWithoutQuotationMarks = wifiName.replaceAll("\"","");
        this.wifiEditText.setText(wifiNameWithoutQuotationMarks);
    }
}
