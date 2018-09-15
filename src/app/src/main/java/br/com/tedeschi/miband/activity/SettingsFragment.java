package br.com.tedeschi.miband.activity;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

import br.com.tedeschi.miband.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            Log.d("TAG", preference.getKey() + ": " + stringValue);

            return true;
        }
    };

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_notification, rootKey);

        PreferenceScreen prefScreen = getPreferenceScreen();
        int prefCount = prefScreen.getPreferenceCount();

        Log.d("TAG", "prefCount: " + prefCount);

        for (int i = 0; i < prefCount; i++) {
            Preference pref = prefScreen.getPreference(i);

            int count = ((PreferenceCategory) pref).getPreferenceCount();

            for (int j = 0; j < count; j++) {
                Preference prefe = ((PreferenceCategory) pref).getPreference(j);
                prefe.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
            }
        }
    }
}
