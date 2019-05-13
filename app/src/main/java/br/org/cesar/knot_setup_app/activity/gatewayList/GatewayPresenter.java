package br.org.cesar.knot_setup_app.activity.gatewayList;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.utils.Constants;

import static br.org.cesar.knot_setup_app.utils.Constants.DNS_SD_SERVICE_TYPE;

public class GatewayPresenter implements GatewayContract.Presenter {

    private GatewayContract.ViewModel mViewModel;

    private NsdManager nsdManager;
    private NsdManager.DiscoveryListener discoveryListener;

    private ArrayList<NsdServiceInfo> mService;

    GatewayPresenter(GatewayContract.ViewModel viewModel, NsdManager nsdManager) {
        mViewModel = viewModel;
        this.mService = new ArrayList<NsdServiceInfo>();
        this.nsdManager = nsdManager;
    }

    public void getAllGateways(){
        final ArrayList<NsdServiceInfo> deviceList = new ArrayList<NsdServiceInfo>();
        mViewModel.callbackOnGatewaysFound(deviceList);
    }

    public void getGateway(NsdServiceInfo nsdServiceInfo){
        Log.d("DEV-LOG","service: " + nsdServiceInfo);
        nsdManager.resolveService(nsdServiceInfo,new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails. Use the error code to debug.
                Log.e("DEV-LOG", "Resolve failed: " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e("DEV-LOG", "Resolve Succeeded. " + serviceInfo);
                mViewModel.callBackOnGatewayFound(1);
                if (serviceInfo.getServiceName().equals(Constants.DNS_SD_SERVICE_NAME)){
                    Log.d("DEV-LOG", "Same IP.");
                }
            }
        });
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
                Log.d("DEV-LOG", "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found! Do something with it.
                if (!service.getServiceType().equals(DNS_SD_SERVICE_TYPE)) {
                    Log.d("DEV-LOG", "Service: " + service);
                    if (service.getServiceName().contains(Constants.DNS_SD_SERVICE_NAME)) {
                        mService.add(service);
                        mViewModel.callbackOnGatewaysFound(mService);
                    }
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.d("DEV-LOG","onServiceLost");
                for (NsdServiceInfo info:mService) {
                    if(info.getServiceName().equals(service.getServiceName())){
                        Log.e("DEV-LOG", "service lost: " + service + "IndexOf: " + mService.indexOf(service));
                        mService.remove(info);
                    }
                }
                mViewModel.callbackOnGatewaysFound(mService);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i("DEV-LOG", "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e("DEV-LOG", "Discovery failed: Error code:" + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e("DEV-LOG", "Discovery failed: Error code:" + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }

        };

        nsdManager.discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, discoveryListener);

    }

}
