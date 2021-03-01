package de.hauke_stieler.geonotes.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

import de.hauke_stieler.geonotes.MainActivity;
import de.hauke_stieler.geonotes.R;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setDarkMode();

        preferences = getSharedPreferences(getString(R.string.pref_file), MODE_PRIVATE);

        load();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setDarkMode() {
        Switch switchDarkMode = findViewById(R.id.settings_dark_mode);
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toolbar toolbar = findViewById(R.id.settings_toolbar);
                LinearLayout ll = findViewById(R.id.settings_page);
                TextView zoom_button = findViewById(R.id.zoon_button);
                TextView scale_factor = findViewById(R.id.scale_factor);
                TextView dark_mode_buton = findViewById(R.id.dark_mode_button);
                EditText input = findViewById(R.id.settings_scale_input);
                Intent intent = new Intent();

                if(isChecked) {
                    toolbar.setBackgroundColor(Color.rgb(61, 56, 56));
                    getWindow().setStatusBarColor(Color.rgb(20, 20, 20));
                    ll.setBackgroundColor(Color.rgb(46, 46, 46));
                    zoom_button.setTextColor(Color.WHITE);
                    scale_factor.setTextColor(Color.WHITE);
                    dark_mode_buton.setTextColor(Color.WHITE);
                    input.setTextColor(Color.WHITE);
                    intent.putExtra("Dark Mode", "true");
                } else {
                    toolbar.setBackgroundColor(Color.rgb(57, 142, 62));
                    getWindow().setStatusBarColor(Color.rgb(57, 142, 62));
                    ll.setBackgroundColor(Color.WHITE);
                    zoom_button.setTextColor(Color.BLACK);
                    scale_factor.setTextColor(Color.BLACK);
                    dark_mode_buton.setTextColor(Color.BLACK);
                    input.setTextColor(Color.BLACK);
                    intent.putExtra("Dark Mode", "false");
                }
                intent.setClass(SettingsActivity.this, MainActivity.class);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void load() {
        boolean prefZoomButtons = preferences.getBoolean(getString(R.string.pref_zoom_buttons), true);
        ((Switch) findViewById(R.id.settings_zoom_switch)).setChecked(prefZoomButtons);

        float prefMapScaling = preferences.getFloat(getString(R.string.pref_map_scaling), 1.0f);
        ((EditText) findViewById(R.id.settings_scale_input)).setText("" + prefMapScaling);

        boolean prefDarkMode = preferences.getBoolean(getString(R.string.pref_dark_mode), false);
        ((Switch) findViewById(R.id.settings_dark_mode)).setChecked(prefDarkMode);
        setDarkMode();
    }

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

    @Override
    public boolean onSupportNavigateUp() {
        save();
        finish();
        return true;
    }
}