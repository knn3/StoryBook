package com.example.storbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private FirebaseUser user;
    private FirebaseFirestore mDatabaseRef;
    private List<ImageUrls> mImgUrl;

    String name;
    String relation;
    String info;
    String imageurl;
    int positionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mRecyclerView = findViewById(R.id.gallery_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mImgUrl = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseFirestore.getInstance();


        Intent intent = this.getIntent();
        // Get the carried out information
        if (intent != null){

            name = intent.getStringExtra("Name");
            relation = intent.getStringExtra("Relation");
            info = intent.getStringExtra("Info");
            imageurl = intent.getStringExtra("imageID");
            positionIndex = intent.getIntExtra("position",0);

        }


        for (String FMurl : ((global) this.getApplication()).pictureUrlsforFM){
            ImageUrls imageUrl = new ImageUrls(FMurl);
            mImgUrl.add(imageUrl);
        }
        mAdapter = new ImageAdapter(GalleryActivity.this, mImgUrl);

        mRecyclerView.setAdapter(mAdapter);
        /*
        mDatabaseRef.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        // No gallery building if no media uploaded
                        if (document.getData().get("media") != null) {
                            List<String> imgUrls = (List<String>) document.get("media");
                            for (String imgUrl : imgUrls) {
                                ImageUrls imageUrl = new ImageUrls(imgUrl);
                                mImgUrl.add(imageUrl);
                            }

                            mAdapter = new ImageAdapter(GalleryActivity.this, mImgUrl);

                            mRecyclerView.setAdapter(mAdapter);
                        }

                    }
                }
            }
        });

         */
    }

    //back button
    public void onBackClick(View v){
        Intent i = new Intent(this, FamilyMemberPage.class);
        i.putExtra("Name", name);
        i.putExtra("Relation", relation);
        i.putExtra("imageID", imageurl);
        i.putExtra("Info", info);
        i.putExtra("position", positionIndex);
        startActivity(i);
        this.startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, FamilyMemberPage.class);
        i.putExtra("Name", name);
        i.putExtra("Relation", relation);
        i.putExtra("imageID", imageurl);
        i.putExtra("Info", info);
        i.putExtra("position", positionIndex);
        startActivity(i);
        this.startActivity(i);
    }

}

