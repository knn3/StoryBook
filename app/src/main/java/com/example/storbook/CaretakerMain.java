package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class CaretakerMain extends AppCompatActivity {

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_main);

        logout = findViewById(R.id.logout); // R.id.logout : name of the logout button

        // click on Log Out button will logout and redirect to Main page
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CaretakerMain.this, InitializeActivity.class));
                finish();
            }
        });
    }

    // click on Manage to edit or view statistics
    public void onCtManageClick(View view){
        startActivity(new Intent(CaretakerMain.this, ImagesActivity.class));
    }

    // click on Upload Media button will redirect to uploading page
    public void onCtUploadClick(View view){
        startActivity(new Intent(CaretakerMain.this, CaretakerUploading.class));
    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }
}