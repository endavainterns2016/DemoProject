package android.endava.com.demoproject.activities;

import android.content.Intent;
import android.endava.com.demoproject.R;
import android.endava.com.demoproject.db.ClientDataBaseHelper;
import android.endava.com.demoproject.db.HelperFactory;
import android.endava.com.demoproject.fragments.LoginFragment;
import android.endava.com.demoproject.model.User;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {
    private User user;
    private Toolbar mToolbar;
    private ClientDataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_color));

        HelperFactory.setHelper(getApplicationContext());

        dbHelper = ClientDataBaseHelper.getInstance();
        user = dbHelper.getUser();
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_login_activity_layout, new LoginFragment()).commit();
        }
    }

}
