package endava.com.demoproject.view;


public interface LoginView extends MvpView {

    void populateView(String userName, boolean shouldSave);

    void requestStarted();

    void requestFinished();

    void showError(String error);

    void finishLogin();

    void showConnectionError();
}
