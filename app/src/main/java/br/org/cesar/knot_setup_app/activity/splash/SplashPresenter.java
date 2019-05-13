package br.org.cesar.knot_setup_app.activity.splash;

import android.database.Cursor;
import android.util.Log;

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
    private  String login,token;

    SplashPresenter(ViewModel viewModel){
        this.mViewModel = viewModel;
    }


    @Override
    public void chooseActivity(){
    }

    public void checkApi(){
        dataManager.getInstance().getService().getState()
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleStateSuccess,
                        this::onErrorHandler);
    }

    public void checkToken(){
        dataManager.getInstance().getService().getDevices(token)
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
        Log.d("DEV-LOG", "onErrorHandler: " + throwable.getMessage());
    }

}