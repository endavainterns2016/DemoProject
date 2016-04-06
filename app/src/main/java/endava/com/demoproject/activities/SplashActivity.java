package endava.com.demoproject.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import endava.com.demoproject.R;
import endava.com.demoproject.fragments.SplashFragment;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        Fragment fragment;
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
