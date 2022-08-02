package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ManageVideoAndPhotoPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_video_and_photo_page);
    }

    public void onBackClick(View v){
        Intent myIntent = new Intent(this, CaretakerManage.class);
        this.startActivity(myIntent);
        this.finish();
    }

    public void onPhotoClick (View v){
        Intent myIntent = new Intent(this, ImagesActivity.class);
        this.startActivity((myIntent));
        this.finish();
    }
    public void onVideoClick (View v){
        Intent myIntent = new Intent(this, VideoActivity.class);
        this.startActivity((myIntent));
        this.finish();
    }
}