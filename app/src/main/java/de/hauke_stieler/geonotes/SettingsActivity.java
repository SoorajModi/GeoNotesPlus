package de.hauke_stieler.geonotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Activity class that handles application settings
 */
public class SettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;
    Button ChangePassword;
    FirebaseAuth Auth1;
    Button LogOut;

    /**
     * Create settings page
     *
     * @param savedInstanceState - application state
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        preferences = getSharedPreferences(getString(R.string.pref_file), MODE_PRIVATE);

        setDarkMode();
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
     * Switch application to dark mode
     */
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
                TextView dark_mode_button = findViewById(R.id.dark_mode_button);
                EditText input = findViewById(R.id.settings_scale_input);
                Intent intent = new Intent();

                if (isChecked) {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.black));
                    ll.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                    zoom_button.setTextColor(Color.WHITE);
                    scale_factor.setTextColor(Color.WHITE);
                    dark_mode_button.setTextColor(Color.WHITE);
                    input.setTextColor(Color.WHITE);
                    intent.putExtra("Dark Mode", "true");
                } else {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.primary_dark));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
                    ll.setBackgroundColor(Color.WHITE);
                    zoom_button.setTextColor(Color.BLACK);
                    scale_factor.setTextColor(Color.BLACK);
                    dark_mode_button.setTextColor(Color.BLACK);
                    input.setTextColor(Color.BLACK);
                    intent.putExtra("Dark Mode", "false");
                }
                intent.setClass(SettingsActivity.this, MainActivity.class);
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
        setDarkMode();
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
    }

    /**
     * Save settings and finish activity
     */
    @Override
    public boolean onSupportNavigateUp() {
        save();
        finish();
        return true;
    }


}