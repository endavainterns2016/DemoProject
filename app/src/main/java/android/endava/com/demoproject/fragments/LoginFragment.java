package android.endava.com.demoproject.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.endava.com.demoproject.R;
import android.endava.com.demoproject.SaveUserToDB;
import android.endava.com.demoproject.services.SaveUserToDBService;
import android.endava.com.demoproject.activities.LoginActivity;
import android.endava.com.demoproject.activities.MainActivity;
import android.endava.com.demoproject.model.Avatar;
import android.endava.com.demoproject.model.User;
import android.endava.com.demoproject.retrofit.ServiceFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {


    public static final String AUTH_DATA = "authData";
    private static final String USERNAME_PREF = "username";
    private static final String REMEMBER_USERNAME_PREF = "rememberUsername";
    private View v;
    private EditText mPasswordEdt;
    private EditText mUSerNameEdt;
    private Button mLoginBtn;
    private String credentials;
    private LoginOnClickListener loginOnClickListener;
    private Callback<List<User>> loginCallBack;
    private CheckBox usernameCheckBox;
    private String username;
    private String password;
    private BroadcastReceiver broadcastReceiver;
    private boolean rememberUsername;
    private String usernamePref;
    private SharedPreferences authData;
    private LoginActivity mActivity;
    private Callback<Avatar> avatarCallBack;
    private User user;
    private Snackbar connectionFailedSnackBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginActivity) {
            mActivity = (LoginActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login, container, false);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(broadcastReceiver);
        mActivity.stopService(new Intent(mActivity, SaveUserToDBService.class));
    }

    @Override
    public void onViewCreated(final View v, Bundle savedInstanceState) {
        authData = mActivity.getSharedPreferences(AUTH_DATA, 0);
        rememberUsername = authData.getBoolean(REMEMBER_USERNAME_PREF, false);
        usernamePref = authData.getString(USERNAME_PREF, "");
        loginOnClickListener = new LoginOnClickListener();

        IntentFilter filter = new IntentFilter();
        filter.addAction("user_was_saved_to_db");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent intentToMain = new Intent(mActivity, MainActivity.class);
                startActivity(intentToMain);
                mActivity.finish();
            }
        };
        mActivity.registerReceiver(broadcastReceiver, filter);
        usernameCheckBox = (CheckBox) v.findViewById(R.id.name_chbx);
        mPasswordEdt = (EditText) v.findViewById(R.id.password_edt);
        mUSerNameEdt = (EditText) v.findViewById(R.id.login_edt);
        mLoginBtn = (Button) v.findViewById(R.id.login_bnt);
        mLoginBtn.setOnClickListener(loginOnClickListener);

        checkPrefCredentials();

        loginCallBack = new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body() != null) {
                    saveToPrefCredentials();

                    user = response.body().get(0);
                    user.setUserName(username);
                    user.setHashedCredentials(credentials);

                    handleAvatarRequest();

                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, getString(R.string.credentials_error), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Snackbar snackbar = getSnackBar();
                snackbar.show();
            }
        };

        avatarCallBack = new Callback<Avatar>() {
            @Override
            public void onResponse(Call<Avatar> call, Response<Avatar> response) {
                if (response.body() != null) {
                    user.setAvatarUrl(response.body().getAvatarUrl());
                    SaveUserToDB command = new SaveUserToDB(user);
                    Intent intent = new Intent(mActivity, SaveUserToDBService.class);
                    intent.putExtra("command", command);
                    mActivity.startService(intent);
                } else {
                    Snackbar snackbar = getSnackBar();
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<Avatar> call, Throwable t) {
                Snackbar snackbar = getSnackBar();
                snackbar.show();
            }
        };

    }

    private Snackbar getSnackBar() {
        return Snackbar
                .make(v, getString(R.string.get_token_error), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.try_again), loginOnClickListener);
    }


    private void checkPrefCredentials() {
        if (rememberUsername && usernamePref.length() > 0) {
            mUSerNameEdt.append(usernamePref);
            usernameCheckBox.setChecked(rememberUsername);
        }
    }


    private void saveToPrefCredentials() {
        SharedPreferences.Editor editor = authData.edit();
        editor.putString(USERNAME_PREF, username);
        editor.putBoolean(REMEMBER_USERNAME_PREF, usernameCheckBox.isChecked());
        editor.apply();
    }

    private boolean credentialsAreFilled(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            Snackbar snackbar = Snackbar
                    .make(v, getString(R.string.fill_in_username), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Snackbar snackbar = Snackbar
                    .make(v, getString(R.string.fill_in_password), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;

        } else return true;
    }


    private void handleAvatarRequest() {
        ServiceFactory.getInstance().getUserAvatar("Basic " + user.getHashedCredentials()).enqueue(avatarCallBack);
    }

    private void handleLoginRequest(String username, String password) {
        try {
            credentials = android.util.Base64.encodeToString(
                    (username + ":" + password).getBytes("UTF-8"),
                    android.util.Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding ", e.toString());
        }
        ServiceFactory.getInstance().auth("Basic " + credentials).enqueue(loginCallBack);
    }

    public class LoginOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            username = mUSerNameEdt.getText().toString();
            password = mPasswordEdt.getText().toString();
            if (credentialsAreFilled(username, password))
                handleLoginRequest(username, password);
        }
    }
}
