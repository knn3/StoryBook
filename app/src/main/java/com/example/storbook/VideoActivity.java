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

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity implements VideoAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;

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
        bar.setSubtitle("Hold to Delete Video");

        mStorage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.recycler_view_for_edit);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager((new LinearLayoutManager(this)));

        mVidUrl = new ArrayList<>();

        mAdapter = new VideoAdapter(VideoActivity.this, mVidUrl);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(VideoActivity.this);

        mVidUrls = ((global) this.getApplication()).videoUrls;

        for(String vidUrl : mVidUrls) {
            VideoUrls videoUrl = new VideoUrls(vidUrl);
            mVidUrl.add(videoUrl);
        }
    }

    public void onItemClick(int position) {
        Toast.makeText(this, "Hold to edit", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        VideoUrls selectedItem = mVidUrl.get(position);
        String selectedImg = selectedItem.getVideoUrl();

        // reference to firestore and use url to delete in the array using arrayRemove()
        mDatabaseRef.collection("users").document(user.getUid()).update("media", FieldValue.arrayRemove(selectedImg));

        //
        mDatabaseRef.collection("users").document(user.getUid()).collection("Media").document(((global)this.getApplication()).videoFilename.get(position)).delete();

        if (!((global)this.getApplication()).videoBelonged.get(position).equals("")){
            mDatabaseRef.collection("users").document(user.getUid()).collection("FamilyMember").document(((global)this.getApplication()).videoBelonged.get(position)).update("media", FieldValue.arrayRemove(((global)this.getApplication()).videoFilename.get(position)));
        }


        // delete video in firebase cloud storage
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedImg);
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(VideoActivity.this, "Successfully Deleted from Storage", Toast.LENGTH_SHORT).show();
                deleteLocal(position);
                // after deleted, refresh the activity to render the videos again
                finish();
                startActivity(new Intent(VideoActivity.this, VideoActivity.class));
            }
        });
    }
    public void deleteLocal(int position){
        ((global) this.getApplication()).videoUrls.remove(position);
    }
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, ManageVideoAndPhotoPage.class);
        this.startActivity(myIntent);
    }
}