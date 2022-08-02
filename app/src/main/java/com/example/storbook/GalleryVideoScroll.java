package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GalleryVideoScroll extends AppCompatActivity implements VideoPwdAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private VideoPwdAdapter mAdapter;

    private FirebaseStorage mStorage;
    private FirebaseUser user;
    private FirebaseFirestore mDatabaseRef;

    private ArrayList<VideoUrls> mVidUrl;
    ArrayList<String> mVidUrls;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Toolbar bar = findViewById(R.id.toolbar);
        bar.setSubtitle("Video Gallery");

        mStorage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.recycler_view_for_edit);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager((new LinearLayoutManager(this)));

        mVidUrl = new ArrayList<>();

        mAdapter = new VideoPwdAdapter(GalleryVideoScroll.this, mVidUrl);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(GalleryVideoScroll.this);

        mVidUrls = ((global) this.getApplication()).videoUrls;

        for(String vidUrl : mVidUrls) {
            VideoUrls videoUrl = new VideoUrls(vidUrl);
            mVidUrl.add(videoUrl);
        }
    }

    public void onItemClick(int position) {
        Intent i = new Intent(GalleryVideoScroll.this, VideoGallery.class);
        i.putExtra("position", position);
        if(!((global)this.getApplication()).isCaretaker()) {
            ((global) this.getApplication()).updateVideoTimeClicked(position);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CANADA);
            Date now = new Date();
            String time = formatter.format(now);
            ((global) this.getApplication()).updateRecentTimeClickedforVideo(position, time);
        }
        startActivity(i);
    }

    @Override
    public void onDeleteClick(int position) {

    }
    public void deleteLocal(int position){
        ((global) this.getApplication()).videoUrls.remove(position);
    }
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, GalleryOfPhotoAndVideo.class);
        this.startActivity(myIntent);
    }
}