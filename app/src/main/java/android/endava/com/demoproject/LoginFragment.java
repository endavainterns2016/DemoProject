package android.endava.com.demoproject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.endava.com.demoproject.db.DataBaseHelper;
import android.endava.com.demoproject.db.HelperFactory;
import android.endava.com.demoproject.model.User;
import android.endava.com.demoproject.retrofit.ServiceFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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
import java.sql.SQLException;
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
    private Callback<List<User>> loginCallBack;
    private DataBaseHelper dbHelper;
    private CheckBox usernameCheckBox;
    private String username;
    private String password;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        dbHelper = HelperFactory.getHelper();
        loginOnClickListener = new LoginOnClickListener();

        IntentFilter filter = new IntentFilter();
        filter.addAction("user_was_saved_to_db");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getFragmentManager().beginTransaction().replace(R.id.root_activity_layout, new ReposListFragment()).commit();
            }
        };
        getActivity().registerReceiver(broadcastReceiver, filter);

        usernameCheckBox = (CheckBox) v.findViewById(R.id.name_chbx);
        mPasswordEdt = (EditText) v.findViewById(R.id.password_edt);
        mUSerNameEdt = (EditText) v.findViewById(R.id.login_edt);
        mLoginBtn = (Button) v.findViewById(R.id.login_bnt);
        mToolbar = (Toolbar) v.findViewById(R.id.fragment_login_toolbar);
        mToolbar.setTitle(R.string.app_name);

        User user = null;
        try {
            if (!dbHelper.getUserDAO().getAllUsers().isEmpty())
                user = dbHelper.getUserDAO().getAllUsers().get(0);
        } catch (SQLException e) {
            Log.e("SQLException ", e.toString());
        }
        if (user != null) {
            usernameCheckBox.setChecked(user.getShouldSaveUserName());
            mUSerNameEdt.append(user.getUserName());
        }


        loginCallBack = new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body() != null) {

                    User user = response.body().get(0);
                    user.setUserName(username);
                    user.setShouldSaveUserName(usernameCheckBox.isChecked());
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

    private void handleLoginRequest() {
        try {
            credentials = android.util.Base64.encodeToString(
                    (username + ":" + password).getBytes("UTF-8"),
                    android.util.Base64.NO_WRAP);
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
            if (credentialsAreFilled(username, password))
                handleLoginRequest();
        }
    }
}
