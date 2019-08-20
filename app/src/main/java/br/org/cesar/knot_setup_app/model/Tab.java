package br.org.cesar.knot_setup_app.model;

import android.support.v4.app.Fragment;

public class Tab {

    private Fragment fragment;
    private String tabName;

    public Tab(String tabName, Fragment fragment) {
        this.fragment = fragment;
        this.tabName = tabName;
    }

    public Fragment getFragment() {
        return this.fragment;
    }

    public  String getTabName() {
        return tabName;
    }

}
