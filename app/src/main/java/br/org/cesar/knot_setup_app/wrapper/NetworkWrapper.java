package br.org.cesar.knot_setup_app.wrapper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import br.org.cesar.knot_setup_app.wrapper.LogWrapper;

public class NetworkWrapper {

    private static Context context;
    public static final String WIFI_INTERFACE_IDENTIFIER = "WIFI";

    public NetworkWrapper(Context context){
        this.context = context;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            return activeNetworkInfo != null
                    && activeNetworkInfo.isConnected();
        }

        return false;
    }

    public static void forceSocketsThroughWifi(){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        for (Network network : connectivityManager.getAllNetworks()){
            String connectionType = connectivityManager.getNetworkInfo(network).getTypeName();
            LogWrapper.Log("network type: " + connectionType, Log.DEBUG);
            if(connectionType.equals(WIFI_INTERFACE_IDENTIFIER)){
                LogWrapper.Log("Wifi found!", Log.DEBUG);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.bindProcessToNetwork(network);
                }
                break;
            }
        }

    }

}
