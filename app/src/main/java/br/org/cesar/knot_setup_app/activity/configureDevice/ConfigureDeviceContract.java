package br.org.cesar.knot_setup_app.activity.configureDevice;

public interface ConfigureDeviceContract{

    interface ViewModel{
        void callbackOnConnected();
        void callbackOnDisconnected();
    }

    interface Presenter{
    }
}