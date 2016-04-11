package endava.com.demoproject.presenter;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import endava.com.demoproject.R;
import endava.com.demoproject.cacheableObserver.Event;
import endava.com.demoproject.cacheableObserver.EventContext;
import endava.com.demoproject.cacheableObserver.Observer;
import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.helpers.LoginHelper;
import endava.com.demoproject.helpers.LoginHelperResponse;
import endava.com.demoproject.helpers.ResourceHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelperResponse;
import endava.com.demoproject.view.LoginView;

public class LoginPresenter implements LoginHelperResponse, SharedPreferencesHelperResponse, Observer {
    private LoginHelper loginHelper;
    private SharedPreferencesHelper sharedPrefHelper;
    private Subject subject = Subject.newInstance();
    private LoginView loginView;
    private String userName;
    private String password;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        try {
            sharedPrefHelper = SharedPreferencesHelper.getInstance(this);
            loginHelper = LoginHelper.getInstance(this);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

    public boolean validateCredentials(String userName, String password) {
        loginView.showProgress();
        boolean result = true;
        if (userName.length() == 0) {
            loginView.setError(ResourceHelper.getResources().getString(R.string.fill_in_username));
            result = false;
        } else if (password.length() == 0) {
            loginView.setError(ResourceHelper.getResources().getString(R.string.fill_in_password));
            result = false;
        } else {
            this.userName = userName;
            this.password = password;
        }
        return result;
    }

    public void registerObserver() {
        subject.registerObserver(this);
    }

    public void doLogin() {
        loginHelper.doLogin(userName, password);
    }

    public void rememberUserName() {
        sharedPrefHelper.rememberUserName(userName);
    }

    public void forgetUserName() {
        sharedPrefHelper.forgetUserName();
    }

    @Override
    public void setError(String error) {
        loginView.setError(error);
    }

    public void setConnectionError() {
        loginView.setConnectionError();
    }

    public void populateView(String username, boolean shouldSave) {
        loginView.populateView(username, shouldSave);
    }

    public void getSharedPreferences() {
        sharedPrefHelper.populateViewWithSharedPreferences();
    }

    public void onDestroy() {
        loginView = null;
        subject.unregisterObservers(this);
    }

    @Override
    public void onEvent(Event e) {
        subject.unregisterObservers(this);
        loginView.startMainActivity();
    }

    @Override
    public List<EventContext> getObserverKeys() {
        EventContext userWasSavedEvent = new EventContext("user_was_saved", null);
        List<EventContext> list = new ArrayList<>();
        list.add(userWasSavedEvent);
        return list;
    }

    @Override
    public boolean isMainObserverForKey(EventContext key) {
        return false;
    }
}
