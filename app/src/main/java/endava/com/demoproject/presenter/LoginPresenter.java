package endava.com.demoproject.presenter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import endava.com.demoproject.R;
import endava.com.demoproject.cacheableObserver.Event;
import endava.com.demoproject.cacheableObserver.EventContext;
import endava.com.demoproject.cacheableObserver.EventHandler;
import endava.com.demoproject.cacheableObserver.Observer;
import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.helpers.LoginHelper;
import endava.com.demoproject.helpers.ResourcesHelper;
import endava.com.demoproject.helpers.SharedPreferencesHelper;
import endava.com.demoproject.view.LoginView;

public class LoginPresenter extends BasePresenter<LoginView> implements Observer {

    private LoginHelper loginHelper;
    private SharedPreferencesHelper sharedPrefHelper;
    private ResourcesHelper resourcesHelper;
    private Subject subject;
    private LoginView loginView;
    private String userName;
    private HashMap<String, EventHandler> eventHandlerMap = new HashMap<>();
    private List<EventContext> eventContextList;


    public LoginPresenter(LoginView loginView,ResourcesHelper resourcesHelper, SharedPreferencesHelper sharedPrefHelper,LoginHelper loginHelper,Subject subject) {
        this.loginView = loginView;
        this.resourcesHelper = resourcesHelper;
        this.sharedPrefHelper = sharedPrefHelper;
        this.loginHelper = loginHelper;
        this.subject = subject;
    }

    public boolean validateCredentials(String userName, String password) {
        loginView.requestStarted();
        boolean result = true;
        if (userName.length() == 0) {
            loginView.showError(resourcesHelper.provideResources().getString(R.string.fill_in_username));
            result = false;
        } else if (password.length() == 0) {
            loginView.showError(resourcesHelper.provideResources().getString(R.string.fill_in_password));
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
        if(eventContextList.contains(e.getEventKey())){
            eventHandlerMap.get(e.getEventKey().getEventKey()).handleEvent();
        }
    }

    @Override
    public List<EventContext> getObserverKeys() {
        eventContextList = new ArrayList<>();
        EventContext successfulLogin = new EventContext(resourcesHelper.provideResources().getString(R.string.successful_login_tag), null);
        EventContext connectionError = new EventContext(resourcesHelper.provideResources().getString(R.string.connection_error_tag), null);
        EventContext credentialsError = new EventContext(resourcesHelper.provideResources().getString(R.string.credential_error_tag), null);
        eventContextList.add(successfulLogin);
        eventContextList.add(connectionError);
        eventContextList.add(credentialsError);
        return eventContextList;
    }

    @Override
    public boolean isMainObserverForKey(EventContext key) {
        if(eventContextList.contains(key)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void attachView(LoginView mvpView) {
        super.attachView(mvpView);
        populateView();
        subject.registerObserver(this);
        populateEventsMap();
    }

    @Override
    public void detachView() {
        super.detachView();
        subject.unregisterObservers(this);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

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
            loginView.showError(resourcesHelper.provideResources().getString(R.string.credentials_error_message));
        }
    }

    private void populateEventsMap() {
        eventHandlerMap.put(resourcesHelper.provideResources().getString(R.string.successful_login_tag), new SuccessfulLoginHandler());
        eventHandlerMap.put(resourcesHelper.provideResources().getString(R.string.connection_error_tag), new ConnectionErrorHandler());
        eventHandlerMap.put(resourcesHelper.provideResources().getString(R.string.credential_error_tag), new CredentialsErrorHandler());
    }
}
