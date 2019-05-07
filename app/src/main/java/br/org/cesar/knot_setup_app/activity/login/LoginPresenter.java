package br.org.cesar.knot_setup_app.activity.login;


import android.database.Cursor;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import br.org.cesar.knot_setup_app.activity.login.LoginContract.ViewModel;
import br.org.cesar.knot_setup_app.data.DataManager;
import br.org.cesar.knot_setup_app.model.User;
import br.org.cesar.knot_setup_app.persistence.mysqlDatabase.DBHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter{

    private ViewModel mViewModel;
    private DBHelper dbHelper;
    private static DataManager dataManager;
    private String email;

    public void setEmail(String email){this.email = email;}

    LoginPresenter(ViewModel viewModel, DBHelper dbHelper){
        this.mViewModel = viewModel;
        this.dbHelper = dbHelper;
    }

    @Override
    public void fillEmail(){
        Cursor cursor = this.dbHelper.getAccount();
        //try post request
        if(cursor.moveToFirst()){
            mViewModel.fillEmailText(cursor.getString(cursor.getColumnIndex("login")));
        }
    }

    @Override
    public void doLogin(String password){
        Log.d("DEV-LOG","email: " + email + " password: " + password);
        dataManager.getInstance().getService().login(email,password)
        .timeout(30, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::loginSucceeded,
        this::onErrorHandler);
    }

    private void loginSucceeded(User user) {
        Log.d("DEV-LOG",user.getToken());
        dbHelper.insertAccount(email, user.getToken());
        mViewModel.callbackOnLogin();
    }

    private void onErrorHandler(Throwable throwable){
        Log.d("DEV-LOG", "onErrorHandler: " + throwable.getMessage());
    }
}
