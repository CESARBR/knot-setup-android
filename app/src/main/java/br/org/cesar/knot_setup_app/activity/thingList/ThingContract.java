package br.org.cesar.knot_setup_app.activity.thingList;

import java.util.ArrayList;

public interface  ThingContract{

    interface ViewModel{
        void callbackOnDeviceList(ArrayList<String> deviceList);
    }

    interface Presenter {
        void getDeviceList();
    }

}