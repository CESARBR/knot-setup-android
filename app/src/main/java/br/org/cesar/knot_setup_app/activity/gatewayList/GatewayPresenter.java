package br.org.cesar.knot_setup_app.activity.gatewayList;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import br.org.cesar.knot_setup_app.data.DataManager;
import br.org.cesar.knot_setup_app.utils.Constants;
import br.org.cesar.knot_setup_app.wrapper.LogWrapper;

import static br.org.cesar.knot_setup_app.utils.Constants.DNS_SD_SERVICE_TYPE;

public class GatewayPresenter implements GatewayContract.Presenter {

    private GatewayContract.ViewModel mViewModel;
    private static DataManager dataManager;
    private Context context;
    private NsdManager nsdManager;
    private NsdManager.DiscoveryListener discoveryListener;

    private ArrayList<NsdServiceInfo> mService;

    GatewayPresenter(GatewayContract.ViewModel viewModel, NsdManager nsdManager, Context context) {
        mViewModel = viewModel;
        this.mService = new ArrayList<NsdServiceInfo>();
        this.nsdManager = nsdManager;
        this.context = context;
    }

    public void getGateway(NsdServiceInfo nsdServiceInfo){
        LogWrapper.Log("service: " + nsdServiceInfo, Log.DEBUG);
        nsdManager.resolveService(nsdServiceInfo,new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails. Use the error code to debug.
                LogWrapper.Log("Resolve failed: " + errorCode, Log.DEBUG);
                getGateway(serviceInfo);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {

                LogWrapper.Log("Resolve Succeeded. " + serviceInfo, Log.DEBUG);
                mService.add(serviceInfo);
                mViewModel.callbackOnGatewaysFound(mService);
                if (serviceInfo.getServiceName().equals(Constants.DNS_SD_SERVICE_NAME)){
                    LogWrapper.Log("Same IP.", Log.DEBUG);
                }
            }
        });
    }

    public void setIpAndPort(NsdServiceInfo serviceInfo){
        dataManager.getInstance().getPreference().
                setSharedPreferenceString(context,
                        "ip",serviceInfo.getHost().getHostAddress());

        dataManager.getInstance().getPreference().
                setSharedPreferenceString(context,
                        "port", String.valueOf(serviceInfo.getPort()));
    }


    public void stopScanning(){
        nsdManager.stopServiceDiscovery(discoveryListener);
    }

    public void clearNsdServiceList(){
        mService.clear();
    }

    public void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        discoveryListener = new NsdManager.DiscoveryListener() {

            // Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                LogWrapper.Log("Service discovery started", Log.DEBUG);
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found! Do something with it.
                if (!service.getServiceType().equals(DNS_SD_SERVICE_TYPE)) {
                    LogWrapper.Log("Service: " + service, Log.DEBUG);
                    removeFromListIfSameName(service);
                    if (service.getServiceName().startsWith(Constants.DNS_SD_SERVICE_NAME)) {
                        getGateway(service);
                    }
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                LogWrapper.Log("onServiceLost", Log.DEBUG);
                removeFromListIfSameName(service);
                mViewModel.callbackOnGatewaysFound(mService);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                LogWrapper.Log("Discovery stopped: "
                        + serviceType, Log.DEBUG);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                LogWrapper.Log("Discovery failed: Error code:"
                        + errorCode, Log.DEBUG);
                nsdManager.stopServiceDiscovery(this);
            }
            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                LogWrapper.Log("Discovery failed: Error code:"
                        + errorCode, Log.DEBUG);
                nsdManager.stopServiceDiscovery(this);
            }

        };

        nsdManager.discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, discoveryListener);

    }


    private boolean removeFromListIfSameName(NsdServiceInfo service){
        for (NsdServiceInfo info:mService) {
            if(info.getServiceName().equals(service.getServiceName())){
                mService.remove(info);
                return true;
            }
        }
        checkIfGatewayListIsEmpty();
        return false;
    }

    private void checkIfGatewayListIsEmpty(){
        LogWrapper.Log("isMserviceEmpty: " + mService.isEmpty(), Log.DEBUG);
        if(mService.isEmpty()){
            mViewModel.setSearchingFeedback(View.INVISIBLE);
        }

        else{
            mViewModel.setSearchingFeedback(View.VISIBLE);
        }

    }

}
