package br.org.cesar.knot_setup_app.view.thingRegistered;

import java.util.List;

import br.org.cesar.knot_setup_app.model.Gateway;

public interface ThingContract {

    interface ViewModel {
        void onThingsFound(List<Gateway> deviceList);
    }

    interface Presenter {
        void onResume();
    }
}
