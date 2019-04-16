package br.org.cesar.knot_setup_app.activity.thingList;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.activity.gatewayList.GatewayContract;
import br.org.cesar.knot_setup_app.activity.thingList.ThingContract.ViewModel;
import br.org.cesar.knot_setup_app.activity.thingList.ThingContract.Presenter;
import br.org.cesar.knot_setup_app.persistence.mysqlDatabase.DBHelper;

public class  ThingPresenter implements Presenter{
    private ViewModel mViewModel;
    private DBHelper dbHelper;
    private int gatewayID;

    ThingPresenter(ViewModel viewModel,DBHelper dbHelper,int gatewayID) {
        mViewModel = viewModel;
        this.dbHelper = dbHelper;
        this.gatewayID = gatewayID;
    }

    public void getDeviceList(){
        this.mViewModel.callbackOnDeviceList(this.dbHelper.getAllGatewayThings());
    }



}