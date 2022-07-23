package com.example.storbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private FirebaseStorage mStorage;
    private FirebaseUser user;
    private FirebaseFirestore mDatabaseRef;
    private List<ImageUrls> mImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        // set up recycler view
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mImgUrl = new ArrayList<>();

        mAdapter = new ImageAdapter(ImagesActivity.this, mImgUrl);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ImagesActivity.this);

        // get firebase instances
        mStorage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseFirestore.getInstance();

        // get whole document with user's UID
        mDatabaseRef.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    //get document
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        // No gallery building if no media uploaded
                        if (document.getData().get("media") != null) {
                            // direct to media field
                            List<String> imgUrls = (List<String>) document.get("media");

                            mImgUrl.clear();

                            //loop through each entry of array media to get each image url
                            for (String imgUrl : imgUrls) {
                                ImageUrls imageUrl = new ImageUrls(imgUrl);
                                mImgUrl.add(imageUrl);
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }


    // normal click on image
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Hold to edit", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        // get the url of the image clicked
        ImageUrls selectedItem = mImgUrl.get(position);
        String selectedImg = selectedItem.getUrl();

        // reference to firestore and use url to delete in the array using arrayRemove()
        mDatabaseRef.collection("users").document(user.getUid()).update("media", FieldValue.arrayRemove(selectedImg));

        // delete image in firebase cloud storage
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedImg);
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ImagesActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();

                // after deleted, refresh the activity to render the images again
                finish();
                startActivity(new Intent(ImagesActivity.this, ImagesActivity.class));
            }
        });

    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, CaretakerManage.class);
        this.startActivity(myIntent);
    }

}