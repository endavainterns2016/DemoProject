package endava.com.demoproject.presenter;

public interface LoginPresenter {

    void populateView();

    boolean validateCredentials(String username, String password);

    void doLogin();

    void rememberUserName();

    void forgetUserName();

    void onDestroy();
}
