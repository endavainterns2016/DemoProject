package endava.com.demoproject.view;

/**
 * Created by lbuzmacov on 20-04-16.
 */
public interface MainView extends MvpView {

    void initView();

    void setNavViewDetails(String username, String url);

    void logOutDialogShow();

    void navigateToLoginView();
}
