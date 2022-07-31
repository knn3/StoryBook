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
import com.example.storbook.databinding.ActivityFamilyMemberPageBinding;
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


public class FamilyMemberPage extends AppCompatActivity {

    ActivityFamilyMemberPageBinding binding;
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String name;
    String relation;
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
        binding = ActivityFamilyMemberPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        isAvatarset = false;


        Intent intent = this.getIntent();
        // Get the carried out information
        if (intent != null){

            name = intent.getStringExtra("Name");
            relation = intent.getStringExtra("Relation");
            info = intent.getStringExtra("Info");
            imageurl = intent.getStringExtra("imageID");
            positionIndex = intent.getIntExtra("position",0);


            binding.fmname.setText(name);
            binding.fmrelation.setText(relation);
            binding.fminformation.setText(info);

            Glide.with(this).load(imageurl).into(binding.profileImage);

        }

        // Prepare for the member's gallery
        ((global)this.getApplication()).getpictureUrlsforFM(name);

        // Enable edit buttons when in caretaker mode
        if (((global) this.getApplication()).isCaretaker()){
            binding.changebtn.setEnabled(true);
            binding.cancelbtn.setEnabled(true);
            binding.changebtn.setVisibility(View.VISIBLE);
            binding.cancelbtn.setVisibility(View.VISIBLE);
            binding.fmrelation.setEnabled(true);
            binding.fminformation.setEnabled(true);
            binding.fmname.setEnabled(true);
            binding.profileImage.setClickable(true);
            binding.mediabtn.setText("delete");
        }
        else{
            binding.changebtn.setEnabled(false);
            binding.cancelbtn.setEnabled(false);
            binding.changebtn.setVisibility(View.INVISIBLE);
            binding.cancelbtn.setVisibility(View.INVISIBLE);
            binding.fmrelation.setBackgroundResource(android.R.color.transparent);
            binding.fminformation.setBackgroundResource(android.R.color.transparent);
            binding.fmname.setBackgroundResource(android.R.color.transparent);
            binding.fmrelation.setEnabled(false);
            binding.fminformation.setEnabled(false);
            binding.fmname.setEnabled(false);
            binding.profileImage.setClickable(false);
            binding.mediabtn.setText("media");
        }

    }


    public void cancel(View v){
        Intent myIntent = new Intent(this, FamilyMemberMainPage.class);
        this.startActivity(myIntent);
    }


    public void change(View v){
        String newName = binding.fmname.getText().toString();
        String newRelation = binding.fmrelation.getText().toString();
        String newInfo = binding.fminformation.getText().toString();
        boolean noDuplication = true;
        if (newInfo.isEmpty()){
            newInfo = "No Information.";
        }
        // check if the name is already existed
        for (int i = 0; i < ((global) this.getApplication()).AllFMembers.size(); i++){
            if (((global) this.getApplication()).AllFMembers.get(i).name.equals(newName)){
                noDuplication = false;
                Toast.makeText(getApplicationContext(), "Family member has the same name already exists!", Toast.LENGTH_SHORT).show();
            }
        }
        if (newName.isEmpty() || newRelation.isEmpty()){
            Toast.makeText(getApplicationContext(), "Name, relation fields cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        else if (noDuplication){
            if (isAvatarset) {
                ((global) this.getApplication()).familyMembermodification(newName, newInfo, newRelation, positionIndex, imageUri);
            }
            else{
                ((global) this.getApplication()).familyMembermodification(newName, newInfo, newRelation, positionIndex, null);
            }
            // go back if the edit success
            Intent myIntent = new Intent(this, FamilyMemberMainPage.class);
            this.startActivity(myIntent);
        }

        // End actions
        isAvatarset = false;
    }

    public void deleteOrMeida(View v){
        if (((global) this.getApplication()).isCaretaker) {
            ((global) this.getApplication()).deleteFamilyMember(name, positionIndex);
            Toast.makeText(getApplicationContext(), "Family Member Deleted!", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(this, FamilyMemberMainPage.class);
            this.startActivity(myIntent);
        }
        else{
            Intent i = new Intent(this, GalleryActivity.class);
            i.putExtra("Name", name);
            i.putExtra("Relation", relation);
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

    public void updateLocalList (FamilyMember newFM){
        ((global) this.getApplication()).AllFMembers.add(newFM);
    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, FamilyMemberMainPage.class);
        this.startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(this, FamilyMemberMainPage.class);
        this.startActivity(myIntent);
    }

}