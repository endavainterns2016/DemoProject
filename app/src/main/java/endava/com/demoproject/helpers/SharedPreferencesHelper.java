package endava.com.demoproject.helpers;


import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;

public class SharedPreferencesHelper {
    public static final String AUTH_DATA = "authData";
    private static final String USERNAME_PREF = "username";
    private static final String REMEMBER_USERNAME_PREF = "rememberUsername";
    public static SharedPreferencesHelper helper;
    @Inject
    Context context;
    private SharedPreferences authData;

    public static SharedPreferencesHelper getInstance() {
        if (helper == null) {
            helper = new SharedPreferencesHelper();
        }
        helper.setContext();
        return helper;
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

    public String getUserName() {
        authData = context.getSharedPreferences(AUTH_DATA, 0);
        return authData.getString(USERNAME_PREF, "");
    }
}
