package br.org.cesar.knot_setup_app.fragment.login;

public interface LoginContract {

    interface ViewModel {
        void fillEmail(String email);
        void onInvalidCredentials();
        void onLogin();
    }

    interface Presenter {
        void onFocus();
        void onSendClicked(String login, String pwd);
    }
}
