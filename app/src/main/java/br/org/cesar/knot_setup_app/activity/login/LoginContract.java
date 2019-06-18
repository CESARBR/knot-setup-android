package br.org.cesar.knot_setup_app.activity.login;

import br.org.cesar.knot_setup_app.model.State;

public interface LoginContract {

    interface ViewModel{
        void fillEmailText(String email);
        void loginFailed();
        void callbackOnLogin();
    }
    interface Presenter{
        void fillEmail();
        void doLogin(String password);
        void setEmail(String s);
    }

}

