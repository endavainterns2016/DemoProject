package android.endava.com.demoproject.activities;


import android.endava.com.demoproject.R;
import android.endava.com.demoproject.fragments.SettingsFragment;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.HashMap;

public class SettingsActivity extends PreferenceActivity {

    private static String DELEGATE_KEY = "delegate";
    private HashMap<String, AppCompatDelegate> delegateMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activty);

        ToolBarOnClickListener toolBarOnClickListener = new ToolBarOnClickListener();
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setTitle(R.string.menu_settings);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(toolBarOnClickListener);

        getFragmentManager().beginTransaction()
                .replace(R.id.setting_activity_container, new SettingsFragment())
                .commit();
    }

    private AppCompatDelegate getDelegateInstance() {
        if (!delegateMap.containsKey(DELEGATE_KEY)) {
            AppCompatDelegate mDelegate = AppCompatDelegate.create(this, null);
            delegateMap.put(DELEGATE_KEY, mDelegate);
            return mDelegate;
        } else {
            return delegateMap.get(DELEGATE_KEY);
        }
    }

    private ActionBar getSupportActionBar() {
        return getDelegateInstance().getSupportActionBar();
    }

    private void setSupportActionBar(Toolbar toolbar) {
        getDelegateInstance().setSupportActionBar(toolbar);
    }

    public class ToolBarOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    }
}
