package android.endava.com.demoproject.activities;

import android.endava.com.demoproject.R;
import android.endava.com.demoproject.db.HelperProvider;
import android.endava.com.demoproject.fragments.SplashFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        Fragment fragment;
        HelperProvider.setHelper(getApplicationContext());
        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().findFragmentByTag("splash_fragment_tag");
        } else {
            fragment = new SplashFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_splash_activity_layout, fragment, "splash_fragment_tag")
                .commit();
    }
}
