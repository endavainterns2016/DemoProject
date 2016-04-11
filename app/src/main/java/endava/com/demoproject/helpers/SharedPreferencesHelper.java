package endava.com.demoproject.helpers;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    public static final String AUTH_DATA = "authData";
    private static final String USERNAME_PREF = "username";
    private static final String REMEMBER_USERNAME_PREF = "rememberUsername";
    public static SharedPreferencesHelper handler;
    private SharedPreferences authData;
    private Activity activity;
    private SharedPreferencesHelperResponse helperResponse;

    public static void initLoginRequestHandler(Activity activity) {
        handler = new SharedPreferencesHelper();
        handler.setActivity(activity);
    }

    public static SharedPreferencesHelper getInstance(SharedPreferencesHelperResponse response) throws Exception {
        if (handler == null) {
            throw new Exception("You should init your helper in your current activity before getting instance of it");
        }
        handler.helperResponse = response;
        return handler;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void rememberUserName(String username) {
        authData = activity.getSharedPreferences(AUTH_DATA, 0);
        SharedPreferences.Editor editor = authData.edit();
        editor.putString(USERNAME_PREF, username);
        editor.putBoolean(REMEMBER_USERNAME_PREF, true);
        editor.apply();
    }

    public void forgetUserName() {
        SharedPreferences settings = activity.getSharedPreferences(AUTH_DATA, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

    public void populateViewWithSharedPreferences() {
        authData = activity.getSharedPreferences(AUTH_DATA, 0);
        helperResponse.populateView(authData.getString(USERNAME_PREF, ""), authData.getBoolean(REMEMBER_USERNAME_PREF, false));
    }
}
