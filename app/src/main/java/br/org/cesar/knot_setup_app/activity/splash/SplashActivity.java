package br.org.cesar.knot_setup_app.activity.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import br.org.cesar.knot_setup_app.KnotSetupApplication;
import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.activity.LoginActivity;
import br.org.cesar.knot_setup_app.activity.splash.SplashContract.ViewModel;
import br.org.cesar.knot_setup_app.activity.splash.SplashContract.Presenter;
import br.org.cesar.knot_setup_app.utils.Constants;
import br.org.cesar.knot_setup_app.wrapper.NetworkWrapper;
import br.org.cesar.knot_setup_app.activity.ThingActivity;

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
        intent.putExtra(Constants.GATEWAY_ID,getIntent().getStringExtra(Constants.GATEWAY_ID));
        startActivity(intent);
        finish();
    }

    @Override
    public void doListGateways(){
        Intent intent = new Intent(SplashActivity.this, ThingActivity.class);
        intent.putExtra("operation", Constants.CONFIGURE_THING_OPENTHREAD);
        startActivity(intent);
        finish();
    }

    @Override
    public void apiNotConfigured(){
        Toast.makeText(this,"The backend of the webUI needs to be configured.",Toast.LENGTH_LONG).show();
    }

    @Override
    public void callbackOnConnectionError(){
        Toast.makeText(this,"Network failure, " +
                "please try again later",Toast.LENGTH_LONG).show();
        finish();
    }

}
