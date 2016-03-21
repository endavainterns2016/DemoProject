package android.endava.com.demoproject;

import android.endava.com.demoproject.db.HelperFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HelperFactory.setHelper(getApplicationContext());
        setContentView(R.layout.main_activity);
        Fragment fragment;
        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().findFragmentByTag("splash_fragment_tag");
        } else {
            fragment = new SplashFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_activity_layout, fragment, "splash_fragment_tag")
                .commit();
    }
}