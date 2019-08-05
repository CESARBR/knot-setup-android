package br.org.cesar.knot_setup_app.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.activity.ThingActivity;
import br.org.cesar.knot_setup_app.fragment.login.LoginContract.ViewModel;
import br.org.cesar.knot_setup_app.fragment.login.LoginContract.Presenter;
import br.org.cesar.knot_setup_app.wrapper.NetworkWrapper;


public class LoginFragment extends Fragment implements ViewModel {

    private EditText emailFld;
    private EditText pwdFld;
    private Presenter presenter;
    private Handler mHandler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailFld = view.findViewById(R.id.email);
        pwdFld =  view.findViewById(R.id.password_input);

        presenter = new LoginPresenter(this,getActivity());
        mHandler = new Handler(Looper.getMainLooper());

        Button button = view.findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSendClicked(emailFld.getText().toString(),pwdFld.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.onResume();
    }

    @Override
    public void onLogin() {
        Intent intent = new Intent(getActivity(), ThingActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void fillEmail(String email) {
        emailFld.setText(email);
    }

    @Override
    public void onInvalidCredentials() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),"Incorrect username or password."
                        ,Toast.LENGTH_LONG).show();
            }
        });
    }

}
