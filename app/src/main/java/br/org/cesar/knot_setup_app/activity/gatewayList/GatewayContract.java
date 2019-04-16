package br.org.cesar.knot_setup_app.activity.gatewayList;

import java.util.ArrayList;

public interface GatewayContract {

    interface ViewModel {
        void callbackOnGatewaysFound(ArrayList<String> deviceList);
        void callBackOnGatewayFound(int gatewayID);
    }

    interface Presenter {
        void getAllGateways();
    }
}
