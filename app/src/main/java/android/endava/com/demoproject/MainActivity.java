package android.endava.com.demoproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.endava.com.demoproject.db.DataBaseHelper;
import android.endava.com.demoproject.db.HelperFactory;
import android.endava.com.demoproject.model.User;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DataBaseHelper dbHelper;
    private User user;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private TextView user_login_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        dbHelper = HelperFactory.getHelper();
        user = null;
        try {
            if (!dbHelper.getUserDAO().getAllUsers().isEmpty())
                user = dbHelper.getUserDAO().getAllUsers().get(0);
        } catch (SQLException e) {
            Log.e("SQLException ", e.toString());
        }

        mNavigationView = (NavigationView) findViewById(R.id.nvView);
        View header = mNavigationView.getHeaderView(0);
        user_login_nav = (TextView) header.findViewById(R.id.user_login);
        mNavigationView.setNavigationItemSelectedListener(this);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_color));
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);
        user_login_nav.setText(user.getUserName());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_activity_layout, new ReposListFragment()).commit();

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_signout:
                logOutDialogShow();
                break;
        }
        mDrawer.closeDrawers();
        return true;
    }

    public void logOutDialogShow() {
        AlertDialog.Builder logOutDialog = new AlertDialog.Builder(this);
        logOutDialog.setTitle(R.string.menu_signout);
        logOutDialog.setCancelable(true);
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
        try {
            dbHelper.getAppDAO().delete(user.getApp());
            dbHelper.getUserDAO().delete(user);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } catch (SQLException e) {
            Log.e("SQLException ", e.toString());
        }
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

}
