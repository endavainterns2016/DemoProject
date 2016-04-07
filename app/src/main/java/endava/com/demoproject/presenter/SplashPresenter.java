package endava.com.demoproject.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import javax.inject.Inject;

import endava.com.demoproject.DemoProjectApplication;
import endava.com.demoproject.activities.LoginActivity;
import endava.com.demoproject.activities.MainActivity;
import endava.com.demoproject.activities.SplashActivity;
import endava.com.demoproject.asyncLoader.UserLoadingTask;
import endava.com.demoproject.constants.LoaderConstants;
import endava.com.demoproject.fragments.SplashFragment;
import endava.com.demoproject.model.User;

public class SplashPresenter implements LoaderManager.LoaderCallbacks<User>{

    @Inject
    Context context;
    private User user;
    private BroadcastReceiver broadcastReceiver;
    private SplashActivity mActivity;

    private SplashFragment splashFragment;

    public SplashPresenter(SplashFragment splashFragment) {
        this.splashFragment = splashFragment;
    }

    public void onCreate() {
        DemoProjectApplication.getApplicationComponent().inject(this);
        mActivity = (SplashActivity) splashFragment.getActivity();
        registerBroadcast();
        mActivity.getSupportLoaderManager().restartLoader(LoaderConstants.USER_LOADING_TASK_ID, null, this);
    }


    public void onDestroy() {
        context.unregisterReceiver(broadcastReceiver);
        splashFragment = null;
        Log.d("splashdebug", "onDestroy SplashPresenter");
    }


    public void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("load_finished");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                doLogin(user);
            }
        };
        context.registerReceiver(broadcastReceiver, filter);
    }


    private void doLogin(User user) {
        Intent intent;
        if (user != null) {
            intent = new Intent(mActivity, MainActivity.class);
        } else {
            intent = new Intent(mActivity, LoginActivity.class);
        }
        mActivity.startActivity(intent);
        mActivity.finish();
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
