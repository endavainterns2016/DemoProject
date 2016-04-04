package android.endava.com.demoproject.activities;

import android.endava.com.demoproject.R;
import android.endava.com.demoproject.db.HelperFactory;
import android.endava.com.demoproject.fragments.LoginFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_color));
        HelperFactory.setHelper(getApplicationContext());
        Fragment fragment;
        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().findFragmentByTag("login_fragment_tag");
        } else {
            fragment = new LoginFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_login_activity_layout, fragment, "login_fragment_tag")
                .commit();
    }
}
