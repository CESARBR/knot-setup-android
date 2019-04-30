package br.org.cesar.knot_setup_app.data;
import android.util.Log;

import br.org.cesar.knot_setup_app.data.api.ApiService;
import br.org.cesar.knot_setup_app.data.api.ImplService;

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

}