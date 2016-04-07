package endava.com.demoproject.view;


public interface LoginView {

    void populateView(String userName,boolean shouldSave);

    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void setCredentialsError();

    void setConnectionError();
}
