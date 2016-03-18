package android.endava.com.demoproject;


import android.content.SharedPreferences;
import android.endava.com.demoproject.model.User;
import android.endava.com.demoproject.retrofit.ServiceFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private Toolbar mToolbar;
    private EditText mPasswordEdt;
    private EditText mUSerNameEdt;
    private Button mLoginBtn;
    private String credentials;
    private LoginOnClickListener loginOnClickListener;
    Callback<List<User>> loginCallBack;
    private CheckBox passwordCheckBox;
    private CheckBox usernameCheckBox;
    public static final String AUTH_DATA = "authData";
    private SharedPreferences authData;
    private boolean rememberPassword;
    private boolean rememberUsername;
    private String usernamePref;
    private String passwordPref;
    private String username;
    private String password;
    private static final String REMEMBER_PASSWORD_PREF = "rememberPassword";
    private static final String REMEMBER_USERNAME_PREF = "rememberUsername";
    private static final String PASSWORD_PREF = "password";
    private static final String USERNAME_PREF = "username";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authData = getActivity().getSharedPreferences(AUTH_DATA, 0);
        rememberPassword = authData.getBoolean(REMEMBER_PASSWORD_PREF, false);
        rememberUsername = authData.getBoolean(REMEMBER_USERNAME_PREF, false);
        usernamePref = authData.getString(USERNAME_PREF, "");
        passwordPref = authData.getString(PASSWORD_PREF, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        loginOnClickListener = new LoginOnClickListener();

        passwordCheckBox = (CheckBox) v.findViewById(R.id.password_chbx);
        usernameCheckBox = (CheckBox) v.findViewById(R.id.name_chbx);
        mPasswordEdt = (EditText) v.findViewById(R.id.password_edt);
        mUSerNameEdt = (EditText) v.findViewById(R.id.login_edt);
        mLoginBtn = (Button) v.findViewById(R.id.login_bnt);
        mToolbar = (Toolbar) v.findViewById(R.id.fragment_login_toolbar);
        mToolbar.setTitle(R.string.app_name);

        checkPrefCredentials();

        loginCallBack = new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body() != null) {
                    Toast.makeText(getActivity(), response.body().get(0).getToken(),
                            Toast.LENGTH_LONG).show();
                    saveToPrefCredentials();

                } else {
                    Toast.makeText(getActivity(), getString(R.string.credentials_error),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.get_token_error),
                        Toast.LENGTH_LONG).show();
            }
        };

        mLoginBtn.setOnClickListener(loginOnClickListener);
    }

    private void checkPrefCredentials() {
        if (rememberUsername && usernamePref.length() > 0) {
            mUSerNameEdt.append(usernamePref);
            usernameCheckBox.setChecked(rememberUsername);
        }

        if (rememberPassword && passwordPref.length() > 0) {
            mPasswordEdt.append(passwordPref);
            passwordCheckBox.setChecked(rememberPassword);
        }
    }

    private void saveToPrefCredentials() {
        SharedPreferences.Editor editor = authData.edit();
        editor.putString(USERNAME_PREF, username);
        editor.putString(PASSWORD_PREF, password);
        editor.putBoolean(REMEMBER_USERNAME_PREF, usernameCheckBox.isChecked());
        editor.putBoolean(REMEMBER_PASSWORD_PREF, passwordCheckBox.isChecked());
        editor.commit();
    }

    private boolean credentialsAreFilled() {
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getActivity(), getString(R.string.fill_in_username),
                    Toast.LENGTH_SHORT).show();
            return false;

        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), getString(R.string.fill_in_password),
                    Toast.LENGTH_SHORT).show();
            return false;

        } else return true;
    }

    private void handleLoginRequest() {
        try {
            credentials = android.util.Base64.encodeToString(
                    (username + ":" + password).getBytes("UTF-8"),
                    android.util.Base64.NO_WRAP
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ServiceFactory.getInstance().auth("Basic " + credentials).enqueue(loginCallBack);

    }

    public class LoginOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            username = mUSerNameEdt.getText().toString();
            password = mPasswordEdt.getText().toString();
            if (credentialsAreFilled())
                handleLoginRequest();
        }
    }
}
