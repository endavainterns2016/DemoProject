package endava.com.demoproject.helpers;


import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;

public class SharedPreferencesHelper {
    public static final String AUTH_DATA = "authData";
    private static final String USERNAME_PREF = "username";
    private static final String REMEMBER_USERNAME_PREF = "rememberUsername";
    public static SharedPreferencesHelper handler;
    @Inject
    Context context;
    private SharedPreferences authData;
    private SharedPreferencesHelperResponse helperResponse;

    public static SharedPreferencesHelper getInstance(SharedPreferencesHelperResponse response) throws Exception {
        if (handler == null) {
            handler = new SharedPreferencesHelper();
        }
        handler.helperResponse = response;
        handler.setContext();
        return handler;
    }

    public void setContext() {
        DemoProjectApplication.getApplicationComponent().inject(this);
    }

    public void rememberUserName(String username) {
        authData = context.getSharedPreferences(AUTH_DATA, 0);
        SharedPreferences.Editor editor = authData.edit();
        editor.putString(USERNAME_PREF, username);
        editor.putBoolean(REMEMBER_USERNAME_PREF, true);
        editor.apply();
    }

    public void forgetUserName() {
        SharedPreferences settings = context.getSharedPreferences(AUTH_DATA, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

    public void populateViewWithSharedPreferences() {
        authData = context.getSharedPreferences(AUTH_DATA, 0);
        helperResponse.populateView(authData.getString(USERNAME_PREF, ""), authData.getBoolean(REMEMBER_USERNAME_PREF, false));
    }
}
