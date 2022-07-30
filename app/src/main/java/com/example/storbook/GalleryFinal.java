package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GalleryFinal extends AppCompatActivity {

    private List<String> mImgUrl;
    private List<String> mImgTitle;
    private List<String> mImgDesc;
    int currentEntry = 0;
    int sizeOfList;
    ImageView imageView;
    TextView title;
    TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_final);
        imageView = findViewById(R.id.imageView2);
        //Get URLs

        title = (TextView) findViewById(R.id.titleView);
        desc = (TextView) findViewById(R.id.descView);

        mImgUrl = ((global) this.getApplication()).picutreUrls;
        mImgTitle = ((global) this.getApplication()).picutreTitles;
        mImgDesc = ((global) this.getApplication()).picutreDescriptions;
        sizeOfList = mImgUrl.size();

        if(sizeOfList > 0) {
            Glide.with(this).load(mImgUrl.get(0)).into(imageView);
            title.setText(mImgTitle.get(0));
            desc.setText(mImgDesc.get(0));
        }
        else{
            Toast.makeText(this, "Error: No photos uploaded!", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(this, MainActivity.class);
            this.startActivity(myIntent);
        }
    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, GalleryOfPhotoAndVideo.class);
        this.startActivity(myIntent);
    }

    public void onLeftClick(View v){
        currentEntry = currentEntry -1;
        if(currentEntry<0){
            currentEntry = sizeOfList-1;
            Glide.with(this).load(mImgUrl.get(currentEntry)).into(imageView);
            title.setText(mImgTitle.get(currentEntry));
            desc.setText(mImgDesc.get(currentEntry));
        }
        else{
            Glide.with(this).load(mImgUrl.get(currentEntry)).into(imageView);
            title.setText(mImgTitle.get(currentEntry));
            desc.setText(mImgDesc.get(currentEntry));
        }
    }

    public void onRightClick(View v){
        currentEntry = currentEntry +1;
        if(currentEntry>=sizeOfList){
            currentEntry = 0;
            Glide.with(this).load(mImgUrl.get(currentEntry)).into(imageView);
            title.setText(mImgTitle.get(currentEntry));
            desc.setText(mImgDesc.get(currentEntry));
        }
        else{
            Glide.with(this).load(mImgUrl.get(currentEntry)).into(imageView);
            title.setText(mImgTitle.get(currentEntry));
            desc.setText(mImgDesc.get(currentEntry));
        }
    }
}