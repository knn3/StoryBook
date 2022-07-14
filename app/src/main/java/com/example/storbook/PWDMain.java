package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PWDMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwdmain);
    }
    public void onGalleryClicked(View view){
        Intent myIntent = new Intent(PWDMain.this, GalleryView.class);
        PWDMain.this.startActivity(myIntent);
    }
    public void onPeopleClicked(View view){
        Intent myIntent = new Intent(PWDMain.this, GalleryView.class);
        PWDMain.this.startActivity(myIntent);
    }
}