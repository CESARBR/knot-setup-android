package br.org.cesar.knot_setup_app.activity;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.fragment.configureDevice.ConfigureDeviceFragment;
import br.org.cesar.knot_setup_app.model.Tab;

public class ConfigureDeviceActivity extends BaseActivity {

    private ArrayList<Tab> tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabs = new ArrayList<>();
        Tab tab = new Tab(getString(R.string.configure_device_tab_name), new ConfigureDeviceFragment());
        tabs.add(tab);

        setupHeader(getString(R.string.configure_device_header_title), tabs);
    }

}
