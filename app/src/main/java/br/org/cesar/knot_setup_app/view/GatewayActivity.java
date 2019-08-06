package br.org.cesar.knot_setup_app.view;

import android.os.Bundle;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.view.gatewayConnected.GatewayConnectedFragment;
import br.org.cesar.knot_setup_app.view.scan.ScanFragment;
import br.org.cesar.knot_setup_app.model.Tab;
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

        Tab leftTab, rightTab;
        leftTab = new Tab(getString(R.string.gateway_left_tab_name), scanWifiFragment);
        rightTab = new Tab(getString(R.string.gateway_right_tab_name),
                new GatewayConnectedFragment());

        tabs.add(leftTab);
        tabs.add(rightTab);

        setupHeader(getString(R.string.gateway_header_title), tabs);
    }

}
