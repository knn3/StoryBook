package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FamilyMemberMainPage extends AppCompatActivity {
    FloatingActionButton addFM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_member_main_page);

        addFM = (FloatingActionButton)findViewById(R.id.AddFM);
        Intent i = new Intent(this, FamilyMemberCreatePage.class);
        addFM.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The add new family member option is only available for caretakers
        if (((global) this.getApplication()).isCaretaker()){
            addFM.setVisibility(View.VISIBLE);
            addFM.setEnabled(true);
        }
        else{
            addFM.setVisibility(View.INVISIBLE);
            addFM.setEnabled(false);
        }
    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, pwd_activity.class);
        this.startActivity(myIntent);
    }
}