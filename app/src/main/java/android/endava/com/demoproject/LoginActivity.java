package android.endava.com.demoproject;

import android.content.Intent;
import android.endava.com.demoproject.db.DataBaseHelper;
import android.endava.com.demoproject.db.HelperFactory;
import android.endava.com.demoproject.model.User;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
    private DataBaseHelper dbHelper;
    private User user;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.toolbar_color));

        HelperFactory.setHelper(getApplicationContext());

        dbHelper = HelperFactory.getHelper();
        user = null;
        try {
            if (!dbHelper.getUserDAO().getAllUsers().isEmpty())
                user = dbHelper.getUserDAO().getAllUsers().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
