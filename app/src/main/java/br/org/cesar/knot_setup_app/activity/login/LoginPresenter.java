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

    private void getOpenthreadConfig(){
        String bearer,ip;
        bearer = dataManager.getInstance()
                .getPersistentPreference()
                .getSharedPreferenceString(context,"token");

        ip = dataManager.getInstance()
                .getPreference()
                .getSharedPreferenceString(context,"ip");

        this.port = dataManager.getInstance()
                .getPreference().getSharedPreferenceString(context,"port");

        this.request = "http://" + ip + ":" + port +"/api/radio/openthread";

        dataManager.getInstance().getService().getOpenthreadConfig(this.request,bearer)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getOpenThreadSucceeded,
                        this::onErrorHandler);

    }

    private void getOpenThreadSucceeded(Openthread openthread){

        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"netname",openthread.getNetworkName());

        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"channel",openthread.getChannel());

        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"xpanid",openthread.getXpanId());

        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"panid", openthread.getPanId());

        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"ipv6",openthread.getMeshIpv6());

        dataManager.getInstance().getPreference()
                .setSharedPreferenceString(context,"masterkey",openthread.getMasterKey());

        mViewModel.callbackOnLogin();
    }



    private void loginSucceeded(User user) {
        dataManager.getInstance().getPersistentPreference()
                .setSharedPreferenceString(context,"email",email);

        dataManager.getInstance().getPersistentPreference()
                .setSharedPreferenceString(context,"token","Bearer " + user.getToken());

        getOpenthreadConfig();
    }

    private void onErrorHandler(Throwable throwable){
        Log.d("DEV-LOG", "onErrorHandler: " + throwable.getMessage());
    }
}
