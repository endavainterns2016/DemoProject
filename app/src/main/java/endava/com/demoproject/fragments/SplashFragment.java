package endava.com.demoproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import endava.com.demoproject.R;
import endava.com.demoproject.activities.LoginActivity;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.activities.SplashActivity;
import endava.com.demoproject.presenter.SplashPresenter;
import endava.com.demoproject.view.SplashView;


public class SplashFragment extends Fragment implements SplashView {
    private SplashActivity mActivity;
    private SplashPresenter splashPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SplashActivity) {
            mActivity = (SplashActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container,
                false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        splashPresenter = new SplashPresenter();
        splashPresenter.attachView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startNextActivity();
            }
        }, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        splashPresenter.detachView();
    }

    @Override
    public void startNextActivity() {

        Intent intent;
        if (splashPresenter.userIsAvailable()) {
            intent = new Intent(mActivity, MainActivity.class);
        } else {
            intent = new Intent(mActivity, LoginActivity.class);
        }
        startActivity(intent);
        mActivity.finish();
    }
}
