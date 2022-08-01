//package com.example.storbook;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//
//import java.util.ArrayList;
//
//public class VideoActivity extends AppCompatActivity {
//    private RecyclerView mRecyclerView;
//    private VideoAdapter mAdapter;
//
//    private ArrayList<VideoUrls> mVidUrl;
//    ArrayList<String> mVidUrls;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video);
//
//        mRecyclerView = findViewById(R.id.recycler_view_for_edit);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager((new LinearLayoutManager(this)));
//
//        mVidUrl = new ArrayList<>();
//
//        mAdapter = new VideoAdapter(VideoActivity.this, mVidUrl);
//
//        mRecyclerView.setAdapter(mAdapter);
//
//        mVidUrls = ((global)) (this.getApplication().videoUrls);
//    }
//}