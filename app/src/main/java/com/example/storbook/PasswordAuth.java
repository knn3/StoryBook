package com.example.storbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_auth);
        CTpassword = (EditText) findViewById(R.id.goCTPassword);
        Accessbtn = (Button)findViewById(R.id.passwordauthbtn);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        String Turepassword = ((global)this.getApplication()).passWord;

        Toolbar bar = findViewById(R.id.toolbar);


        // Click the "ACCESS" button
        Accessbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Compare the password entered with the password retrieved from the database
                String password = CTpassword.getText().toString().trim();
                if (password.equals(Turepassword)) {
                    // If correct set to CT mode and jump back to setting page
                    setToCT();
                    Toast.makeText(PasswordAuth.this, R.string.switchct, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PasswordAuth.this, MainActivity.class));
                } else {
                    // If incorrect display a message and do nothing
                    Toast.makeText(PasswordAuth.this, R.string.incorrectpw, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, Setting.class);
        this.startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(this, Setting.class);
        this.startActivity(myIntent);
    }

    private void setToCT(){
            ((global) this.getApplication()).setCaretaker(true);
        }

}