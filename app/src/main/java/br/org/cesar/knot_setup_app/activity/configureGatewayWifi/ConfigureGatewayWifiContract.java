package br.org.cesar.knot_setup_app.activity.configureGatewayWifi;

public class ConfigureGatewayWifiContract {

    interface ViewModel{
        void callbackOnSetWifiSSID(String wifiName);
        void callbackOnDisconnected();
        void callbackOnWriteFailed();
    }

    interface Presenter{
        void setWifiSSID();
        void writeGatewayWifiSettings(String ssid, String pssd);
    }
}
