package br.org.cesar.knot_setup_app.view.login;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import br.org.cesar.knot_setup_app.view.login.LoginContract.Presenter;
import br.org.cesar.knot_setup_app.view.login.LoginContract.ViewModel;
import br.org.cesar.knot_setup_app.data.DataManager;
import br.org.cesar.knot_setup_app.model.User;
import br.org.cesar.knot_setup_app.utils.Constants;
import br.org.cesar.knot_setup_app.wrapper.LogWrapper;
import br.org.cesar.knot_setup_app.wrapper.NetworkWrapper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements Presenter{

    private ViewModel mViewModel;
    private static DataManager dataManager;
    private String email;
    private String request,port,ip;
    private Context context;

    public void setEmail(String email){this.email = email;}

    LoginPresenter(ViewModel viewModel, Context context){
        this.mViewModel = viewModel;
        this.context = context;

        this.ip = dataManager.getInstance()
                .getPreference().getSharedPreferenceString(context, Constants.GATEWAY_IP);

        this.port =
                dataManager.getInstance().getPreference().getSharedPreferenceString(context, Constants.GATEWAY_PORT);

        this.request = "http://" + ip +":" + port +"/api/auth";

    }

    @Override
    public void onFocus() {
        if(NetworkWrapper.isConnected(context)) {
            searchEmail();
        }
    }

    @Override
    public void onSendClicked(String email, String pwd) {
        setEmail(email);
        doLogin(pwd);
    }

    private void searchEmail() {
        String email;
        email = dataManager.getInstance().getPersistentPreference()
                .getSharedPreferenceString(context, "email");
        mViewModel.fillEmail(email);
    }

    private void doLogin(String password){
        LogWrapper.Log("email: " + email + " password: " + password + " request: " + request, Log.DEBUG);
        dataManager.getInstance().getService().login(this.request,email,password)
        .timeout(30, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::loginSucceeded,
        this::onErrorHandler);
    }

    private void loginSucceeded(User user) {
        dataManager.getInstance().getPersistentPreference()
                .setSharedPreferenceString(context, "email",email);

        dataManager.getInstance().getPersistentPreference()
                .setSharedPreferenceString(context,"token","Bearer " + user.getToken());

        mViewModel.onLogin();
    }

    private void onErrorHandler(Throwable throwable){
        LogWrapper.Log("onErrorHandler: " + throwable.getMessage(), Log.DEBUG);
        if(throwable.getMessage().contains("401")){
            mViewModel.onInvalidCredentials();
        }
    }
}
