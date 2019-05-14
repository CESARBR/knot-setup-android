package br.org.cesar.knot_setup_app.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import br.org.cesar.knot_setup_app.utils.Constants;

public class PreferenceHelper {
    private static PreferenceHelper PreferenceHelper;

    private PreferenceHelper() { }

    public static PreferenceHelper getInstance() {
        if (PreferenceHelper == null) {
            PreferenceHelper = new PreferenceHelper();
        }
        return PreferenceHelper;
    }

    public void setSharedPreferenceString(Context context, String key, String value){
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFERENCE_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getSharedPreferenceString(Context context, String key){
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFERENCE_FILE, 0);
        return settings.getString(key, null);
    }

    public void clearPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFERENCE_FILE, 0);
        settings.edit().clear().apply();
    }
}
