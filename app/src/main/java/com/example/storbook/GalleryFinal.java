package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GalleryFinal extends AppCompatActivity {

    private List<String> mImgUrl;
    int currentEntry = 0;
    int sizeOfList;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_final);
        imageView = findViewById(R.id.imageView2);
        //Get URLs
        mImgUrl = ((global) this.getApplication()).picutreUrls;
        sizeOfList = mImgUrl.size();

        if(sizeOfList > 0) {
            Glide.with(this).load(mImgUrl.get(0)).into(imageView);
        }
        else{
            Toast.makeText(this, "Error: No photos uploaded!", Toast.LENGTH_LONG).show();
        }
    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }

    public void onLeftClick(View v){
        currentEntry = currentEntry -1;
        if(currentEntry<0){
            currentEntry = sizeOfList-1;
            Glide.with(this).load(mImgUrl.get(currentEntry)).into(imageView);
        }
        else{
            Glide.with(this).load(mImgUrl.get(currentEntry)).into(imageView);
        }
    }

    public void onRightClick(View v){
        currentEntry = currentEntry +1;
        if(currentEntry>=sizeOfList){
            currentEntry = 0;
            Glide.with(this).load(mImgUrl.get(currentEntry)).into(imageView);
        }
        else{
            Glide.with(this).load(mImgUrl.get(currentEntry)).into(imageView);
        }
    }
}