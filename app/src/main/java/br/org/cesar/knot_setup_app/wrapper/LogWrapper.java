package br.org.cesar.knot_setup_app.wrapper;
import android.content.Context;
import android.util.Log;

import br.org.cesar.knot_setup_app.BuildConfig;

public class LogWrapper {

    private static final String TAG = "DEV-LOG";

    public LogWrapper(){ }

    public static void Log(String msg, int logPriority){
        if(BuildConfig.DEBUG)
            switch (logPriority){
                case Log.VERBOSE: Log.v(TAG,msg);
                        break;
                case Log.DEBUG: Log.d(TAG,msg);
                        break;
                case Log.INFO: Log.i(TAG,msg);
                        break;
                case Log.WARN: Log.w(TAG,msg);
                        break;
                case Log.ERROR: Log.e(TAG,msg);
        }
    }

}
