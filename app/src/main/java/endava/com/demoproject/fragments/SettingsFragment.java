package endava.com.demoproject.fragments;


import android.content.SharedPreferences;
import endava.com.demoproject.R;
import endava.com.demoproject.dialogs.NumberPickerPreference;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static String AUTO_SYNC_NUMBER_PICKER_KEY = "auto_sync_number_picker_key";
    public static Integer AUTO_SYNC_DEFAULT_VALUE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSettingsView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setUpSettingsView();
    }

    public void setUpSettingsView() {
        NumberPickerPreference numberPickerPreference = (NumberPickerPreference) findPreference(AUTO_SYNC_NUMBER_PICKER_KEY);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Integer value = prefs.getInt(AUTO_SYNC_NUMBER_PICKER_KEY, AUTO_SYNC_DEFAULT_VALUE);
        if (prefs.getInt(AUTO_SYNC_NUMBER_PICKER_KEY, AUTO_SYNC_DEFAULT_VALUE) == 1) {
            prefs.edit().putInt(AUTO_SYNC_NUMBER_PICKER_KEY, value)
                    .apply();
        }
        String summary = String.format(getString(R.string.settings_auto_sync_timer_value), value);
        numberPickerPreference.setSummary(summary);
    }
}
