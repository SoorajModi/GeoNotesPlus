package de.hauke_stieler.geonotes.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;

import de.hauke_stieler.geonotes.Login;
import de.hauke_stieler.geonotes.MainActivity;
import de.hauke_stieler.geonotes.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import android.util.Log;



public class SettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;
    Button CP;
    FirebaseAuth Auth1;
    Button LogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        preferences = getSharedPreferences(getString(R.string.pref_file), MODE_PRIVATE);

        load();

        CP = findViewById(R.id.ChangePass);
        LogOut = findViewById(R.id.logOut);
        Auth1 = FirebaseAuth.getInstance();
        FirebaseUser user1 = Auth1.getCurrentUser();



        CP.setOnClickListener(new View.OnClickListener() {
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
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });




    }

    private void load() {
        boolean prefZoomButtons = preferences.getBoolean(getString(R.string.pref_zoom_buttons), true);
        ((Switch) findViewById(R.id.settings_zoom_switch)).setChecked(prefZoomButtons);

        float prefMapScaling = preferences.getFloat(getString(R.string.pref_map_scaling), 1.0f);
        ((EditText) findViewById(R.id.settings_scale_input)).setText("" + prefMapScaling);
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
        editor.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        save();
        finish();
        return true;
    }







}