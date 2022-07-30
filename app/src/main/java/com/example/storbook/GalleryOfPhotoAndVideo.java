package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GalleryOfPhotoAndVideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_of_photo_and_video);
    }

    public void onBackClick(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
        this.finish();
    }

    public void onPhotoClick (View v){
        Intent myIntent = new Intent(this, GalleryFinal.class);
        this.startActivity((myIntent));
        this.finish();
    }
    public void onVideoClick (View v){
        Intent myIntent = new Intent(this, VideoGallery.class);
        this.startActivity((myIntent));
        this.finish();
    }
}