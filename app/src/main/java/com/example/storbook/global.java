package com.example.storbook;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

// ALL GLOBAL variables will be stored in this section

public class global extends Application {

    private FirebaseUser user;
    private FirebaseFirestore mDatabaseRef;
    /////////////////////
    // CareTaker mode

    boolean isCaretaker = false;
    public boolean isCaretaker() {
        return isCaretaker;
    }
    public void setCaretaker(boolean caretaker) {
        isCaretaker = caretaker;
    }

    /////////////////////
    // All stored FM
    ArrayList<FamilyMember> AllFMembers;

    // Refresh FM list function
    public void refreshFMlist(){
        this.AllFMembers = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseFirestore.getInstance();
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

                                    String thispic;
                                    if (aFM.get("Avatar").toString().equals("")) {
                                        thispic = "https://firebasestorage.googleapis.com/v0/b/cmpt-276-storybook.appspot.com/o/images%2FCleanShot%202022-07-20%20at%2013.12.37%402x.png?alt=media&token=771c7d59-17c2-4538-ad76-c0ab54a5d0de";
                                    }

                                    else{
                                        thispic = aFM.get("Avatar").toString();
                                    }
                                    FamilyMember familyMember = new FamilyMember(aFM.get("FMName").toString(),aFM.get("FMRelation").toString(),aFM.get("FMInfo").toString(),thispic);
                                    AllFMembers.add(familyMember);
                                }
                                // Occurs too often so crossed out for now
                                //Toast.makeText(getApplicationContext(), "Local Family Member Refreshed from cloud!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Refresh member data from online failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public ArrayList<String> getFMnamees(){
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < this.AllFMembers.size(); i++){
            names.add(AllFMembers.get(i).name);
        }
        return names;
    }
    public ArrayList<String> getFMrelations(){
        ArrayList<String> relations = new ArrayList<>();
        for (int i = 0; i < this.AllFMembers.size(); i++){
            relations.add(AllFMembers.get(i).relationship);
        }
        return relations;
    }
    public ArrayList<String> getFMinfos(){
        ArrayList<String> infos = new ArrayList<>();
        for (int i = 0; i < this.AllFMembers.size(); i++){
            infos.add(AllFMembers.get(i).info);
        }
        return infos;
    }
    public ArrayList<String> getFMavatars(){
        ArrayList<String> avatars = new ArrayList<>();
        for (int i = 0; i < this.AllFMembers.size(); i++){
            avatars.add(AllFMembers.get(i).mImageUrl);
        }
        return avatars;
    }
    /////////////////////
    // Urls for all pictures
    ArrayList<String> picutreUrls;
    ArrayList<String> picutreTitles;
    ArrayList<String> picutreDescriptions;

    // Urls for all videos
    ArrayList<String> videoUrls;
    ArrayList<String> videoTitles;
    ArrayList<String> videoDescriptions;


    // Refresh urls
    public void refreshpictureUrls(){
        this.picutreUrls = new ArrayList<>();
        this.picutreTitles = new ArrayList<>();
        this.picutreDescriptions = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseFirestore.getInstance();
        mDatabaseRef.collection("users")
                .document(user.getUid())
                .collection("Media")
                .whereEqualTo("Type", "picture")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map aMedia =  document.getData();
                                    picutreUrls.add(aMedia.get("Url").toString());
                                    picutreTitles.add(aMedia.get("Title").toString());
                                    picutreDescriptions.add(aMedia.get("Description").toString());
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Refresh media data from online failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        // refresh for videos
        this.videoUrls = new ArrayList<>();
        this.videoTitles = new ArrayList<>();
        this.videoDescriptions = new ArrayList<>();
        mDatabaseRef.collection("users")
                .document(user.getUid())
                .collection("Media")
                .whereEqualTo("Type","video")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map aMedia =  document.getData();
                                videoUrls.add(aMedia.get("Url").toString());
                                videoTitles.add(aMedia.get("Title").toString());
                                videoDescriptions.add(aMedia.get("Description").toString());
                            }
                        }
                    }
                });
    }
    /////////////////////
    // Urls for picture linked to one family member

    public ArrayList<String> refreshpictureUrlsforFM(String targetName){
        ArrayList<String> picutreUrlsforFM = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseFirestore.getInstance();
        mDatabaseRef.collection("users")
                .document(user.getUid())
                .collection("Media")
                .whereEqualTo("Type", "picture")
                .whereEqualTo("Belonged", targetName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map aMedia =  document.getData();
                                    picutreUrlsforFM.add(aMedia.get("Url").toString());
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Refresh media data from online failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        return picutreUrlsforFM;
    }



}