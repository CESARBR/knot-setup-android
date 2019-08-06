package br.org.cesar.knot_setup_app.view.configureGatewayWifi;

public interface ConfigureGatewayWifiContract {

    interface ViewModel {
        void setWifiSSID(String wifiName);
        void onDisconnected();
        void onWriteFailed();
    }

    interface Presenter {
        void onFocus();
        void onSetWifiClicked(String ssid, String pwd);
    }
}
