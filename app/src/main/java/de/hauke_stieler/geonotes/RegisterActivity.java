package de.hauke_stieler.geonotes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// CODE iS INFLUENCED BY A YOUTUBE SOURCE : https://www.youtube.com/watch?v=TwHmrZxiPA8&ab_channel=SmallAcademy

/**
 * Activity class that handles user registration with the app
 * <p>
 * Author: Mustafa Al-Obaidi
 */
public class RegisterActivity extends AppCompatActivity {
    EditText FullName1, Email1, Password1;
    Button RegisterBtn1;
    TextView LoginBtn1;
    FirebaseAuth Auth1;

    /**
     * Create registration page
     *
     * @param savedInstanceState - application state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FullName1 = findViewById(R.id.name_field);
        Email1 = findViewById(R.id.email_field);
        Password1 = findViewById(R.id.password_field);
        RegisterBtn1 = findViewById(R.id.register_button);
        LoginBtn1 = findViewById(R.id.already_registered_button);
        Auth1 = FirebaseAuth.getInstance();


//        if(fAuth.getCurrentUser() != null)
//        {
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//            finish();
//        }

        RegisterBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTest = Email1.getText().toString().trim();
                String passwordTest = Password1.getText().toString().trim();
                if (TextUtils.isEmpty(emailTest)) {
                    Email1.setError("Email is Required");
                    return;
                } else if (TextUtils.isEmpty(passwordTest)) {
                    Password1.setError("Password is Required");
                    return;
                } else if (passwordTest.length() < 6) {
                    Password1.setError("Password must greater or equal to 6 charchters");
                    return;
                }
                // progressorBar.setVisibility(1);
                //  REGISTER THE DATA BASE TO FIREBASE
                Auth1.createUserWithEmailAndPassword(emailTest, passwordTest).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "You Have Susccefully Registered ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            }
        });

        LoginBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}