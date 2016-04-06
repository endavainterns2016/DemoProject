package endava.com.demoproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import endava.com.demoproject.R;
import endava.com.demoproject.presenter.LoginPresenter;
import endava.com.demoproject.presenter.LoginPresenterImpl;
import endava.com.demoproject.view.LoginView;

public class LoginFragment extends Fragment implements LoginView {

    @Bind(R.id.password_edt)
    EditText mPasswordEdt;

    @Bind(R.id.login_edt)
    EditText mUSerNameEdt;

    @Bind(R.id.login_bnt)
    Button mLoginBtn;

    @Bind(R.id.name_chbx)
    CheckBox usernameCheckBox;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        LoginPresenter presenter = new LoginPresenterImpl(this);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setUsernameError() {

    }

    @Override
    public void setPasswordError() {

    }
}
