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

        Tab thingUnregisteredTab, thingRegisteredTab;
        thingUnregisteredTab = new Tab(getString(R.string.thing_unregistered), scanFragment);
        thingRegisteredTab = new Tab(getString(R.string.thing_registered), new ThingFragment());

        tabs.add(thingUnregisteredTab);
        tabs.add(thingRegisteredTab);

        setupHeader(getString(R.string.thing_header_title), tabs);
    }

}
