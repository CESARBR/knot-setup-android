package br.org.cesar.knot_setup_app.view;

import android.os.Bundle;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.view.configureDevice.ConfigureDeviceFragment;
import br.org.cesar.knot_setup_app.model.Tab;

import static br.org.cesar.knot_setup_app.utils.Constants.THING_NAME;

public class ConfigureDeviceActivity extends BaseActivity {

    private ArrayList<Tab> tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabs = new ArrayList<>();
        String thingName = getIntent().getStringExtra(THING_NAME);
        Tab tab = new Tab(thingName, new ConfigureDeviceFragment());
        tabs.add(tab);

        setupHeader(getString(R.string.configure_device_header_title), tabs);

        int textColor = getResources().getColor(R.color.colorWhite);
        setTextColor(textColor);
    }

}
