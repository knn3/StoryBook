package com.example.storbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.storbook.databinding.ActivityFamilyMemberMainPageBinding;
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

public class FamilyMemberMainPage extends AppCompatActivity {

    ActivityFamilyMemberMainPageBinding binding;
    private FirebaseUser user;
    private FirebaseFirestore mDatabaseRef;
    //List<ImageUrls> mImgUrl;
    List<String> mImgUrl;
    List<String> mName;
    List<String> mRelation;
    List<String> mInfo;

    // This mode defines the behaviour of this page: 0 = default view mode, 1 = choose name to uploading page
    int mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamilyMemberMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Refresh the page with the stored information
        if (!((global) this.getApplication()).AllFMembers.isEmpty()) {
            FM_listAdapter listAdapter = new FM_listAdapter(FamilyMemberMainPage.this, ((global) this.getApplication()).AllFMembers);
            binding.FamilyMemberlist.setAdapter(listAdapter);
        }

        Intent i = new Intent(this, FamilyMemberCreatePage.class);
        binding.AddFM.setOnClickListener(new View.OnClickListener(){
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

        //Refresh the page with the stored information
        if (!((global) this.getApplication()).AllFMembers.isEmpty()) {
            FM_listAdapter listAdapter = new FM_listAdapter(FamilyMemberMainPage.this, ((global) this.getApplication()).AllFMembers);
            binding.FamilyMemberlist.setAdapter(listAdapter);
        }
        // The add new family member option is only available for caretakers and in view mode
        if (((global) this.getApplication()).isCaretaker() && mode == 0){
            binding.AddFM.setVisibility(View.VISIBLE);
            binding.AddFM.setEnabled(true);
        }
        else{
            binding.AddFM.setVisibility(View.INVISIBLE);
            binding.AddFM.setEnabled(false);
        }
/*
        // Retrieve the family members from the database
        mImgUrl = new ArrayList<>();
        mName = new ArrayList<>();
        mRelation = new ArrayList<>();
        mInfo = new ArrayList<>();


        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseFirestore.getInstance();



        // Add those family member to the list
        ArrayList<FamilyMember> FMarray = new ArrayList<>();

        mDatabaseRef.collection("users")
                .document(user.getUid())
                .collection("FamilyMember")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map aFM =  document.getData();
                                    //Log.d("db", document.getId() + " => " + aFM);
                                    //Log.d("db", document.getId() + " => " + document.getData());
                                    //Get avatar
                                    //String noavatar = "";
                                    // if (!aFM.get("Avatar").toString().equals(noavatar)){
                                    //    ImageUrls imageUrl = new ImageUrls(aFM.get("Avater").toString());
                                    //     mImgUrl.add(imageUrl);
                                    // }
                                    //if the avatar is not uploaded use the default picture as avatar
                                    //else{
                                    //   ImageUrls imageUrl = new ImageUrls("https://firebasestorage.googleapis.com/v0/b/cmpt-276-storybook.appspot.com/o/images%2FCleanShot%202022-07-20%20at%2013.12.37%402x.png?alt=media&token=771c7d59-17c2-4538-ad76-c0ab54a5d0de");
                                    //    mImgUrl.add(imageUrl);
                                    // }
                                    String thispic;
                                    if (aFM.get("Avatar").toString().equals("")) {
                                        mImgUrl.add("https://firebasestorage.googleapis.com/v0/b/cmpt-276-storybook.appspot.com/o/images%2FCleanShot%202022-07-20%20at%2013.12.37%402x.png?alt=media&token=771c7d59-17c2-4538-ad76-c0ab54a5d0de");
                                        thispic = "https://firebasestorage.googleapis.com/v0/b/cmpt-276-storybook.appspot.com/o/images%2FCleanShot%202022-07-20%20at%2013.12.37%402x.png?alt=media&token=771c7d59-17c2-4538-ad76-c0ab54a5d0de";
                                    }
                                    else{
                                        mImgUrl.add(aFM.get("Avatar").toString());
                                        thispic = aFM.get("Avatar").toString();
                                    }
                                    //Get name
                                    mName.add(aFM.get("FMName").toString());
                                    //Get relation
                                    mRelation.add(aFM.get("FMRelation").toString());
                                    //Get Info (No information implemented so far)
                                    mInfo.add(aFM.get("FMInfo").toString());
                                    FamilyMember familyMember = new FamilyMember(aFM.get("FMName").toString(),aFM.get("FMRelation").toString(),aFM.get("FMInfo").toString(),thispic);
                                    FMarray.add(familyMember);
                                    // Refresh the list for each iteration
                                    //FM_listAdapter listAdapter = new FM_listAdapter(FamilyMemberMainPage.this,FMarray);
                                    //binding.FamilyMemberlist.setAdapter(listAdapter);
                                }
                            }
                            else {
                                Toast.makeText(FamilyMemberMainPage.this, "Failed to Retrieve from database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

 */

        binding.FamilyMemberlist.setClickable(true);
        //Set each item on list clickable and when click it will direct to the family member's page

        ArrayList<String> FmNames = ((global) this.getApplication()).getFMnamees();
        ArrayList<String> FmRelations = ((global) this.getApplication()).getFMrelations();
        ArrayList<String> FmInfos = ((global) this.getApplication()).getFMinfos();
        ArrayList<String> FmAvatars = ((global) this.getApplication()).getFMavatars();

        binding.FamilyMemberlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // View the Family member page for the clicked the family member
                if (mode == 0) {
                    Intent i = new Intent(FamilyMemberMainPage.this, FamilyMemberPage.class);
                    i.putExtra("Name", FmNames.get(position));
                    i.putExtra("Relation", FmRelations.get(position));
                    i.putExtra("imageID", FmAvatars.get(position));
                    i.putExtra("Info", FmInfos.get(position));
                    startActivity(i);
                }

                // pass the name back to the uploading page
                else if (mode == 1){
                    Intent i = new Intent(FamilyMemberMainPage.this, CaretakerUploading.class);
                    i.putExtra("Name", FmNames.get(position));
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
    
}
