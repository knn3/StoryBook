package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.storbook.databinding.ActivityCaretakerUploadingBinding;
import com.example.storbook.databinding.ActivityFriendMemberCreatePageBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class FriendMemberCreatePage extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    Button addFRbtn;
    EditText mFriendMemberName, mFriendMemberEmail, mFriendMemberInfo;
    String downloadedUri;
    ProgressDialog progressDialog;
    Uri imageUri, homeUri;
    StorageReference storageReference;
    ImageButton Avatar;
    boolean isAvatarset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_member_create_page);

        // Initialize database
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Interactive part initialize
        mFriendMemberName = (EditText) findViewById(R.id.FriendNumberName);
        mFriendMemberEmail = (EditText)findViewById(R.id.FriendNumberEmail);
        mFriendMemberInfo = (EditText)findViewById(R.id.FriendNumberInfo);
        addFRbtn = (Button) findViewById(R.id.addFRbtn);
        Avatar = (ImageButton)findViewById(R.id.Avatarbtn);
        homeUri = Uri.parse("android.resource://" + getPackageName() + "/drawable/" + "clicktouploadavatar");
        Avatar.setImageURI(homeUri);
        isAvatarset = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!isAvatarset)
        {Avatar.setImageURI(homeUri);}
    }

    public void uploadFR(View v){
        String FriendMemberName = mFriendMemberName.getText().toString();
        String FriendMemberEmail = mFriendMemberEmail.getText().toString();
        String FriendMemberInfo = mFriendMemberInfo.getText().toString();


        // Information can be empty
        if (FriendMemberInfo.isEmpty()){FriendMemberInfo = "No Information.";}


        // Preconditions
        boolean noDuplicated = true;
        // No duplication allowed
        ArrayList<String> FrNames = ((global) this.getApplication()).getFMnames();
        for (int i = 0; i < FrNames.size(); i++) {
            if (FrNames.get(i).equals(FriendMemberName)) {
                noDuplicated = false;
                Toast.makeText(FriendMemberCreatePage.this, "Friend member with the same name already exists", Toast.LENGTH_SHORT).show();
            }
        }
        // Do nothing if one of the field is empty
        if (FriendMemberName.isEmpty()|| FriendMemberEmail.isEmpty() && noDuplicated) {
            Toast.makeText(FriendMemberCreatePage.this, "Name and Email cannot be empty", Toast.LENGTH_SHORT).show();
        }

        // The case If no avatar selected only the other information will be sent to the database
        else if (isAvatarset == false && noDuplicated) {
            downloadedUri = "";
            FriendMember newFR = new FriendMember(FriendMemberName, FriendMemberEmail, FriendMemberInfo, "https://firebasestorage.googleapis.com/v0/b/cmpt-276-storybook.appspot.com/o/images%2FCleanShot%202022-07-20%20at%2013.12.37%402x.png?alt=media&token=771c7d59-17c2-4538-ad76-c0ab54a5d0de");
            Map<String, Object> friendMember = new HashMap<>();
            friendMember.put("FRName", FriendMemberName);
            friendMember.put("FREmail", FriendMemberEmail);
            friendMember.put("Avatar", downloadedUri);
            friendMember.put("FRInfo", FriendMemberInfo);
            db.collection("users")
                    .document(fAuth.getUid()).collection("FriendMember").document(FriendMemberName).set(friendMember).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("db", "Upload a friend member without avatar to the firestore");
                                updateLocalList(newFR);
                            }
                            else{
                                Toast.makeText(FriendMemberCreatePage.this, "Fail to Upload a friend member without avatar to cloud", Toast.LENGTH_SHORT).show();
                                Log.d("db", "Fail to Upload a friend member without avatar to the firestore");
                            }
                        }
                    });
            Toast.makeText(FriendMemberCreatePage.this, "New Friend Member Created without avatar!", Toast.LENGTH_SHORT).show();
            // End actions
            isAvatarset = false;
            Avatar.setImageURI(homeUri);
            mFriendMemberName.getText().clear();
            mFriendMemberEmail.getText().clear();
            mFriendMemberInfo.getText().clear();
        }
        // The case avatar is selected
        else if(noDuplicated){
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading.....");
            progressDialog.show();

            // To get the file name as the current time
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
            Date now = new Date();
            String fileName = formatter.format(now);

            storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);

            // uploading to firestore and cloud storage
            UploadTask uploadTask = storageReference.putFile(imageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    //handle progressDialog
                    Avatar.setImageURI(null);
                    //Toast.makeText(FriendMemberCreatePage.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        Avatar.setImageURI(homeUri); // change the hint picture back after the uploading tab
                    }

                    // Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        String FriendMemberInfo = mFriendMemberInfo.getText().toString();
                        if (FriendMemberInfo.isEmpty()){FriendMemberInfo = "No Information.";}
                        // get download Url
                        Uri downloadUri = task.getResult();
                        downloadedUri = downloadUri.toString();

                        FriendMember newFR = new FriendMember(FriendMemberName, FriendMemberEmail, FriendMemberInfo, downloadedUri);
                        //put the friend member into the data base.
                        Map<String, Object> friendMember = new HashMap<>();
                        friendMember.put("FRName", FriendMemberName);
                        friendMember.put("FREmail", FriendMemberEmail);
                        friendMember.put("Avatar", downloadedUri);
                        friendMember.put("FRInfo", FriendMemberInfo);
                        // Put the new friend member into cloud
                        db.collection("users")
                                .document(fAuth.getUid()).collection("FriendMember").document(FriendMemberName).set(friendMember).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("db", "Upload a friend member with avatar to the firestore");
                                            updateLocalList(newFR);
                                        }
                                        else{
                                            Toast.makeText(FriendMemberCreatePage.this, "Fail to Upload to firestore", Toast.LENGTH_SHORT).show();
                                            Log.d("db", "Fail to Upload a friend member with avatar to the firestore");
                                        }
                                    }
                                });
                        Toast.makeText(FriendMemberCreatePage.this, "New Friend Member Created!", Toast.LENGTH_SHORT).show();
                    }
                    // The case where the upload is unsuccessful
                    else
                    {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(FriendMemberCreatePage.this, "Failed to Upload, check Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            // End actions
            isAvatarset = false;
            Avatar.setImageURI(homeUri);
            mFriendMemberName.getText().clear();
            mFriendMemberEmail.getText().clear();
        }
        //((globalfr) this.getApplication()).refreshFRlist();
    }

    public void selectImage(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            Avatar.setImageURI(imageUri);
            isAvatarset = true;
        }
        else{
            Avatar.setImageURI(homeUri);
            isAvatarset = false;
        }
    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, FriendMemberMainPage.class);
        this.startActivity(myIntent);
    }

    public void updateLocalList (FriendMember newFR){
        ((global) this.getApplication()).AllFRembers.add(newFR);
    }

}