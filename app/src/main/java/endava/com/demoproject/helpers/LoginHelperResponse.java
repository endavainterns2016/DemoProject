package endava.com.demoproject.helpers;


public interface LoginHelperResponse {

    void setCredentialsError();

    void setConnectionError();

    void hideProgress();

    void populateView(String username, boolean shouldSave);


}
