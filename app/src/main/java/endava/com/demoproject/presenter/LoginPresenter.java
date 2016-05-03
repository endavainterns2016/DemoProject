package endava.com.demoproject.presenter;


import android.content.res.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import endava.com.demoproject.R;
import endava.com.demoproject.cacheableObserver.Event;
import endava.com.demoproject.cacheableObserver.EventContext;
import endava.com.demoproject.cacheableObserver.EventHandler;
import endava.com.demoproject.cacheableObserver.Observer;
import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.helpers.LoginHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;
import endava.com.demoproject.view.LoginView;

public class LoginPresenter extends BasePresenter<LoginView> implements Observer {

    private LoginHelper loginHelper;
    private SharedPreferencesHelper sharedPrefHelper;
    private Resources resources;
    private Subject subject;
    private LoginView loginView;
    private String userName;
    private HashMap<String, EventHandler> eventHandlerMap = new HashMap<>();
    private List<EventContext> eventContextList;

    @Inject
    public LoginPresenter(Resources resources, SharedPreferencesHelper sharedPrefHelper, LoginHelper loginHelper, Subject subject) {
        this.resources = resources;
        this.sharedPrefHelper = sharedPrefHelper;
        this.loginHelper = loginHelper;
        this.subject = subject;
    }

    public boolean validateCredentials(String userName, String password) {
        loginView.requestStarted();
        boolean result = true;
        if (userName.length() == 0) {
            loginView.showError(resources.getString(R.string.fill_in_username));
            result = false;
        } else if (password.length() == 0) {
            loginView.showError(resources.getString(R.string.fill_in_password));
            result = false;
        } else {
            this.userName = userName;
        }
        return result;
    }

    public void doLogin(String userName, String password, boolean shouldSaveUser) {
        if (validateCredentials(userName, password)) {
            if (shouldSaveUser) {
                rememberUserName();
            } else {
                forgetUserName();
            }
            loginHelper.doLogin(userName, password);
        }
    }

    public void rememberUserName() {
        sharedPrefHelper.rememberUserName(userName);
    }

    public void forgetUserName() {
        sharedPrefHelper.forgetUserName();
    }

    public void populateView() {
        String username = sharedPrefHelper.getUserName();
        loginView.populateView(username, username.length() != 0);
    }

    public void onDestroy() {
        detachView();
    }

    @Override
    public void onEvent(Event e) {
        if (eventContextList.contains(e.getEventKey())) {
            eventHandlerMap.get(e.getEventKey().getEventKey()).handleEvent();
        }
    }

    @Override
    public List<EventContext> getObserverKeys() {
        eventContextList = new ArrayList<>();
        EventContext successfulLogin = new EventContext(resources.getString(R.string.successful_login_tag), null);
        EventContext connectionError = new EventContext(resources.getString(R.string.connection_error_tag), null);
        EventContext credentialsError = new EventContext(resources.getString(R.string.credential_error_tag), null);
        eventContextList.add(successfulLogin);
        eventContextList.add(connectionError);
        eventContextList.add(credentialsError);
        return eventContextList;
    }

    @Override
    public boolean isMainObserverForKey(EventContext key) {
        if (eventContextList.contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void attachView(LoginView mvpView) {
        super.attachView(mvpView);
        loginView = mvpView;
        populateView();
        populateEventsMap();
    }

    @Override
    public void onResume() {
        subject.registerObserver(this);
    }

    @Override
    public void onPause() {
        subject.unregisterObservers(this);
    }

    private void populateEventsMap() {
        eventHandlerMap.put(resources.getString(R.string.successful_login_tag), new SuccessfulLoginHandler());
        eventHandlerMap.put(resources.getString(R.string.connection_error_tag), new ConnectionErrorHandler());
        eventHandlerMap.put(resources.getString(R.string.credential_error_tag), new CredentialsErrorHandler());
    }

    private class SuccessfulLoginHandler implements EventHandler {

        @Override
        public void handleEvent() {
            loginView.finishLogin();
        }
    }

    private class ConnectionErrorHandler implements EventHandler {

        @Override
        public void handleEvent() {
            loginView.showConnectionError();
        }
    }

    private class CredentialsErrorHandler implements EventHandler {

        @Override
        public void handleEvent() {
            loginView.showError(resources.getString(R.string.credentials_error_message));
        }
    }
}
