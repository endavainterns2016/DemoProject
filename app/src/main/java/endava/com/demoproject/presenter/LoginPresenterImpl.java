package endava.com.demoproject.presenter;


import endava.com.demoproject.helpers.LoginHelper;
import endava.com.demoproject.helpers.LoginHelperResponse;
import endava.com.demoproject.view.LoginView;

public class LoginPresenterImpl implements LoginPresenter, LoginHelperResponse {
    private LoginHelper handler;
    private LoginView loginView;
    private String userName;
    private String password;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        handler = LoginHelper.getInstance(this);
    }

    @Override
    public boolean validateCredentials(String userName, String password) {
        loginView.showProgress();
        boolean result = true;
        if (userName.length() == 0) {
            loginView.setUsernameError();
            result = false;
        } else if (password.length() == 0) {
            loginView.setPasswordError();
            result = false;
        } else {
            this.userName = userName;
            this.password = password;
        }
        return result;
    }

    @Override
    public void doLogin() {
        handler.doLogin(userName, password);
    }

    @Override
    public void rememberUserName() {
        handler.rememberUserName(userName);
    }

    @Override
    public void forgetUserName() {
        handler.forgetUserName();
    }

    @Override
    public void setCredentialsError() {
        loginView.setCredentialsError();
    }

    @Override
    public void setConnectionError() {
        loginView.setConnectionError();
    }

    @Override
    public void hideProgress() {
        loginView.hideProgress();
    }

    @Override
    public void populateView(String username, boolean shouldSave) {
        loginView.populateView(username, shouldSave);
    }
    @Override
    public void getSharedPreferences() {
        handler.getSharedPreferences();
    }

    @Override
    public void onDestroy() {
        loginView = null;
        handler.unregisterReceiver();
    }
}
