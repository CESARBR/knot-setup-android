package br.org.cesar.knot_setup_app.activity.gatewayList;

import android.net.nsd.NsdServiceInfo;

import java.util.ArrayList;

public interface GatewayContract {

    interface ViewModel {
        void callbackOnGatewaysFound(ArrayList<NsdServiceInfo> deviceList);
        void setSearchingFeedback(int visibility);
        void callbackOnMissingCharacteristic();
    }

    interface Presenter {
        void clearNsdServiceList();
        void setIpAndPort(NsdServiceInfo serviceInfo);
    }
}
