package br.org.cesar.knot_setup_app.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import br.org.cesar.knot_setup_app.KnotSetupApplication;
import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.activity.login.LoginActivity;
import br.org.cesar.knot_setup_app.activity.splash.SplashContract.ViewModel;
import br.org.cesar.knot_setup_app.activity.splash.SplashContract.Presenter;
import br.org.cesar.knot_setup_app.activity.thingList.ThingActivity;
import br.org.cesar.knot_setup_app.wrapper.NetworkWrapper;

public class SplashActivity extends AppCompatActivity implements  ViewModel{

    private Presenter mPresenter;
    private NetworkWrapper networkWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        networkWrapper = KnotSetupApplication.getNetworkWrapper();
        networkWrapper.forceSocketsThroughWifi();

        mPresenter = new SplashPresenter(this,this);

        if(NetworkWrapper.isConnected(this)){
            mPresenter.chooseActivity();
        }
    }

    @Override
    public void doLogin(){
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void doListGateways(){
        Intent intent = new Intent(SplashActivity.this, ThingActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void apiNotConfigured(){
        Toast.makeText(this,"The backend of the webUI needs to be configured.",Toast.LENGTH_LONG).show();
    }

}
