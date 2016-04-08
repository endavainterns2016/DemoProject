package endava.com.demoproject.presenter;


import endava.com.demoproject.helpers.LoginHelper;
import endava.com.demoproject.helpers.LoginHelperResponse;
import endava.com.demoproject.view.LoginView;

public class LoginPresenter implements LoginHelperResponse {
    private LoginHelper handler;
    private LoginView loginView;
    private String userName;
    private String password;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        handler = LoginHelper.getInstance(this);
    }

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


    public void doLogin() {
        handler.doLogin(userName, password);
    }

    public void rememberUserName() {
        handler.rememberUserName(userName);
    }

    public void forgetUserName() {
        handler.forgetUserName();
    }

    public void setCredentialsError() {
        loginView.setCredentialsError();
    }

    public void setConnectionError() {
        loginView.setConnectionError();
    }


    public void hideProgress() {
        loginView.hideProgress();
    }

    public void populateView(String username, boolean shouldSave) {
        loginView.populateView(username, shouldSave);
    }

    public void getSharedPreferences() {
        handler.getSharedPreferences();
    }

    public void onDestroy() {
        loginView = null;
        handler.unregisterReceiver();
    }
}
