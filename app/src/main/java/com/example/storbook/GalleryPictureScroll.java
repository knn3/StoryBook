package com.example.storbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GalleryPictureScroll extends AppCompatActivity implements ImagePwdAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ImagePwdAdapter mAdapter;

    private FirebaseStorage mStorage;
    private FirebaseUser user;
    private FirebaseFirestore mDatabaseRef;
    private List<ImageUrls> mImgUrl;
    private String fileName;
    private String belonged;
    ArrayList<String> mImgUrls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        // set up recycler view
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mImgUrl = new ArrayList<>();

        mAdapter = new ImagePwdAdapter(GalleryPictureScroll.this, mImgUrl);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(GalleryPictureScroll.this);

        // get firebase instances
        mStorage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseFirestore.getInstance();

        // get array of imageUrls


        mImgUrls = ((global) this.getApplication()).picutreUrls;

        for(String imgUrl : mImgUrls) {
            ImageUrls imageUrl = new ImageUrls(imgUrl);
            mImgUrl.add(imageUrl);
        }
    }

    // When click on image go to that image and increment the time clicked
    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(GalleryPictureScroll.this, GalleryFinal.class);
        i.putExtra("position", position);
        if(!((global)this.getApplication()).isCaretaker()) {
            ((global) this.getApplication()).updatePictureTimeClicked(position);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CANADA);
            Date now = new Date();
            String time = formatter.format(now);
            ((global) this.getApplication()).updateRecentTimeClickedforPicture(position, time);
        }
        startActivity(i);
    }


    @Override
    public void onDeleteClick(int position) {
        Toast.makeText(this, "Not authorized", Toast.LENGTH_SHORT).show();
    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }

}