package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class VideoGallery extends AppCompatActivity {

    private ArrayList<String> mVideoUrl;
    private ArrayList<String> mVideoTitle;
    private ArrayList<String> mVideoDesc;
    int currentEntry;
    int sizeOfList;
    VideoView videoView;
    TextView title;
    TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);


        currentEntry = 0;
        Intent intent = this.getIntent();
        // Get the carried out information
        if (intent != null) {
            currentEntry = intent.getIntExtra("position",0);
        }

        title = (TextView) findViewById(R.id.titleView2);
        desc = (TextView) findViewById(R.id.descView2);

        mVideoUrl = ((global) this.getApplication()).videoUrls;
        mVideoTitle = ((global) this.getApplication()).videoTitles;
        mVideoDesc = ((global) this.getApplication()).videoDescriptions;
        sizeOfList = mVideoUrl.size();

        videoView = findViewById(R.id.videoView2);

        if(sizeOfList > 0) {
            videoView.setVideoURI(Uri.parse(mVideoUrl.get(currentEntry)));
            title.setText(mVideoTitle.get(currentEntry));
            desc.setText(mVideoDesc.get(currentEntry));

            //        add playback video
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
            videoView.start();
        }
        else{
            Toast.makeText(this, "No video uploaded!", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(this, MainActivity.class);
            this.startActivity(myIntent);
        }
    }

    public void onBackClick(View v){
        Intent myIntent = new Intent(this, GalleryOfPhotoAndVideo.class);
        this.startActivity(myIntent);
    }

//    move to last video
    public void onLeftClick(View v){
        currentEntry = currentEntry -1;
        if(currentEntry<0) {
            currentEntry = sizeOfList - 1;
        }

        videoView.setVideoURI(Uri.parse(mVideoUrl.get(currentEntry)));
        title.setText(mVideoTitle.get(currentEntry));
        desc.setText(mVideoDesc.get(currentEntry));

        //        add playback video
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();

    }

    // move to next video
    public void onRightClick(View v){
        currentEntry = currentEntry +1;
        if(currentEntry>=sizeOfList){
            currentEntry = 0;
        }

        videoView.setVideoURI(Uri.parse(mVideoUrl.get(currentEntry)));
        title.setText(mVideoTitle.get(currentEntry));
        desc.setText(mVideoDesc.get(currentEntry));

//        add playback video
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();

    }
}