package com.example.storbook;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.storbook.databinding.ActivityFriendMemberPageBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FriendMemberPage extends AppCompatActivity {
    ActivityFriendMemberPageBinding binding;
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String name;
    String email;
    String info;
    String imageurl;
    int positionIndex;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    Uri imageUri;
    String downloadedUri;
    boolean isAvatarset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendMemberPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        isAvatarset = false;


        Intent intent = this.getIntent();
        // Get the carried out information
        if (intent != null){

            name = intent.getStringExtra("Name");
            email = intent.getStringExtra("Email");
            info = intent.getStringExtra("Info");
            imageurl = intent.getStringExtra("imageID");
            positionIndex = intent.getIntExtra("position",0);


            binding.frname.setText(name);
            binding.fremail.setText(email);
            binding.frinformation.setText(info);

            Glide.with(this).load(imageurl).into(binding.profileImage);

        }

        // Prepare for the member's gallery
        ((global)this.getApplication()).getpictureUrlsforFR(name);

        // Enable edit buttons when in caretaker mode
        if (((global) this.getApplication()).isCaretaker()){
            binding.changebtn.setEnabled(true);
            binding.cancelbtn.setEnabled(true);
            binding.changebtn.setVisibility(View.VISIBLE);
            binding.cancelbtn.setVisibility(View.VISIBLE);
            binding.fremail.setEnabled(true);
            binding.frinformation.setEnabled(true);
            binding.frname.setEnabled(true);
            binding.profileImage.setClickable(true);
            binding.mediabtn.setText("delete");
        }
        else{
            binding.changebtn.setEnabled(false);
            binding.cancelbtn.setEnabled(false);
            binding.changebtn.setVisibility(View.INVISIBLE);
            binding.cancelbtn.setVisibility(View.INVISIBLE);
            binding.fremail.setBackgroundResource(android.R.color.transparent);
            binding.frinformation.setBackgroundResource(android.R.color.transparent);
            binding.frname.setBackgroundResource(android.R.color.transparent);
            binding.fremail.setEnabled(false);
            binding.frinformation.setEnabled(false);
            binding.frname.setEnabled(false);
            binding.profileImage.setClickable(false);
            binding.mediabtn.setText("media");
        }

    }


    public void cancel(View v){
        Intent myIntent = new Intent(this, FriendMemberMainPage.class);
        this.startActivity(myIntent);
    }


    public void change(View v){
        String newName = binding.frname.getText().toString();
        String newEmail = binding.fremail.getText().toString();
        String newInfo = binding.frinformation.getText().toString();
        boolean noDuplication = true;
        if (newInfo.isEmpty()){
            newInfo = "No Information.";
        }
        // check if the name is already existed
        for (int i = 0; i < ((global) this.getApplication()).AllFRembers.size(); i++){
            if (((global) this.getApplication()).AllFRembers.get(i).name.equals(newName)){
                noDuplication = false;
                Toast.makeText(getApplicationContext(), "Friend member has the same name already exists!", Toast.LENGTH_SHORT).show();
            }
        }
        if (newName.isEmpty()){
            Toast.makeText(getApplicationContext(), " name fields cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        else if (noDuplication){
            if (isAvatarset) {
                ((global) this.getApplication()).friendMembermodification(newName, newInfo, newEmail, positionIndex, imageUri);
            }
            else{
                ((global) this.getApplication()).friendMembermodification(newName, newInfo, newEmail, positionIndex, null);
            }
            // go back if the edit success
            Intent myIntent = new Intent(this, FriendMemberMainPage.class);
            this.startActivity(myIntent);
        }

        // End actions
        isAvatarset = false;
    }

    public void deleteOrMeida(View v){
        if (((global) this.getApplication()).isCaretaker) {
            ((global) this.getApplication()).deleteFriendMember(name, positionIndex);
            Toast.makeText(getApplicationContext(), "Friend Member Deleted!", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(this, FriendMemberMainPage.class);
            this.startActivity(myIntent);
        }
        else{
            Intent i = new Intent(this, GalleryActivity.class);
            i.putExtra("Name", name);
            i.putExtra("Email", email);
            i.putExtra("imageID", imageurl);
            i.putExtra("Info", info);
            i.putExtra("position", positionIndex);
            startActivity(i);
            this.startActivity(i);
        }
    }

    public void chooseavatar(View v){
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
            binding.profileImage.setImageURI(imageUri);
            isAvatarset = true;
        }
    }

    public void updateLocalList (FriendMember newFR){
        ((global) this.getApplication()).AllFRembers.add(newFR);
    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, FriendMemberMainPage.class);
        this.startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(this, FriendMemberMainPage.class);
        this.startActivity(myIntent);
    }
}