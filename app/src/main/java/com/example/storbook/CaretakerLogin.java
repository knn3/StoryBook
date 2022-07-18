package com.example.storbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CaretakerLogin extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    public void registerLinkClicked(View view){
        Intent myIntent = new Intent(CaretakerLogin.this, CaretakerRegister.class);
        CaretakerLogin.this.startActivity(myIntent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_login);

        mEmail = findViewById(R.id.caretakerEmail);
        mPassword = findViewById(R.id.caretakerPassword);
        mRegisterBtn = findViewById(R.id.registerLink);
        mLoginBtn = findViewById(R.id.loginButton);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        // Jump to main activity if already logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(CaretakerLogin.this, MainActivity.class));
        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password must be more than 6 characters.");
                }

                // progressBar.setVisibility(View.VISIBLE); Delete this as this may cause overload

                //Authenticate the user if success, redirect to Caretaker Main Page

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {  
                        if (task.isSuccessful()) {
                            Toast.makeText(CaretakerLogin.this, "User Logged In.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CaretakerLogin.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(CaretakerLogin.this, "Error!" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Jump to main activity if already logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(CaretakerLogin.this, MainActivity.class));
        }
    }
}