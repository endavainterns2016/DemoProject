package endava.com.demoproject.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.squareup.picasso.Picasso;

import endava.com.demoproject.R;
import endava.com.demoproject.RoundedTransformation;
import endava.com.demoproject.asyncLoader.UserLoadingTask;
import endava.com.demoproject.constants.LoaderConstants;
import endava.com.demoproject.fragments.ReposLikeFragment;
import endava.com.demoproject.fragments.ReposListFragment;
import endava.com.demoproject.fragments.ReposSyncFragment;
import endava.com.demoproject.helpers.DbHelper;
import endava.com.demoproject.model.User;

public class MainActivity extends AppCompatActivity implements OnMenuTabClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<User> {

    private BottomBar mBottomBar;
    private User user;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private DbHelper dbHelper;
    private TextView user_login_nav;
    private ImageView avatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        dbHelper = DbHelper.getInstance();
        getSupportLoaderManager().restartLoader(LoaderConstants.USER_LOADING_TASK_ID, savedInstanceState, this);

        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.root_activity_coordinator_layout),
                findViewById(R.id.root_activity_layout), savedInstanceState);
        mBottomBar.noNavBarGoodness();
        mBottomBar.setItemsFromMenu(R.menu.repos_list_fragment_bottom_bar, this);
        mBottomBar.setActiveTabColor(getResources().getColor(R.color.colorAccent));
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

    public BottomBar getBottomBar(){
        return mBottomBar;
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(mNavigationView)) {
            mDrawer.closeDrawers();
        } else {
            super.onBackPressed();
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
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onMenuTabSelected(int menuItemId) {
        switch (menuItemId) {
            case R.id.repo:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.root_activity_layout, new ReposListFragment())
                        .commit();
                break;
            case R.id.sync:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.root_activity_layout, new ReposSyncFragment())
                        .commit();
                break;
            case R.id.like:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.root_activity_layout, new ReposLikeFragment())
                        .commit();
                break;
        }
    }

    @Override
    public void onMenuTabReSelected(int menuItemId) {

    }
}
