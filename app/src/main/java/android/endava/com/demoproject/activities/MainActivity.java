package android.endava.com.demoproject.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.endava.com.demoproject.R;
import android.endava.com.demoproject.RoundedTransformation;
import android.endava.com.demoproject.asyncLoader.UserLoadingTask;
import android.endava.com.demoproject.db.ClientDataBaseHelper;
import android.endava.com.demoproject.fragments.ReposListFragment;
import android.endava.com.demoproject.model.User;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,LoaderManager.LoaderCallbacks<User> {
    private User user;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private ProgressDialog progressDialog;

    private ClientDataBaseHelper dbHelper;
    private TextView user_login_nav;
    private ImageView avatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        dbHelper = ClientDataBaseHelper.getInstance();
        progressDialog = ProgressDialog.show(this, "", getString(R.string.progress_dialog_loading));
        getSupportLoaderManager().restartLoader(LoaderIDs.USER_LOADING_TASK_ID, savedInstanceState, this);

        mNavigationView = (NavigationView) findViewById(R.id.nvView);
        View header = mNavigationView.getHeaderView(0);
        user_login_nav = (TextView) header.findViewById(R.id.user_login);
        avatarImageView = (ImageView) header.findViewById(R.id.avatar);
        mNavigationView.setNavigationItemSelectedListener(this);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_color));
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_activity_layout, new ReposListFragment()).commit();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_signout:
                logOutDialogShow();
                break;
            case R.id.drawer_settings:
                Intent intentToMain = new Intent(this, SettingsActivity.class);
                startActivity(intentToMain);
                break;
        }
        mDrawer.closeDrawers();
        return true;
    }

    public Toolbar getActivityToolbar() {
        return mToolbar;
    }

    public void logOutDialogShow() {
        AlertDialog.Builder logOutDialog = new AlertDialog.Builder(this);
        logOutDialog.setTitle(R.string.menu_signout);
        logOutDialog.setCancelable(true);
        logOutDialog.setIcon(R.drawable.ic_power_settings_new_black_24dp);
        logOutDialog.setMessage(R.string.logout_message);

        logOutDialog.setPositiveButton(R.string.alert_delete_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                logOutUser();
                dialog.dismiss();
            }
        });

        logOutDialog.setNegativeButton(R.string.alert_delete_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        logOutDialog.show();
    }

    public void logOutUser() {
        dbHelper.deleteUser(user);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(mNavigationView)) {
            mDrawer.closeDrawers();
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new UserLoadingTask(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader loader, User result) {
        user = result;
        user_login_nav.setText(result.getUserName());
        Picasso.with(this).load(result.getAvatarUrl()).resize(80, 80).transform(new RoundedTransformation(getResources(), 40)).placeholder(R.drawable.nav_drawer_background).into(avatarImageView);
        progressDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
