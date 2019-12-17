package br.org.cesar.knot_setup_app.view;

import android.os.Bundle;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.view.configureGatewayWifi.ConfigureGatewayWifiFragment;
import br.org.cesar.knot_setup_app.model.Tab;

import static br.org.cesar.knot_setup_app.utils.Constants.GATEWAY_NAME;

public class ConfigureGatewayWifiActivity extends BaseActivity {

    private ArrayList<Tab> tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabs = new ArrayList<>();

        String thingName = getIntent().getStringExtra(GATEWAY_NAME);
        Tab tab = new Tab(thingName, new ConfigureGatewayWifiFragment());
        tabs.add(tab);

        setupHeader(getString(R.string.configure_gateway_wifi_header_title), tabs);

        int textColor = getResources().getColor(R.color.colorWhite);
        setTextColor(textColor);
    }

}
