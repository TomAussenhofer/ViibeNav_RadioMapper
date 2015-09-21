package de.beacon.tom.viibenav_radiomapper.controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import de.beacon.tom.viibenav_radiomapper.R;
import de.beacon.tom.viibenav_radiomapper.model.Definitions;

/**
 * Created by TomTheBomb on 21.09.2015.
 */
public class SettingsActivity extends Activity{

    public static final String MEASUREMENT_AMT_THRESHOLD = "pref_amtRSSIsForMeasure";
    public static final String MIN_BEACONS_FOR_MEASURE = "pref_amtRSSIsForMeasure";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment sf = new SettingsFragment();
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,sf )
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            final EditTextPreference amt_for_Measure = (EditTextPreference) findPreference("pref_amtRSSIsForMeasure");
            amt_for_Measure.setSummary(amt_for_Measure.getText());

            final EditTextPreference max_Beacons_for_Measure = (EditTextPreference) findPreference("pref_minBeaconsForMeasure");
            max_Beacons_for_Measure.setSummary(max_Beacons_for_Measure.getText());
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference pref = findPreference(key);
            if (pref instanceof EditTextPreference) {
                EditTextPreference etp = (EditTextPreference) pref;
                pref.setSummary(etp.getText());

                if(etp.equals(findPreference("pref_amtRSSIsForMeasure"))) {
                    Definitions.MEASUREMENT_AMT_THRESHOLD = Integer.parseInt(sharedPreferences.getString(SettingsActivity.MEASUREMENT_AMT_THRESHOLD, "10"));
                }

                if(etp.equals(findPreference("pref_minBeaconsForMeasure"))) {
                    Definitions.MIN_BEACONS_FOR_MEASURE = Integer.parseInt(sharedPreferences.getString(SettingsActivity.MIN_BEACONS_FOR_MEASURE, "7"));
                }
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }


    }
}
