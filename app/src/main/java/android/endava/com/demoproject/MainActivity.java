package android.endava.com.demoproject;

import android.endava.com.demoproject.db.HelperFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HelperFactory.setHelper(getApplicationContext());
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new LoginFragment())
                .commit();
    }
}
