package br.org.cesar.knot_setup_app.activity.gatewayList;

import android.content.Context;

import java.util.ArrayList;

import br.org.cesar.knot_setup_app.persistence.mysqlDatabase.DBHelper;

public class GatewayPresenter implements GatewayContract.Presenter {

    private DBHelper dbHelper;
    private GatewayContract.ViewModel mViewModel;

    GatewayPresenter(GatewayContract.ViewModel viewModel, DBHelper dbHelper) {
        mViewModel = viewModel;
        this.dbHelper = dbHelper;
    }

    public void getAllGateways(){
        final ArrayList<String> deviceList = dbHelper.getAllKNoTDevices();
        mViewModel.callbackOnGatewaysFound(deviceList);
    }

    public void getGateway(String gatewayName){
        mViewModel.callBackOnGatewayFound(Integer.parseInt(dbHelper.getData("name",gatewayName)));
    }

}
