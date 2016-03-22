package android.endava.com.demoproject;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_color));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_activity_layout, new ReposListFragment()).commit();

    }
}
