package de.hauke_stieler.geonotes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class PreferencesActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_settings));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, settingsFragment)
                .commit();


        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //save();
        //savePreferences();
        finish();
        return true;
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = preferences.edit();

        SwitchPreferenceCompat zoomButtonPref = settingsFragment.findPreference("zoom");
        //editor.putBoolean()

        editor.apply();
    }

    /**
     * Save settings and finish activity
     */
    private void save() {
        SharedPreferences.Editor editor = preferences.edit();

        boolean checked = ((Switch) findViewById(R.id.settings_zoom_switch)).isChecked();
        editor.putBoolean(getString(R.string.pref_zoom_buttons), checked);

        String mapScaleString = ((EditText) findViewById(R.id.settings_scale_input)).getText().toString();
        float mapScale = 1.0f;
        try {
            mapScale = Float.parseFloat(mapScaleString);
            if (mapScale < 0.1f) {
                mapScale = 0.1f;
            }
        } catch (NumberFormatException e) {
            // Nothing to do, just don't crash because of wrong input
        }
        editor.putFloat(getString(R.string.pref_map_scaling), mapScale);

        boolean is_dark_mode = ((Switch) findViewById(R.id.settings_dark_mode)).isChecked();
        editor.putBoolean(getString(R.string.pref_dark_mode), is_dark_mode);

        editor.commit();
    }
}

