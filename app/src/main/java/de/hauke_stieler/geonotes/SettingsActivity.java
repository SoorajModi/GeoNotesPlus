package de.hauke_stieler.geonotes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import de.hauke_stieler.geonotes.settings.SettingsFragment;

<<<<<<< HEAD
import skin.support.SkinCompatManager;


/**
 * Activity class that handles application settings
 */
public class SettingsActivity extends BaseActivity {
=======
public class SettingsActivity extends AppCompatActivity {
    SettingsFragment settingsFragment = new SettingsFragment();
>>>>>>> c7c6292e029c45ad988c4654769b1dd0de45fd46

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
<<<<<<< HEAD

        preferences = getSharedPreferences(getString(R.string.pref_file), MODE_PRIVATE);

        load();

        ChangePassword = findViewById(R.id.ChangePass);
        LogOut = findViewById(R.id.logOut);
        Auth1 = FirebaseAuth.getInstance();
        FirebaseUser user1 = Auth1.getCurrentUser();


        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText text = new EditText(v.getContext());
                AlertDialog.Builder reset1 = new AlertDialog.Builder(v.getContext()); // construct an aler dialog to ask for the user's new password.
                reset1.setTitle("Change Password");
                reset1.setMessage("Enter your new Password(Must be greater than 6 charchters):");
                reset1.setView(text);
                reset1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String upadtedPass = text.getText().toString(); // get the new password
                        user1.updatePassword(upadtedPass).addOnCompleteListener(new OnCompleteListener<Void>() {  // use the updatepaswword method from firebase
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {  //if the task is succefull
                                    Toast.makeText(SettingsActivity.this, "Your password have been changed succefully!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } else if (!task.isSuccessful()) {  // if the task fails
                                    Toast.makeText(SettingsActivity.this, "Something went wrong :(, failed to change the password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                reset1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                reset1.show();
            }
        });


        LogOut.setOnClickListener(new View.OnClickListener() {  // when the user presses the button sign out, then the system signs them out
            @Override
            public void onClick(View v) {
                Auth1.signOut();
                Toast.makeText(SettingsActivity.this, "You have logged out successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    /**
     * Load user settings
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void load() {
        boolean prefZoomButtons = preferences.getBoolean(getString(R.string.pref_zoom_buttons), true);
        ((Switch) findViewById(R.id.settings_zoom_switch)).setChecked(prefZoomButtons);

        float prefMapScaling = preferences.getFloat(getString(R.string.pref_map_scaling), 1.0f);
        ((EditText) findViewById(R.id.settings_scale_input)).setText("" + prefMapScaling);

        boolean prefDarkMode = preferences.getBoolean(getString(R.string.pref_dark_mode), false);
        ((Switch) findViewById(R.id.settings_dark_mode)).setChecked(prefDarkMode);
        ((Switch) findViewById(R.id.settings_dark_mode)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
            }else {
                SkinCompatManager.getInstance().restoreDefaultTheme();
            }
        });
    }

    /**
     * Save user settings
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
=======
>>>>>>> c7c6292e029c45ad988c4654769b1dd0de45fd46
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
