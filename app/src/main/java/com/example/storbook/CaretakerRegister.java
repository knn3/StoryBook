package com.example.storbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CaretakerRegister extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore db;

    public void loginLinkClicked(View view){
        Intent myIntent = new Intent(CaretakerRegister.this, CaretakerLogin.class);
        CaretakerRegister.this.startActivity(myIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_register);

        mFullName = findViewById(R.id.caretakerFullName);
        mEmail = findViewById(R.id.caretakerEmail);
        mPassword = findViewById(R.id.caretakerPassword);
        mRegisterBtn = findViewById(R.id.loginButton);
        mLoginBtn = findViewById(R.id.registerLink);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();

        // if user authenticated, redirect to MainActivity
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String userName = mFullName.getText().toString();

                // check requirements of email and password field
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

                //register the user in firebase if success, redirect to Caretaker Main Page
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CaretakerRegister.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),CaretakerMain.class));

                            // Add a new document for the new user with generated ID and the other info
                            Map<String, Object> user = new HashMap<>();
                            user.put("userName", userName);
                            user.put("eMail", email);
                            user.put("passWord", password);

                            db.collection("users")
                                    .document(fAuth.getUid()).set(user);
                        }else {
                            Toast.makeText(CaretakerRegister.this, "Error!" + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });


    }
}