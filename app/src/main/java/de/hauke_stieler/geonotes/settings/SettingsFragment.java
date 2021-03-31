package de.hauke_stieler.geonotes.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hauke_stieler.geonotes.LoginActivity;
import de.hauke_stieler.geonotes.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences (Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_main, rootKey); // loads preferences

        // Change password preference listener
        EditTextPreference changePassword = findPreference("password");
        changePassword.setOnPreferenceChangeListener((preference, newValue) -> {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            try {
                firebaseUser.updatePassword(newValue.toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Your password has been successfully changed!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong :(, failed to change the password", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IllegalArgumentException e) {
                // User enters blank password, prevents app crashing
                Toast.makeText(getActivity(), "Something went wrong :(, failed to change the password", Toast.LENGTH_LONG).show();
            }
            return true;
        });

        // Log out preference listener
        Preference logout = findPreference("logout");
        logout.setOnPreferenceClickListener(preference -> {
            FirebaseAuth firebaseAuth;
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            Toast.makeText(getActivity(), "You have logged out successfully!", Toast.LENGTH_SHORT).show();
            getActivity().finish();

            // Return to login activity neatly
            Intent returnToLogin = new Intent(getActivity(), LoginActivity.class);
            returnToLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(returnToLogin);
            return true;
        });
    }
}
