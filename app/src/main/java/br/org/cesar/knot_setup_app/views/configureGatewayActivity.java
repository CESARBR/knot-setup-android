package br.org.cesar.knot_setup_app.views;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.domain.callback.DeviceCallback;
import br.org.cesar.knot_setup_app.knotSetupApplication;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;
import br.org.cesar.knot_setup_app.wrapper.BluetoothWrapper;

public class configureGatewayActivity extends AppCompatActivity {

    private BluetoothWrapper bluetoothWrapper;
    private BluetoothDevice device;
    private String Channel = "12";
    private String NetName = "asdasdsa";
    private String PanID = "asdasdsa";
    private String Xpaind = "asdasdsa";
    private String IPV6 = "asdasdsa";

    private final UUID otSettingsService = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d30");
    private final UUID ChannelCharacteristic = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d31");
    private final UUID NetNameCharacteristic = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d32");
    private final UUID PanIDCharacteristic = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d33");
    private final UUID XpanidCharacteristic = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d34");
    private final UUID MasterKeyCharacteristic = UUID.fromString("a8a9e49c-aa9a-d441-9bec-817bb4900d35");

    private final UUID IPV6Service = UUID.fromString("49601183-5db4-498b-b35a-e6ddbe1c1470");
    private final UUID IPV6Characteristic = UUID.fromString("49601183-5db4-498b-b35a-e6ddbe1c1471");


    public static UUID SERVICE_UUID = UUID.fromString("414b9c8b-23f5-891a-46ed-410743e42425");
    public static UUID CHARACTERISTIC_SIGNED_CHALLENGE_UUID = UUID.fromString("414b9c8b-23f5-891a-46ed-410743e42427");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_gateway);
        this.bluetoothWrapper = knotSetupApplication.getBluetoothWrapper();
        this.device = knotSetupApplication.getBluetoothDevice();
        Log.d("DEV-LOG", this.device.getDevice().getName());
        callbackFlux();
    }

    private void writeWrapper(UUID service, UUID characteristic, String valtoWrite){
        Log.d("DEV-LOG", "writewrapper");
        this.bluetoothWrapper.write(service,characteristic,valtoWrite);
    }



    private void callbackFlux(){


        Log.d("DEV-LOG","CallbackFlux");
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
//                writeWrapper(otSettingsService,NetNameCharacteristic,NetName);
                Log.d("DEV-LOG","OnConnect");
                bluetoothWrapper.discoverServices();
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
                writeWrapper(otSettingsService,NetNameCharacteristic,"asdsdsa");
            }
            @Override
            public void onCharacteristicReadFail(){
                Log.d("DEV-LOG","Characteristic read failed");
            }

        });
    }

}
