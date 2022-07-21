package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class peopleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        Toolbar mtoolbar=findViewById(R.id.toolbar);

        mtoolbar.setSubtitle("people activity");

    }

    public void onfamilyClicked(View view){
        Intent i = new Intent(this, FamilyMemberMainPage.class);
        this.startActivity(i);

    }
    public void onfriendClicked(View view){
        Intent i = new Intent(this, friends_page.class);
        this.startActivity(i);

    }
}