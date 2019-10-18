package br.org.cesar.knot_setup_app.view.thingRegistered;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import br.org.cesar.knot_setup_app.data.DataManager;
import br.org.cesar.knot_setup_app.model.Gateway;
import br.org.cesar.knot_setup_app.utils.Constants;
import br.org.cesar.knot_setup_app.wrapper.LogWrapper;
import br.org.cesar.knot_setup_app.view.thingRegistered.ThingContract.Presenter;
import br.org.cesar.knot_setup_app.view.thingRegistered.ThingContract.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class  ThingPresenter implements Presenter{

    private ViewModel mViewModel;
    private static DataManager dataManager;
    private static String token,ip,request,port;
    private Context context;

    ThingPresenter(ViewModel viewModel, Context context) {
        mViewModel = viewModel;
        this.context = context;

        this.token = dataManager.getInstance()
                .getPersistentPreference()
                .getSharedPreferenceString(context, Constants.GATEWAY_TOKEN);

        this.ip = dataManager.getInstance()
                .getPreference()
                .getSharedPreferenceString(context, Constants.GATEWAY_IP);

        this.port = dataManager.getInstance()
                .getPreference().getSharedPreferenceString(context, Constants.GATEWAY_PORT);

        this.request = "http://" + ip + ":" + port +"/api/devices";
    }

    public void onFocus(){
        LogWrapper.Log("request= " + this.request, Log.DEBUG);
        dataManager.getInstance().getService().getDevices(this.request,token)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleDeviceSuccess,
                        this::onErrorHandler);
    }

    private void handleDeviceSuccess(List<Gateway> gat){
        if(!gat.isEmpty()) {
            this.mViewModel.onThingsFound(gat);
        }
        else{
            LogWrapper.Log("There are no devices avaiable.", Log.DEBUG);
        }
    }

    private void onErrorHandler(Throwable throwable){
        LogWrapper.Log("onErrorHandler: " + throwable.getMessage(), Log.DEBUG);
    }

}
