package br.org.cesar.knot_setup_app.activity.thingList;

import java.util.List;

import br.org.cesar.knot_setup_app.model.Gateway;

public interface  ThingContract{

    interface ViewModel{
        void callbackOnDeviceList(List<Gateway> deviceList);
    }

    interface Presenter {
        void getDeviceList();
    }

}