package android.endava.com.demoproject;


import android.endava.com.demoproject.Model.AuthResponse;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Toolbar mToolbar;
    private EditText mPasswordEdt;
    private EditText mUSerNameEdt;
    private Button mLoginBtn;
    private String credentials;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);


        mPasswordEdt = (EditText) v.findViewById(R.id.password_edt);
        mUSerNameEdt = (EditText) v.findViewById(R.id.login_edt);
        mLoginBtn = (Button) v.findViewById(R.id.login_bnt);
        mToolbar = (Toolbar) v.findViewById(R.id.fragment_login_toolbar);
        mToolbar.setTitle(R.string.app_name);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoginRequest();
            }
        });

        return v;
    }

    private void handleLoginRequest() {
        if (mPasswordEdt.length() > 0 && mUSerNameEdt.length() > 0) {
            try {
                credentials = android.util.Base64.encodeToString(
                        (mUSerNameEdt.getText().toString() + ":" + mPasswordEdt.getText().toString()).getBytes("UTF-8"),
                        android.util.Base64.NO_WRAP
                );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            DemoProjectAPI.Factory.getInstance().auth("Basic " + credentials).enqueue(new Callback<List<AuthResponse>>() {
                @Override
                public void onResponse(Call<List<AuthResponse>> call, Response<List<AuthResponse>> response) {
                    if (response.body() != null) {
                        Toast.makeText(getActivity(), response.body().get(0).getToken(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.credentials_error),
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<AuthResponse>> call, Throwable t) {
                    Toast.makeText(getActivity(), getString(R.string.get_token_error),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
