package br.org.cesar.knot_setup_app.activity.splash;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.org.cesar.knot_setup_app.activity.splash.SplashContract.ViewModel;
import br.org.cesar.knot_setup_app.activity.splash.SplashContract.Presenter;
import br.org.cesar.knot_setup_app.data.DataManager;
import br.org.cesar.knot_setup_app.model.Gateway;
import br.org.cesar.knot_setup_app.model.State;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashPresenter implements Presenter{

    private ViewModel mViewModel;
    private static DataManager dataManager;
    private  String login,token,request,ip,port;
    private Context context;

    SplashPresenter(ViewModel viewModel, Context context){
        this.mViewModel = viewModel;
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
                .getPreference()
                .getSharedPreferenceString(context,"port");

        this.request = "http://" + ip +":" + port +  "/api/state";
    }

    @Override
    public void chooseActivity(){
        if(this.login != null && this.token != null){
            checkApi();
        }
        else{
            checkApi();
        }
    }

    public void checkApi(){
        dataManager.getInstance().getService().getState(this.request)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleStateSuccess,
                        this::onErrorHandler);
    }

    public void checkToken(){
        dataManager.getInstance().getService().getDevices(this.request,token)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleTokenSuccess,
                        this::onErrorHandler);
    }

    public void handleTokenSuccess(List<Gateway> devices){
        mViewModel.doListGateways();
    }

    private void handleStateSuccess(State state){
        Log.d("DEV-LOG","State: " + state.getState());
        if(state.getState().equals("ready")){
            this.request = "http://" + ip  + ":"+ this.port +"/api/devices";
            checkToken();
        }
        else{
            mViewModel.apiNotConfigured();
        }
    }

    private void onErrorHandler(Throwable throwable){
        if(throwable.getMessage().contains("401")){
            mViewModel.doLogin();
        }
        if(throwable instanceof IOException){
            mViewModel.callbackOnConnectionError();
        }
        Log.d("DEV-LOG", "onErrorHandler: " + throwable.getMessage());
    }

}