package endava.com.demoproject.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import endava.com.demoproject.R;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.presenter.LoginPresenter;
import endava.com.demoproject.view.LoginView;

public class LoginFragment extends Fragment implements LoginView, View.OnClickListener {
    private Activity activity;
    private LoginPresenter presenter;
    private View view;
    private Button mLoginBtn;
    private EditText mUSerNameEdt;
    private CheckBox usernameCheckBox;
    private ProgressBar progressBar;
    private EditText mPasswordEdt;

    public static Fragment newInstance(){
        return  new LoginFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginBtn = (Button) view.findViewById(R.id.login_bnt);
        mPasswordEdt = (EditText) view.findViewById(R.id.password_edt);
        mUSerNameEdt = (EditText) view.findViewById(R.id.login_edt);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        usernameCheckBox = (CheckBox) view.findViewById(R.id.name_chbx);

        presenter = new LoginPresenter(this);
        presenter.attachView(this);
        presenter.populateView();
        mLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void populateView(String userName, boolean shouldSave) {
        mUSerNameEdt.append(userName);
        usernameCheckBox.setChecked(shouldSave);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        mLoginBtn.setEnabled(false);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        mLoginBtn.setEnabled(true);
    }

    @Override
    public void setError(String error) {
        Snackbar snackbar = Snackbar
                .make(view, error, Snackbar.LENGTH_LONG);
        snackbar.show();
        hideProgress();
    }

    @Override
    public void setConnectionError() {
        Snackbar snackbar = Snackbar
                .make(view, getString(R.string.get_token_error), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.try_again), this);
        snackbar.show();
        hideProgress();
    }

    @Override
    public void startMainActivity() {
        Intent intentToMain = new Intent(getActivity(), MainActivity.class);
        activity.startActivity(intentToMain);
        activity.finish();
    }

    @Override
    public void onClick(View v) {
        if (presenter.validateCredentials(mUSerNameEdt.getText().toString(), mPasswordEdt.getText().toString())) {
            if (usernameCheckBox.isChecked()) {
                presenter.rememberUserName();
            } else {
                presenter.forgetUserName();
            }
            presenter.doLogin();
        }
    }
}
