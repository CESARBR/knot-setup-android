package br.org.cesar.knot_setup_app.view.login;

public interface LoginContract {

    interface ViewModel {
        void fillEmail(String email);
        void onInvalidCredentials();
        void onLogin();
    }

    interface Presenter {
        void onResume();
        void onSendClicked(String login, String pwd);
    }
}
