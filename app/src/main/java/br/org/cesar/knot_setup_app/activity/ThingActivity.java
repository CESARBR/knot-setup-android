package br.org.cesar.knot_setup_app.activity;

import android.os.Bundle;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.fragment.scan.ScanFragment;
import br.org.cesar.knot_setup_app.fragment.thingRegistered.ThingFragment;
import br.org.cesar.knot_setup_app.model.Tab;
import br.org.cesar.knot_setup_app.utils.Constants;

public class ThingActivity extends BaseActivity {

    private ArrayList<Tab> tabs;

    private ScanFragment scanFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabs = new ArrayList<>();

        scanFragment = new ScanFragment();
        scanFragment.setService(Constants.OT_SETTINGS_SERVICE);

        Tab leftTab, rightTab;
        leftTab = new Tab(getString(R.string.thing_left_tab_name), scanFragment);
        rightTab = new Tab(getString(R.string.thing_right_tab_name), new ThingFragment());

        tabs.add(leftTab);
        tabs.add(rightTab);

        setupHeader(getString(R.string.thing_header_title),tabs);
    }

}
