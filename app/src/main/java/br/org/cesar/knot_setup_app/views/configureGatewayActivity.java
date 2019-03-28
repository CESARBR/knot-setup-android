package br.org.cesar.knot_setup_app.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.domain.callback.DeviceCallback;
import br.org.cesar.knot_setup_app.knotSetupApplication;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;
import br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper;

public class configureGatewayActivity extends AppCompatActivity {

    private BluetoothWrapper bluetoothWrapper;
    private BluetoothDevice device;
    String Channel = "12";
    String NetName = "asdasdsa";
    String PanID = "asdasdsa";
    String Xpaind = "asdasdsa";
    String IPV6 = "asdasdsa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_gateway);
        this.bluetoothWrapper = knotSetupApplication.getBluetoothWrapper();
        this.device = knotSetupApplication.getBluetoothDevice();
        callbackFlux();

    }

    private void writeWrapper(String toWrite){
    }



    private void callbackFlux(){

        bluetoothWrapper.waitForBonding(device, new DeviceCallback() {
            @Override
            public void onConnect() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(configureGatewayActivity.this, "Scan for device " +
                                "successful!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onCharacteristicChanged(){
                Log.d("DEV-LOG","Characteristic changed?");
            }
            @Override
            public void onDisconnect(){
                Log.d("DEV-LOG","Disconnected");
            }
            @Override
            public void onServiceDiscoveryComplete(){
                Log.d("DEV-LOG","Services discovered");
            }

            @Override
            public void onServiceDiscoveryFail(){
                Log.d("DEV-LOG","Service discovery failed");
            }

            @Override
            public void onCharacteristicWriteComplete(){
                Log.d("DEV-LOG","Characteristic write");

            }

            @Override
            public void onCharacteristicWriteFail(){
                Log.d("DEV-LOG","Characteristic write failed");
            }
            @Override
            public void onReadRssiComplete(){
                Log.d("DEV-LOG","Rssi read");
            }
            @Override
            public void onReadRssiFail(){
                Log.d("DEV-LOG","Rssi read failed");
            }

            @Override
            public void onCharacteristicReadComplete(){
                Log.d("DEV-LOG","Characteristic read");
            }
            @Override
            public void onCharacteristicReadFail(){
                Log.d("DEV-LOG","Characteristic read failed");
            }

        });
    }

}
