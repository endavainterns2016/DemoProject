package android.endava.com.demoproject.fragments;


import android.content.SharedPreferences;
import android.endava.com.demoproject.R;
import android.endava.com.demoproject.dialogs.NumberPickerPreference;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static String AUTO_SYNC_NUMBER_PICKER_KEY = "auto_sync_number_picker_key";
    private NumberPickerPreference numberPickerPreference;
    private SharedPreferences prefs;
    private Integer value;
    private String summary;

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
        numberPickerPreference = (NumberPickerPreference) findPreference(AUTO_SYNC_NUMBER_PICKER_KEY);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        value = prefs.getInt(AUTO_SYNC_NUMBER_PICKER_KEY, 1);
        summary = String.format(getString(R.string.settings_auto_sync_timer_value), value);
        numberPickerPreference.setSummary(summary);
    }
}
