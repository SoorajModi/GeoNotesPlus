package de.hauke_stieler.geonotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
// CODE iS INFULUNCED BY A YOUTUBE SOURCE : https://www.youtube.com/watch?v=TwHmrZxiPA8&ab_channel=SmallAcademy


public class Login extends AppCompatActivity {

    EditText Email1,Password1;
    Button LoginBtn1;
    TextView CreateBtn1;
    FirebaseAuth Auth1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email1 = findViewById(R.id.Email);
        Password1 = findViewById(R.id.password);
        Auth1 = FirebaseAuth.getInstance();
        LoginBtn1 = findViewById(R.id.LoginBtn);
        CreateBtn1 = findViewById(R.id.textView3);


        LoginBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTest = Email1.getText().toString().trim();
                String passwordTest = Password1.getText().toString().trim();
                if(TextUtils.isEmpty(emailTest)){
                    Email1.setError("Email is Required");
                    return;
                }
                else if(TextUtils.isEmpty(passwordTest)){
                    Password1.setError("Password is Required");
                    return;
                }
                else if(passwordTest.length() < 6)
                {
                    Password1.setError("Password must greater or equal to 6 charchters");
                    return;
                }
                // authenticattion
                Auth1.signInWithEmailAndPassword(emailTest,passwordTest).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in Succefully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        } else if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "Error! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        CreateBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });





    }
}