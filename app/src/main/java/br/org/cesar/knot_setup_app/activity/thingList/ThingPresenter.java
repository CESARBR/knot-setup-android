package br.org.cesar.knot_setup_app.activity.thingList;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import br.org.cesar.knot_setup_app.activity.thingList.ThingContract.ViewModel;
import br.org.cesar.knot_setup_app.activity.thingList.ThingContract.Presenter;
import br.org.cesar.knot_setup_app.data.DataManager;
import br.org.cesar.knot_setup_app.model.Gateway;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class  ThingPresenter implements Presenter{
    private ViewModel mViewModel;
    private int gatewayID;
    private static DataManager dataManager;
    private static String token,login,ip,request,port;
    private Context context;

    ThingPresenter(ViewModel viewModel, int gatewayID, Context context) {
        mViewModel = viewModel;
        this.gatewayID = gatewayID;
        this.context = context;

        this.login = dataManager.getInstance()
                .getPersistentPreference()
                .getSharedPreferenceString(context,"email");

        this.token = dataManager.getInstance()
                .getPersistentPreference()
                .getSharedPreferenceString(context,"token");

        this.ip = dataManager.getInstance()
                .getPreference()
                .getSharedPreferenceString(context,"ip");

        this.port = dataManager.getInstance()
                .getPreference().getSharedPreferenceString(context,"port");

        this.request = "http://" + ip + ":" + port +"/api/devices";
    }


    public void getDeviceList(){
        Log.d("DEV-LOG","Request: " + this.request);
        dataManager.getInstance().getService().getDevices(this.request,token)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleDeviceSuccess,
                        this::onErrorHandler);
    }

    private void handleDeviceSuccess(List<Gateway> gat){
        if(!gat.isEmpty()) {
            this.mViewModel.callbackOnDeviceList(gat);
        }
        else{
            Log.d("DEV-LOG","There are no devices avaiable.");
        }
    }

    private void onErrorHandler(Throwable throwable){
        Log.d("DEV-LOG", "onErrorHandler: " + throwable.getMessage());
    }

}