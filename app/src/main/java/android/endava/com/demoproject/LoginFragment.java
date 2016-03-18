package android.endava.com.demoproject;


import android.content.SharedPreferences;
import android.endava.com.demoproject.Model.AuthResponse;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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
    private EditText mUserNameEdt;
    private Button mLoginBtn;
    private String credentials;
    private CheckBox passwordCheckBox;
    private CheckBox usernameCheckBox;
    public static final String AUTH_DATA = "authData";
    private SharedPreferences authData;
    private boolean rememberPassword;
    private boolean rememberUsername;
    private String username;
    private String password;
    private static final String REMEMBER_PASSWORD_PREF = "rememberPassword";
    private static final String REMEMBER_USERNAME_PREF = "rememberUsername";
    private static final String PASSWORD_PREF = "password";
    private static final String USERNAME_PREF = "username";

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authData = getActivity().getSharedPreferences(AUTH_DATA, 0);
        rememberPassword = authData.getBoolean(REMEMBER_PASSWORD_PREF, false);
        rememberUsername = authData.getBoolean(REMEMBER_USERNAME_PREF, false);
        username = authData.getString(USERNAME_PREF, "");
        password = authData.getString(PASSWORD_PREF, "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        passwordCheckBox = (CheckBox) v.findViewById(R.id.password_chbx);
        usernameCheckBox = (CheckBox) v.findViewById(R.id.name_chbx);
        mPasswordEdt = (EditText) v.findViewById(R.id.password_edt);
        mUserNameEdt = (EditText) v.findViewById(R.id.login_edt);
        mLoginBtn = (Button) v.findViewById(R.id.login_bnt);
        mToolbar = (Toolbar) v.findViewById(R.id.fragment_login_toolbar);
        mToolbar.setTitle(R.string.app_name);

        if (rememberUsername && username.length() > 0) {
            mUserNameEdt.append(username);
            usernameCheckBox.setChecked(rememberUsername);
        }

        if (rememberPassword && password.length() > 0) {
            mPasswordEdt.append(password);
            passwordCheckBox.setChecked(rememberPassword);
        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = mUserNameEdt.getText().toString();

                if (userName.isEmpty()) {
                    Toast.makeText(getActivity(), "user name should not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = mPasswordEdt.getText().toString();

                if (password.isEmpty()) {
                    Toast.makeText(getActivity(), "password should not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(userName, password);
            }
        });

        return v;
    }

    void loginUser(String username, String password) {
        try {
            credentials = android.util.Base64.encodeToString(
                    (username + ":" + password).getBytes("UTF-8"),
                    android.util.Base64.NO_WRAP
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        DemoProjectAPI.Factory.getInstance().auth("Basic " + credentials)
                .enqueue(new Callback<List<AuthResponse>>() {
                    @Override
                    public void onResponse(Call<List<AuthResponse>> call, Response<List<AuthResponse>> response) {

                        Toast.makeText(getActivity(), response.body().get(0).getToken(),
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<List<AuthResponse>> call, Throwable t) {
                        Toast.makeText(getActivity(), "FAIL",
                                Toast.LENGTH_LONG).show();
                    }
                });

        SharedPreferences.Editor editor = authData.edit();
        editor.putString(USERNAME_PREF, username);
        editor.putString(PASSWORD_PREF, password);
        editor.putBoolean(REMEMBER_USERNAME_PREF, usernameCheckBox.isChecked());
        editor.putBoolean(REMEMBER_PASSWORD_PREF, passwordCheckBox.isChecked());
        editor.commit();
    }

}
