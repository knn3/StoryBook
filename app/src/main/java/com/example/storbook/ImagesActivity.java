package com.example.storbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private FirebaseUser user;
    private FirebaseFirestore mDatabaseRef;
    private List<ImageUrls> mImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mImgUrl = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseFirestore.getInstance();

        mDatabaseRef.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        List<String> imgUrls = (List<String>) document.get("media");
                        for (String imgUrl : imgUrls){
                            ImageUrls imageUrl = new ImageUrls(imgUrl);
                            mImgUrl.add(imageUrl);
                        }

                        mAdapter = new ImageAdapter(ImagesActivity.this, mImgUrl);

                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
            }
        });
    }
}