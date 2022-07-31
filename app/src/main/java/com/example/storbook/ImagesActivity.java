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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private FirebaseStorage mStorage;
    private FirebaseUser user;
    private FirebaseFirestore mDatabaseRef;
    private List<ImageUrls> mImgUrl;
    private String fileName;
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

        mAdapter = new ImageAdapter(ImagesActivity.this, mImgUrl);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ImagesActivity.this);

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

        mDatabaseRef.collection("users").document(user.getUid()).collection("Media").whereEqualTo("Url", selectedImg).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map aMedia =  document.getData();
                        fileName = aMedia.get("FileName").toString();

                        mDatabaseRef.collection("users").document(user.getUid()).collection("Media").document(fileName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ImagesActivity.this, "Successfully Deleted from Media Document", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });



        // delete image in firebase cloud storage
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedImg);
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ImagesActivity.this, "Successfully Deleted from Storage", Toast.LENGTH_SHORT).show();

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