package endava.com.demoproject.presenter;

public interface LoginPresenter {

    void doLogin(String username, String password);

    void onDestroy();
}
