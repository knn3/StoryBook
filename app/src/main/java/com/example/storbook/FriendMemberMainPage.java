package com.example.storbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.storbook.databinding.ActivityFriendMemberMainPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendMemberMainPage extends AppCompatActivity {

    ActivityFriendMemberMainPageBinding binding;
    private FirebaseUser user;
    private FirebaseFirestore mDatabaseRef;
    //List<ImageUrls> mImgUrl;
    List<String> mImgUrl;
    List<String> mName;

    List<String> mEmail;
    List<String> mInfo;

    // This mode defines the behaviour of this page: 0 = default view mode, 1 = choose name to uploading page
    int mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendMemberMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        Intent i = new Intent(this, FriendMemberCreatePage.class);
        binding.AddFR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        // fetch the mode from intent
        mode = 0;
        Intent intent = this.getIntent();
        // Get the carried out information
        if (intent != null){

            mode = intent.getIntExtra("Mode", 0);

        }

        Toolbar bar = findViewById(R.id.toolbar);
        if (((global)this.getApplication()).isCaretaker && mode == 0){
            bar.setSubtitle("Manage Friend");
        }
        else if (((global)this.getApplication()).isCaretaker && mode == 1){
            bar.setSubtitle("Choose Friend");
        }
        else{
            bar.setSubtitle("View Friend");
        }

        //Refresh the page with the stored information
        if (!((global) this.getApplication()).AllFRembers.isEmpty()) {
            FR_listAdapter listAdapter = new FR_listAdapter(FriendMemberMainPage.this, ((global) this.getApplication()).AllFRembers);
            binding.FriendMemberlist.setAdapter(listAdapter);
        }
        // The add new friend member option is only available for caretakers and in view mode
        if (((global) this.getApplication()).isCaretaker() && mode == 0){
            binding.AddFR.setVisibility(View.VISIBLE);
            binding.AddFR.setEnabled(true);
        }
        else{
            binding.AddFR.setVisibility(View.INVISIBLE);
            binding.AddFR.setEnabled(false);
        }
        binding.FriendMemberlist.setClickable(true);
        //Set each item on list clickable and when click it will direct to the friend member's page

        ArrayList<String> FrNames = ((global) this.getApplication()).getFRnames();
        ArrayList<String> FrEmail = ((global) this.getApplication()).getFRemails();
        ArrayList<String> FrInfos = ((global) this.getApplication()).getFRinfos();
        ArrayList<String> FrAvatars = ((global) this.getApplication()).getFRavatars();

        binding.FriendMemberlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // View the Friend member page for the clicked the friend member
                if (mode == 0) {
                    Intent i = new Intent(FriendMemberMainPage.this, FriendMemberPage.class);
                    i.putExtra("Name", FrNames.get(position));
                    i.putExtra("Email", FrEmail.get(position));
                    i.putExtra("imageID", FrAvatars.get(position));
                    i.putExtra("Info", FrInfos.get(position));
                    i.putExtra("position", position);
                    startActivity(i);
                }

                // pass the name back to the uploading page
                else if (mode == 1){
                    Intent i = new Intent(FriendMemberMainPage.this, CaretakerUploading.class);
                    i.putExtra("Name", FrNames.get(position));
                    startActivity(i);
                }

            }
        });

    }
    //back button
    public void onBackClick(View v){
        Intent myIntent;
        // Go back to the manage page if in caretaker mode and default view mode
        if (((global) this.getApplication()).isCaretaker() && mode == 0){
            myIntent = new Intent (this, CaretakerManage.class);
        }
        // Go back to the uploading page if in caretaker mode and choose name mode
        else if (((global) this.getApplication()).isCaretaker() && mode == 1) {
            myIntent = new Intent (this, CaretakerUploading.class);
        }
        // Go back to the pwd page if in pwd mode
        else {
            myIntent = new Intent(this, peopleActivity.class);
        }
        this.startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent;
        // Go back to the manage page if in caretaker mode and default view mode
        if (((global) this.getApplication()).isCaretaker() && mode == 0){
            myIntent = new Intent (this, CaretakerManage.class);
        }
        // Go back to the uploading page if in caretaker mode and choose name mode
        else if (((global) this.getApplication()).isCaretaker() && mode == 1) {
            myIntent = new Intent (this, CaretakerUploading.class);
        }
        // Go back to the pwd page if in pwd mode
        else {
            myIntent = new Intent(this, peopleActivity.class);
        }
        this.startActivity(myIntent);
    }

}
