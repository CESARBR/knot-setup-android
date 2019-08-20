package br.org.cesar.knot_setup_app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.model.Tab;
import br.org.cesar.knot_setup_app.fragment.gatewayConnected.GatewayConnectedFragment;

public class GatewayActivity extends BaseActivity {

    private ArrayList<Tab> tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabs = new ArrayList<>();

        Tab configureWiFiTab, gatewayConnectedTab;
        configureWiFiTab = new Tab(getString(R.string.gateway_configure_WiFi), new Fragment());
        gatewayConnectedTab = new Tab(getString(R.string.gateway_connected),
                new GatewayConnectedFragment());

        tabs.add(configureWiFiTab);
        tabs.add(gatewayConnectedTab);

        setupHeader(getString(R.string.gateway_header_title), tabs);
    }

}
