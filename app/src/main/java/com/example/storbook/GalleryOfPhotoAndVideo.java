package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GalleryOfPhotoAndVideo extends AppCompatActivity {

    Button stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_of_photo_and_video);
        Toolbar bar = findViewById(R.id.toolbar);
        stat = findViewById(R.id.Statisticbtn);
        if (((global)this.getApplication()).isCaretaker){
            bar.setSubtitle("Statistic Gallery Page");
            stat.setVisibility(View.VISIBLE);
            stat.setEnabled(true);
        }
        else{
            bar.setSubtitle("Gallery Page");
            stat.setVisibility(View.INVISIBLE);
            stat.setEnabled(false);
        }
    }

    public void onBackClick(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
        this.finish();
    }

    public void onPhotoClick (View v){
        Intent myIntent = new Intent(this, GalleryPictureScroll.class);
        this.startActivity((myIntent));
        this.finish();
    }
    public void onVideoClick (View v){
        Intent myIntent = new Intent(this, GalleryVideoScroll.class);
        this.startActivity((myIntent));
        this.finish();
    }
    public void onStatClick (View v){
        Intent myIntent = new Intent(this, CaretakerStat.class);
        this.startActivity((myIntent));
        this.finish();
    }
}