package br.org.cesar.knot_setup_app.activity.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.activity.login.LoginContract.Presenter;

public class LoginActivity extends AppCompatActivity implements LoginContract.ViewModel {
        private Presenter mPresenter;

        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            mPresenter = new LoginPresenter(this);
        }

}
