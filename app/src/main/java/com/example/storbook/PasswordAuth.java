package com.example.storbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Map;

public class PasswordAuth extends AppCompatActivity {
    EditText CTpassword;
    Button Accessbtn;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    DocumentSnapshot document;
    String truepwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_auth);
        CTpassword = (EditText) findViewById(R.id.goCTPassword);
        Accessbtn = (Button)findViewById(R.id.passwordauthbtn);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Gets user password from the database
        DocumentReference docRef = db.collection("users").document(fAuth.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d("db", "DocumentSnapshot data: " + document.getData());
                        Log.d("db", "db firstName getString() is: " + document.getString("passWord"));
                        truepwd = (String) document.getString("passWord");

                        Log.d("db", "pwd is: " + truepwd);

                    } else {
                        Log.d("db", "No such document");
                    }
                } else {
                    Log.d("db", "get failed with ", task.getException());
                }
            }
        });

        // Click the "ACCESS" button
        Accessbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Compare the password entered with the password retrieved from the database
                String password = CTpassword.getText().toString().trim();
                if (password.equals(truepwd)) {
                    // If correct set to CT mode and jump back to setting page
                    setToCT();
                    Toast.makeText(PasswordAuth.this, "Switched to CareTaker mode!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PasswordAuth.this, Setting.class));
                } else {
                    // If incorrect display a message and do nothing
                    Toast.makeText(PasswordAuth.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setToCT(){
            ((global) this.getApplication()).setCaretaker(true);
        }

}