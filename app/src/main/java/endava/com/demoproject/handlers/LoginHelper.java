package endava.com.demoproject.handlers;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.List;

import endava.com.demoproject.SaveUserToDB;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.model.Avatar;
import endava.com.demoproject.model.User;
import endava.com.demoproject.retrofit.ServiceFactory;
import endava.com.demoproject.services.SaveUserToDBService;
import endava.com.demoproject.view.LoginView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginHelper implements Callback<List<User>> {
    public static final String AUTH_DATA = "authData";
    private static final String USERNAME_PREF = "username";
    private static final String REMEMBER_USERNAME_PREF = "rememberUsername";
    public static LoginHelper handler;
    private Activity activity;
    private LoginView loginView;
    private String credentials;
    private Callback<Avatar> avatarCallback;
    private User user;
    private BroadcastReceiver broadcastReceiver;
    private SharedPreferences authData;

    public static void initLoginRequestHandler(Activity activity) {
        handler = new LoginHelper();
        handler.setActivity(activity);
    }

    public static LoginHelper getInstance(LoginView loginView) {
        if (handler == null) {
            try {
                throw new Exception("You should init your handler in your current activity before getting instance of it");
            } catch (Exception e) {
                Log.e("Custom exeption", e.toString());
            }
        }
        handler.setLoginView(loginView);
        return handler;
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void doLogin(String username, String password) {
        try {
            credentials = android.util.Base64.encodeToString(
                    (username + ":" + password).getBytes("UTF-8"),
                    android.util.Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding ", e.toString());
        }

        ServiceFactory.getInstance().auth("Basic " + credentials).enqueue(this);
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

    @Override
    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
        if (response.body() != null) {
            user = response.body().get(0);
            user.setHashedCredentials(credentials);
            initAvatarCallBack();
            initBroadcastReceiver();
            ServiceFactory.getInstance().getUserAvatar("Basic " + user.getHashedCredentials()).enqueue(avatarCallback);
        } else {
            loginView.setCredentialsError();
        }
    }

    @Override
    public void onFailure(Call<List<User>> call, Throwable t) {
        loginView.setConnectionError();
    }

    public void initAvatarCallBack() {
        avatarCallback = new Callback<Avatar>() {
            @Override
            public void onResponse(Call<Avatar> call, Response<Avatar> response) {
                user.setAvatarUrl(response.body().getAvatarUrl());
                SaveUserToDB command = new SaveUserToDB(user);
                Intent intent = new Intent(activity, SaveUserToDBService.class);
                intent.putExtra("command", command);
                activity.startService(intent);
            }

            @Override
            public void onFailure(Call<Avatar> call, Throwable t) {
                loginView.setConnectionError();
            }
        };
    }

    public void initBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("user_was_saved_to_db");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent intentToMain = new Intent(activity, MainActivity.class);
                loginView.hideProgress();
                activity.startActivity(intentToMain);
                activity.finish();
            }
        };
        activity.registerReceiver(broadcastReceiver, filter);
    }

    public void unregisterReceiver() {
        if (broadcastReceiver != null) {
            activity.unregisterReceiver(broadcastReceiver);
        }
    }

    public void populateView() {
        authData = activity.getSharedPreferences(AUTH_DATA, 0);
        loginView.populateView(authData.getString(USERNAME_PREF, ""), authData.getBoolean(REMEMBER_USERNAME_PREF, false));
    }
}
