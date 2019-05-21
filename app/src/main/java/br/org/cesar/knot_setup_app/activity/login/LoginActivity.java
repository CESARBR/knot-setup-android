package br.org.cesar.knot_setup_app.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.activity.login.LoginContract.Presenter;
import br.org.cesar.knot_setup_app.activity.thingList.ThingActivity;
import br.org.cesar.knot_setup_app.utils.Utils;

public class LoginActivity extends AppCompatActivity implements LoginContract.ViewModel {
        private Presenter mPresenter;

        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            mPresenter = new LoginPresenter(this,this);

            Button button = findViewById(R.id.send);
            EditText emailFld = findViewById(R.id.email);
            EditText passFld = findViewById(R.id.password_input);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.setEmail(emailFld.getText().toString());
                    mPresenter.doLogin(passFld.getText().toString());
                }
            });

            if(Utils.isConnected(this)){
                mPresenter.fillEmail();
            }
        }

        @Override
        public void callbackOnLogin(){
            Intent intent = new Intent(LoginActivity.this, ThingActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void fillEmailText(String email){
            EditText emailFld = findViewById(R.id.email);
            emailFld.setText(email);
        }

}
