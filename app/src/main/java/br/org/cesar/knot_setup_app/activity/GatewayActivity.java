package br.org.cesar.knot_setup_app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.model.Tab;

public class GatewayActivity extends BaseActivity {

    private ArrayList<Tab> tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabs = new ArrayList<>();

        Tab leftTab, rightTab;
        leftTab = new Tab("left-tab", new Fragment());
        rightTab = new Tab("right-tab",
                new Fragment());

        tabs.add(leftTab);
        tabs.add(rightTab);

        setupHeader(getString(R.string.gateway_header_title), tabs);
    }

}
