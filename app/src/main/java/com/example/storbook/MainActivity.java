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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


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
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static ImageView profileImage;
    int[] images = {R.drawable.pwdstatus,R.drawable.ctstatus};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Jump to login page if did not log in
        if(currentUser == null){
            Intent i = new Intent(MainActivity.this, CaretakerLogin.class);
            startActivity(i);
        }
        // Switch profile picture depending on pwd or ct

        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentSnapshot docRef = db.collection("users").document(currentUser.getUid()).get().getResult();
        boolean isCT = (boolean) docRef.get("isCareTaker");
         */

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Double check if the user logged out
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Jump to login page if not logged in
        if(currentUser == null){
            Intent i = new Intent(MainActivity.this, CaretakerLogin.class);
            startActivity(i);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Switch profile of the home page depending on pwd or ct
        profileImage = (ImageView)findViewById(R.id.profilestatuspic);
        View pwdpage=findViewById(R.id.pwd_page);
        View ctpage=findViewById(R.id.caretakerinfo);
        Toolbar mtoolbar=findViewById(R.id.toolbar);
        // Switch profile of the home page depending on pwd or ct
        profileImage = (ImageView)findViewById(R.id.profilestatuspic);
        if (((global) this.getApplication()).isCaretaker()){
            profileImage.setImageResource(R.drawable.ctstatus);
            pwdpage.setVisibility(View.INVISIBLE);
            pwdpage.setEnabled(false);
            ctpage.setEnabled(true);
            ctpage.setVisibility(View.VISIBLE);
            mtoolbar.setSubtitle("Caretaker page");

        }
        else{
            profileImage.setImageResource(R.drawable.pwdstatus);
            ctpage.setVisibility(View.INVISIBLE);
            ctpage.setEnabled(false);
            pwdpage.setEnabled(true);
            pwdpage.setVisibility(View.VISIBLE);
            mtoolbar.setSubtitle("pwd page");
        }
    }

    public void goManage(View v){
        Intent i = new Intent(this, CaretakerManage.class);
        this.startActivity(i);
    }

    // Gallery to be accessed here
    public void goUpload(View view){
        Intent myIntent = new Intent(MainActivity.this, CaretakerUploading.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void goStat (View view){
        //Intent myIntent = new Intent(MainActivity.this, CaretakerMain.class);
       // MainActivity.this.startActivity(myIntent);
    }
    public void goGallery (View view){

    }
    public void goPeople (View view){
        Intent myIntent = new Intent(MainActivity.this, peopleActivity.class);
        MainActivity.this.startActivity(myIntent);

    }
    public void goSettingActivity(View v){
        Intent myIntent = new Intent(MainActivity.this, Setting.class);
        MainActivity.this.startActivity(myIntent);
    }

}
