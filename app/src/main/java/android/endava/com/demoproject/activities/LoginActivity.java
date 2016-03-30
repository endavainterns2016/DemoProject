package android.endava.com.demoproject.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.endava.com.demoproject.R;
import android.endava.com.demoproject.asyncLoader.UserLoadingTask;
import android.endava.com.demoproject.constants.LoaderConstants;
import android.endava.com.demoproject.db.HelperFactory;
import android.endava.com.demoproject.fragments.LoginFragment;
import android.endava.com.demoproject.model.User;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<User> {

    private ProgressDialog progressDialog;
    private User user;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_color));
        HelperFactory.setHelper(getApplicationContext());

        IntentFilter filter = new IntentFilter();
        filter.addAction("load_finished");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                doLogin(user);
            }
        };
        LoginActivity.this.registerReceiver(broadcastReceiver, filter);
        progressDialog = ProgressDialog.show(this, "", getString(R.string.progress_dialog_loading));
        getSupportLoaderManager().restartLoader(LoaderConstants.USER_LOADING_TASK_ID, savedInstanceState, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoginActivity.this.unregisterReceiver(broadcastReceiver);
    }

    private void doLogin(User user) {
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            LoginActivity.this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_login_activity_layout, new LoginFragment()).commit();
        }
    }

    @Override
    public Loader<User> onCreateLoader(final int id, final Bundle args) {
        return new UserLoadingTask(LoginActivity.this);
    }

    @Override
    public void onLoadFinished(final Loader<User> loader, final User result) {
        user = result;
        Intent i = new Intent("load_finished");
        sendBroadcast(i);
        progressDialog.dismiss();
    }

    @Override
    public void onLoaderReset(final Loader<User> loader) {
    }
}
