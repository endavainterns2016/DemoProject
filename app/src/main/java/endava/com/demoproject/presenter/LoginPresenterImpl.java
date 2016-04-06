package endava.com.demoproject.presenter;


import endava.com.demoproject.view.LoginView;

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView loginView;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }


    @Override
    public void doLogin(String username, String password) {

    }

    @Override
    public void onDestroy() {
        loginView = null;
    }
}
