package br.org.cesar.knot_setup_app.activity.login;


import br.org.cesar.knot_setup_app.activity.login.LoginContract.ViewModel;

public class LoginPresenter implements LoginContract.Presenter{
    private ViewModel mViewModel;

    LoginPresenter(ViewModel viewModel){
        this.mViewModel = viewModel;
    }

    public void doLogin(){

    }

}
