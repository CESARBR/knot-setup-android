package br.org.cesar.knot_setup_app.view;

import android.os.Bundle;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.model.Tab;
import br.org.cesar.knot_setup_app.view.gatewayConnected.GatewayConnectedFragment;
import br.org.cesar.knot_setup_app.view.scan.ScanFragment;

import br.org.cesar.knot_setup_app.utils.Constants;

public class GatewayActivity extends BaseActivity {

    private ArrayList<Tab> tabs;

    private ScanFragment scanWifiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabs = new ArrayList<>();

        //Setting the scan to look for a specific UUID
        scanWifiFragment = new ScanFragment();
        scanWifiFragment.setService(Constants.WIFI_CONFIGURATION_SERVICE_GATEWAY);


        Tab configureWiFiTab, gatewayConnectedTab;
        configureWiFiTab = new Tab(getString(R.string.gateway_configure_WiFi), scanWifiFragment);
        gatewayConnectedTab = new Tab(getString(R.string.gateway_connected),
                new GatewayConnectedFragment());

        tabs.add(configureWiFiTab);
        tabs.add(gatewayConnectedTab);

        setupHeader(getString(R.string.gateway_header_title), tabs);

        int backgroundColor = getResources().getColor(R.color.colorWhite);
        setTabBackground(backgroundColor);
    }

}
