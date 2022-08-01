package com.example.storbook;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// ALL GLOBAL variables will be stored in this section

public class global extends Application {

    private FirebaseUser user;
    private FirebaseFirestore mDatabaseRef;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    StorageReference storageReference;


    /////////////////////****************
    // Shared preference All local changes can be stored in here!!!
    public void writeIntToSharedPreference(String TargetName, int value){
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.storbook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TargetName, value);
        editor.apply();
    }

    public void writeStringToSharedPreference(String TargetName, String value){
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.storbook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TargetName, value);
        editor.apply();
    }

    public int readIntFromSharedPreference(String TargetName, int defaultValue){
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.storbook", Context.MODE_PRIVATE);
        int integer = sharedPreferences.getInt(TargetName, defaultValue);
        return integer;
    }

    public String readStringFromSharedPreference(String TargetName) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.storbook", Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(TargetName, "");
        return string;
    }

    /////////////////////****************
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

                                    // "No avatar" avatar if no avatar
                                    String thispic;
                                    if (aFM.get("Avatar").toString().equals("")) {
                                        thispic = "https://firebasestorage.googleapis.com/v0/b/cmpt-276-storybook.appspot.com/o/images%2FCleanShot%202022-07-20%20at%2013.12.37%402x.png?alt=media&token=771c7d59-17c2-4538-ad76-c0ab54a5d0de";
                                    }

                                    else{
                                        thispic = aFM.get("Avatar").toString();
                                    }
                                    FamilyMember familyMember = new FamilyMember(aFM.get("FMName").toString(),aFM.get("FMRelation").toString(),aFM.get("FMInfo").toString(),thispic);
                                    if (document.getData().get("media") != null) {
                                        List<String> imgUrls = (List<String>) document.get("media");
                                        familyMember = new FamilyMember(aFM.get("FMName").toString(),aFM.get("FMRelation").toString(),aFM.get("FMInfo").toString(),thispic, imgUrls);
                                    }
                                    AllFMembers.add(familyMember);
                                }
                                Log.d("db", "Family member refreshed from database");
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

    public ArrayList<String> getFMnames(){
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

    // Helper function to find the family member
    public FamilyMember findFamilyMemberWithName(String name){
        for (int i = 0; i < this.AllFMembers.size(); i++){
            if (this.AllFMembers.get(i).name.equals(name)){
                return this.AllFMembers.get(i);
            }
        }
        return this.AllFMembers.get(0);
    }
    /////////////////////
    // Urls for picture linked to one family member

    ArrayList<String> pictureUrlsforFM;

    public void getpictureUrlsforFM(String targetName){
        this.pictureUrlsforFM = new ArrayList<>();
        // get if the target has any related media
        if (findFamilyMemberWithName(targetName).relatedMedia != null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            mDatabaseRef = FirebaseFirestore.getInstance();
            mDatabaseRef.collection("users")
                    .document(user.getUid())
                    .collection("Media")
                    .whereEqualTo("Type", "picture")
                    .whereEqualTo("Belonged", targetName)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Map aMedia = document.getData();
                                        pictureUrlsforFM.add(aMedia.get("Url").toString());
                                    }
                                    Log.d("db", "Related FM picture url refreshed");
                                } else {
                                    Toast.makeText(getApplicationContext(), "Refresh media data from online failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }

    ////////////////////////
    // A both local and database family member modification
    // Precondition: No field is empty
    public void familyMembermodification(String newname, String newInfo, String newRelation, int position, Uri newAvatar){
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        // Upload the new avatar if it is set
        if (newAvatar != null){

            // To get the file name as the current time
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
            Date now = new Date();
            String fileName = formatter.format(now);

            storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);

            // uploading to firestore and cloud storage
            UploadTask uploadTask = storageReference.putFile(newAvatar);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    //handle progressDialog
                    //Toast.makeText(FamilyMemberCreatePage.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();

                    // Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Log.d("db", "Image uploaded to cloud storage");
                        // get download Url
                        Uri downloadUri = task.getResult();
                        String downloadedUri = downloadUri.toString();
                        familyMembermodificationW(newname, newInfo, newRelation, position, downloadedUri);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Failed to Upload, check Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



        // if avatar is not changed
        else {
            familyMembermodificationW(newname, newInfo, newRelation, position, this.AllFMembers.get(position).mImageUrl);
        }
    }

    public void familyMembermodificationW(String newname, String newInfo, String newRelation, int position, String newAvatar){

        // The case where the name is not changed
        if (this.AllFMembers.get(position).name == newname) {

            // Database modification
            db.collection("users")
                    .document(user.getUid())
                    .collection("Media")
                    .document(newname)
                    .update("FMRelation",newRelation,
                            "FMInfo", newInfo,
                            "Avatar",newAvatar).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("db", "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("db", "Error updating document", e);
                        }
                    });
            // Local modification
            this.AllFMembers.get(position).info = newInfo;
            this.AllFMembers.get(position).relationship = newRelation;
            this.AllFMembers.get(position).mImageUrl = newAvatar;
        }
        // The case where the name is changed so the whole document needs to be changed
        else {
            // First delete the old doc
            deleteFamilyMember(this.AllFMembers.get(position).name, -1);

            // Then add the modified FM document back
            Map<String, Object> familyMember = new HashMap<>();
            familyMember.put("FMName", newname);
            familyMember.put("FMRelation", newRelation);
            familyMember.put("Avatar", newAvatar);
            familyMember.put("FMInfo", newInfo);
            familyMember.put("media", this.AllFMembers.get(position).relatedMedia);
            // Put the new family member into cloud
            db.collection("users")
                    .document(user.getUid()).collection("FamilyMember").document(newname).set(familyMember).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("db", "Family Member Changed on firestore");
                                Toast.makeText(getApplicationContext(), "Family Member Changed!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Fail to Upload to firestore", Toast.LENGTH_SHORT).show();
                                Log.d("db", "Fail to Upload a family member with avatar to the firestore");
                            }
                        }
                    });

            updateRelativeFmMeida(newname, position);

            // Local modification
            this.AllFMembers.get(position).info = newInfo;
            this.AllFMembers.get(position).relationship = newRelation;
            this.AllFMembers.get(position).mImageUrl = newAvatar;
            this.AllFMembers.get(position).name = newname;
        }
    }

    // Set the position to -1 for temporary deletion
    public void deleteFamilyMember(String targetName, int position){
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        // Delete the FM in firestore
        db.collection("users").document(user.getUid()).collection("FamilyMember").document(targetName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("db", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("db", "Error deleting document", e);
                        Toast.makeText(getApplicationContext(), "Family Member Cannot be deleted", Toast.LENGTH_SHORT).show();
                    }
                });

        // True deletion
        if (position != -1){
            if (this.AllFMembers.get(position).relatedMedia != null) {
                List<String> media = this.AllFMembers.get(position).relatedMedia;
                for (int i = 0; i < media.size(); i++) {
                    db.collection("users").document(user.getUid()).collection("Media").document(media.get(i))
                            .update("Belonged", "").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("db", "FM related media updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("db", "Error updating document", e);
                                }
                            });
                }
            }
            this.AllFMembers.remove(position);
        }
    }

    public void updateRelativeFmMeida(String newName, int position){
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        // Update all the relative media if have them
        if (this.AllFMembers.get(position).relatedMedia != null) {
            List<String> media = this.AllFMembers.get(position).relatedMedia;
            for (int i = 0; i < media.size(); i++) {
                db.collection("users").document(user.getUid()).collection("Media").document(media.get(i))
                        .update("Belonged", newName).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("db", "FM related media updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("db", "Error updating document", e);
                            }
                        });
            }
        }
    }

}