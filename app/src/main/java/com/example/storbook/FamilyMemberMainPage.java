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
    int FMcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamilyMemberMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());






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
        // The add new family member option is only available for caretakers
        if (((global) this.getApplication()).isCaretaker()){
            binding.AddFM.setVisibility(View.VISIBLE);
            binding.AddFM.setEnabled(true);
        }
        else{
            binding.AddFM.setVisibility(View.INVISIBLE);
            binding.AddFM.setEnabled(false);
        }


        // Retrieve the family members from the database
        mImgUrl = new ArrayList<>();
        mName = new ArrayList<>();
        mRelation = new ArrayList<>();
        mInfo = new ArrayList<>();
        FMcount = 0;

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
                                    FM_listAdapter listAdapter = new FM_listAdapter(FamilyMemberMainPage.this,FMarray);
                                    binding.FamilyMemberlist.setAdapter(listAdapter);
                                }
                            }
                            else {
                                Toast.makeText(FamilyMemberMainPage.this, "Failed to Retreive from database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        binding.FamilyMemberlist.setClickable(true);
        //Set each item on list clickable and when click it will direct to the family member's page
        binding.FamilyMemberlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent i = new Intent(FamilyMemberMainPage.this, FamilyMemberPage.class);
                i.putExtra("Name",mName.get(position));
                i.putExtra("Relation",mRelation.get(position));
                i.putExtra("imageID", mImgUrl.get(position));
                i.putExtra("Info", mInfo.get(position));
                startActivity(i);

            }
        });

    }
    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, pwd_activity.class);
        this.startActivity(myIntent);
    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, pwd_activity.class);
        this.startActivity(myIntent);
    }
}