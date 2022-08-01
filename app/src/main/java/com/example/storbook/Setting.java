package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
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
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Set;

public class Setting extends AppCompatActivity {
    TextView status;
    Switch nightmode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        status = (TextView) findViewById(R.id.StatusText);

        //Night mode switch
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.storbook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        nightmode = findViewById(R.id.nightmodeswitch);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            nightmode.setChecked(true);
        }
        else{
            nightmode.setChecked(false);
        }
        nightmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    editor.putInt("nightmode", 0);
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Toast.makeText(Setting.this, "Night Mode Off", Toast.LENGTH_SHORT).show();
                }
                else{
                    editor.putInt("nightmode", 1);
                    editor.apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Toast.makeText(Setting.this, "Night Mode On", Toast.LENGTH_SHORT).show();
                }
            }
        });
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