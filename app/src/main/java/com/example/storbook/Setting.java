package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {
    TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        status = (TextView) findViewById(R.id.StatusText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button v = (Button)findViewById(R.id.Caretakerbtn);
        if(((global) this.getApplication()).isCaretaker()){
            v.setText("I am patient");
            status.setText("In CareTaker Mode");
        }
        else{
            v.setText("I am CareTaker");
            status.setText("In Cared Mode");
        }
    }
    //Button to switch the global CT mode on or off
    public void switchCTmode(View v) {
        Button b = (Button) v;
        if (((global) this.getApplication()).isCaretaker()) {
            b.setText("I am patient");
            status.setText("In CareTaker Mode");
            ((global) this.getApplication()).setCaretaker(false);
            Toast.makeText(Setting.this, "Switched to Patient mode!", Toast.LENGTH_SHORT).show();
            // To refresh the page
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        else{
            Intent i = new Intent(this, PasswordAuth.class);
            startActivity(i);
        }
    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }

}