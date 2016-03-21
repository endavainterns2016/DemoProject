package android.endava.com.demoproject;

import android.endava.com.demoproject.db.DataBaseHelper;
import android.endava.com.demoproject.db.HelperFactory;
import android.endava.com.demoproject.model.User;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    DataBaseHelper dbHelper;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HelperFactory.setHelper(getApplicationContext());
        setContentView(R.layout.main_activity);

        dbHelper = HelperFactory.getHelper();
        user = null;
        try {
            if (!dbHelper.getUserDAO().getAllUsers().isEmpty())
                user = dbHelper.getUserDAO().getAllUsers().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_activity_layout, new ReposListFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_activity_layout, new LoginFragment()).commit();
        }
    }
}
