package endava.com.demoproject.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import endava.com.demoproject.R;
import endava.com.demoproject.activities.LoginActivity;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.activities.SplashActivity;
import endava.com.demoproject.asyncLoader.UserLoadingTask;
import endava.com.demoproject.cacheableObserver.Subject;
import endava.com.demoproject.constants.LoaderConstants;
import endava.com.demoproject.model.User;
import endava.com.demoproject.presenter.SplashPresenter;
import endava.com.demoproject.presenter.SplashPresenterImpl;
import endava.com.demoproject.view.SplashView;


public class SplashFragment extends Fragment implements SplashView, LoaderManager.LoaderCallbacks<User> {
    private Subject subject = Subject.newInstance();
    private SplashActivity mActivity;
    private User user;
    private BroadcastReceiver broadcastReceiver;

    private SplashPresenter presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SplashActivity) {
            mActivity = (SplashActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SplashPresenterImpl(this);
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_screen, container,
                false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("load_finished");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                doLogin(user);
            }
        };
        mActivity.registerReceiver(broadcastReceiver, filter);
        mActivity.getSupportLoaderManager().restartLoader(LoaderConstants.USER_LOADING_TASK_ID, savedInstanceState, this);
    }

    private void doLogin(User user) {
        if (user != null) {
            Intent intent = new Intent(mActivity, MainActivity.class);
            startActivity(intent);
            mActivity.finish();
        } else {
            Intent intent = new Intent(mActivity, LoginActivity.class);
            startActivity(intent);
            mActivity.finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<User> onCreateLoader(final int id, final Bundle args) {
        return new UserLoadingTask(mActivity);
    }

    @Override
    public void onLoadFinished(final Loader<User> loader, final User result) {
        user = result;
        Intent i = new Intent("load_finished");
        mActivity.sendBroadcast(i);
    }

    @Override
    public void onLoaderReset(final Loader<User> loader) {
    }
}
