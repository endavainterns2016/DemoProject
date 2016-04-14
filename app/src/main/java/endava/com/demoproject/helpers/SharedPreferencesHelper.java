package endava.com.demoproject.helpers;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;

public class SharedPreferencesHelper {
    public static final String AUTH_DATA = "authData";
    private static final String USERNAME_PREF = "username";
    private static final String REMEMBER_USERNAME_PREF = "rememberUsername";
    public static SharedPreferencesHelper helper;
    private SharedPreferences defaultSharedPreferences;
    @Inject
    Context context;
    private SharedPreferences authData;
    private boolean enableAutoSync;
    private int autoSyncInterval;

    public static SharedPreferencesHelper getInstance() {
        if (helper == null) {
            helper = new SharedPreferencesHelper();
            helper.setContext();
        }
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

    public boolean getAutoSyncStatus(){
        enableAutoSync = getDefaultSharedPrefs().getBoolean("enable_auto_sync", false);
        return enableAutoSync;
    }

    public int getAutoSyncInterval() {
        autoSyncInterval = ((getDefaultSharedPrefs().getInt("auto_sync_number_picker_key", 1)) * 60 * 1000);
        return autoSyncInterval;
    }

    public SharedPreferences getDefaultSharedPrefs(){
        if (defaultSharedPreferences == null) {
            defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return defaultSharedPreferences;
    }
}
