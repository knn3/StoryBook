package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CaretakerManage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_manage);
    }

    //back button
    public void onBackClick(View v) {
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }

    public void manageFamily(View view) {
        Intent i = new Intent(this, FamilyMemberMainPage.class);
        this.startActivity(i);

    }

    public void manageFriends(View view) {
        Intent i = new Intent(this, friends_page.class);
        this.startActivity(i);

    }

    public void manageGallery(View view){
        Intent intent = new Intent(this, ImagesActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void manageStat(View view){
        Intent intent = new Intent(this, CaretakerStat.class);
        this.startActivity(intent);
        finish();
    }

}