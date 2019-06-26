package br.org.cesar.knot_setup_app.activity.login;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import br.org.cesar.knot_setup_app.activity.login.LoginContract.ViewModel;
import br.org.cesar.knot_setup_app.data.DataManager;
import br.org.cesar.knot_setup_app.model.Openthread;
import br.org.cesar.knot_setup_app.model.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter{

    private ViewModel mViewModel;
    private static DataManager dataManager;
    private String email;
    private String request,port;
    private Context context;

    public void setEmail(String email){this.email = email;}

    LoginPresenter(ViewModel viewModel, Context context){
        this.mViewModel = viewModel;
        this.context = context;
        String ip = dataManager.getInstance()
                .getPreference().getSharedPreferenceString(context,"ip");

        this.port = dataManager.getInstance()
                    .getPreference().getSharedPreferenceString(context,"port");

        this.request = "http://" + ip +":" + port +"/api/auth";

    }

    @Override
    public void fillEmail(){
    }

    @Override
    public void doLogin(String password){
        Log.d("DEV-LOG","email: " + email + " password: " + password + " request: " + request);
        dataManager.getInstance().getService().login(this.request,email,password)
        .timeout(30, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::loginSucceeded,
        this::onErrorHandler);
    }

    private void loginSucceeded(User user) {
        dataManager.getInstance().getPersistentPreference()
                .setSharedPreferenceString(context,"email",email);

        dataManager.getInstance().getPersistentPreference()
                .setSharedPreferenceString(context,"token","Bearer " + user.getToken());

        mViewModel.callbackOnLogin();
    }

    private void onErrorHandler(Throwable throwable){
        Log.d("DEV-LOG", "onErrorHandler: " + throwable.getMessage());
        if(throwable.getMessage().contains("401")){
            mViewModel.loginFailed();
        }
    }
}
