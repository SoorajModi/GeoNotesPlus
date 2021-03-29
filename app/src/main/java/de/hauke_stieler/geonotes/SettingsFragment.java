package de.hauke_stieler.geonotes;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences (Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_main, rootKey); // loads preferences
    }
}
