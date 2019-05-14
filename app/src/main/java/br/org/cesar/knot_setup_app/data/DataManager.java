package br.org.cesar.knot_setup_app.data;
import android.util.Log;

import br.org.cesar.knot_setup_app.data.api.ApiService;
import br.org.cesar.knot_setup_app.data.api.ImplService;
import br.org.cesar.knot_setup_app.data.preferences.PersistentPreferenceHelper;
import br.org.cesar.knot_setup_app.data.preferences.PreferenceHelper;

public class DataManager {

    private static DataManager mDataManager;

    public DataManager() {}

    public static DataManager getInstance() {
        if (mDataManager == null) {
            mDataManager = new DataManager();
        }
        return mDataManager;
    }


    public ImplService getService()
    {
        return ApiService.getInstance().getImplService();
    }

    public PersistentPreferenceHelper getPersistentPreference(){return  PersistentPreferenceHelper.getInstance();}
    public PreferenceHelper getPreference(){return  PreferenceHelper.getInstance();}
}