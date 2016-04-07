package endava.com.demoproject.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import endava.com.demoproject.R;
import endava.com.demoproject.fragments.SplashFragment;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_splash_activity_layout, new SplashFragment(), "splash_fragment_tag")
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("splashdebug", "onDestroy SplashActivity");
    }
}
