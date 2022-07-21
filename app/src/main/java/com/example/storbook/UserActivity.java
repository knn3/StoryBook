package com.example.storbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth fAuth;
    Button addFRbtn;
    EditText mfriendsName,mfriendsph,mfriendse;
    String downloadedUri;
    ProgressDialog progressDialog;
    Uri imageUri, homeUri;
    StorageReference storageReference;
    ImageButton Avatar;
    boolean isAvatarset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar mtoolbar=findViewById(R.id.toolbar);

        mtoolbar.setSubtitle("friends create");

        // Initialize database
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Interactive part initialize
        mfriendsName = (EditText) findViewById(R.id.friendsname);
        mfriendsph = (EditText) findViewById(R.id.phone);
        mfriendse = (EditText) findViewById(R.id.email);
        addFRbtn = (Button) findViewById(R.id.addFRbtn);
        Avatar = (ImageButton)findViewById(R.id.Avafrbtn);
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
        String FriendsName = mfriendsName.getText().toString();
        String FriendsPhone = mfriendsph.getText().toString();
        String FriendsEmail = mfriendse.getText().toString();
        // Preconditions

        // Do nothing if one of the field is empty
        if (FriendsName.isEmpty() ) {
            Toast.makeText(UserActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
        // If no avatar selected only the other information will be sent to the database
        else if (isAvatarset == false)
        {
            downloadedUri = "";
            Map<String, Object> friends = new HashMap<>();
            friends.put("FMName", FriendsName);
            friends.put("PhoneNo", FriendsPhone);
            friends.put("Email", FriendsEmail);
            friends.put("Avatar", downloadedUri);
            db.collection("users")
                    .document(fAuth.getUid()).collection("FriendsMember").document(FriendsName).set(friends).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("db", "Upload a friend without avatar to the firestore");
                            }
                            else{
                                Toast.makeText(UserActivity.this, "Fail to Upload a friend without avatar to firestore", Toast.LENGTH_SHORT).show();
                                Log.d("db", "Fail to Upload a friend without avatar to the firestore");
                            }
                        }
                    });
            Toast.makeText(UserActivity.this, "New Family Member Created without avatar!", Toast.LENGTH_SHORT).show();
            // End actions
            isAvatarset = false;
            Avatar.setImageURI(homeUri);
            mfriendsName.getText().clear();
        }
        else {
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
                    //Toast.makeText(FamilyMemberCreatePage.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
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

                        // get download Url
                        Uri downloadUri = task.getResult();
                        downloadedUri = downloadUri.toString();
                        //put the family member into the data base.
                        Map<String, Object> friend = new HashMap<>();
                        friend.put("FRName", FriendsName);
                        friend.put("FRPhone", FriendsPhone);
                        friend.put("FREmail", FriendsEmail);
                        friend.put("Avatar", downloadedUri);
                        // Put the new family member into cloud
                        db.collection("users")
                                .document(fAuth.getUid()).collection("FamilyMember").document(FriendsName).set(friend).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("db", "Upload a friend with avatar to the firestore");
                                        }
                                        else{
                                            Toast.makeText(UserActivity.this, "Fail to Upload to firestore", Toast.LENGTH_SHORT).show();
                                            Log.d("db", "Fail to Upload a friend with avatar to the firestore");
                                        }
                                    }
                                });
                        Toast.makeText(UserActivity.this, "New friend Created!", Toast.LENGTH_SHORT).show();
                    }
                    // The case where the upload is unsuccessful
                    else
                    {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(UserActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            // End actions
            isAvatarset = false;
            Avatar.setImageURI(homeUri);
            mfriendsName.getText().clear();
        }
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

}