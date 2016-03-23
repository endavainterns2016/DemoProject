package android.endava.com.demoproject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.endava.com.demoproject.db.DataBaseHelper;
import android.endava.com.demoproject.db.HelperFactory;
import android.endava.com.demoproject.model.User;
import android.endava.com.demoproject.retrofit.ServiceFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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


    private EditText mPasswordEdt;
    private EditText mUSerNameEdt;
    private Button mLoginBtn;
    private String credentials;
    private LoginOnClickListener loginOnClickListener;
    private Callback<List<User>> loginCallBack;
    private DataBaseHelper dbHelper;
    private CheckBox usernameCheckBox;
    private String username;
    private String password;
    private BroadcastReceiver broadcastReceiver;
    private boolean rememberUsername;
    private String usernamePref;
    private static final String USERNAME_PREF = "username";
    private static final String REMEMBER_USERNAME_PREF = "rememberUsername";
    public static final String AUTH_DATA = "authData";
    private SharedPreferences authData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().stopService(new Intent(getActivity(), SaveUserToDBService.class));
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        authData = getActivity().getSharedPreferences(AUTH_DATA, 0);
        rememberUsername = authData.getBoolean(REMEMBER_USERNAME_PREF, false);
        usernamePref = authData.getString(USERNAME_PREF, "");
        dbHelper = HelperFactory.getHelper();
        loginOnClickListener = new LoginOnClickListener();

        IntentFilter filter = new IntentFilter();
        filter.addAction("user_was_saved_to_db");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent intentToMain = new Intent(getActivity(), MainActivity.class);
                startActivity(intentToMain);
                getActivity().finish();
            }
        };
        getActivity().registerReceiver(broadcastReceiver, filter);

        usernameCheckBox = (CheckBox) v.findViewById(R.id.name_chbx);
        mPasswordEdt = (EditText) v.findViewById(R.id.password_edt);
        mUSerNameEdt = (EditText) v.findViewById(R.id.login_edt);
        mLoginBtn = (Button) v.findViewById(R.id.login_bnt);
        checkPrefCredentials();


        loginCallBack = new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body() != null) {
                    saveToPrefCredentials();

                    User user = response.body().get(0);
                    user.setUserName(username);
                    user.setHashedCredentials(credentials);

                    SaveUserToDB command = new SaveUserToDB(user);
                    Intent intent = new Intent(getActivity(), SaveUserToDBService.class);
                    intent.putExtra("command", command);
                    getActivity().startService(intent);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.credentials_error), Toast.LENGTH_LONG).show();
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
    }


    private void saveToPrefCredentials() {
        SharedPreferences.Editor editor = authData.edit();
        editor.putString(USERNAME_PREF, username);
        editor.putBoolean(REMEMBER_USERNAME_PREF, usernameCheckBox.isChecked());
        editor.commit();
    }

    private boolean credentialsAreFilled(String username, String password) {
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
