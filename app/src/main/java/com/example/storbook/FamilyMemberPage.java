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
        }
        else{
            binding.mediabtn.setVisibility(View.VISIBLE);
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
        }

    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, FamilyMemberMainPage.class);
        this.startActivity(myIntent);
    }

    public void cancel(View v){
        Intent myIntent = new Intent(this, FamilyMemberMainPage.class);
        this.startActivity(myIntent);
    }


    public void change(View v){
        String newName = binding.fmname.getText().toString();
        String newRelation = binding.fmrelation.getText().toString();
        String newInfo = binding.fminformation.getText().toString();
        if (newInfo.isEmpty()){
            newInfo = "No Information.";
        }
        if (newName.isEmpty() || newRelation.isEmpty()){
            Toast.makeText(getApplicationContext(), "Name, relation fields cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        // case where the name is not changed
        else if (!isAvatarset){
            // Delete the old document
            db.collection("user").document(user.getUid()).collection("FamilyMember").document(name)
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
                        }
                    });
            // remove it in the local family member list
            ((global) this.getApplication()).AllFMembers.remove(positionIndex);



            FamilyMember newFM = new FamilyMember(newName, newRelation, newInfo, imageurl);
            //put the family member into the data base.
            Map<String, Object> familyMember = new HashMap<>();
            familyMember.put("FMName", newName);
            familyMember.put("FMRelation", newRelation);
            familyMember.put("Avatar", imageurl);
            familyMember.put("FMInfo", newInfo);
            // Put the new family member into cloud
            db.collection("users")
                    .document(fAuth.getUid()).collection("FamilyMember").document(newName).set(familyMember).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("db", "Upload a family member with avatar to the firestore");
                                updateLocalList(newFM);
                            }
                            else{
                                Toast.makeText(FamilyMemberPage.this, "Fail to Upload to firestore", Toast.LENGTH_SHORT).show();
                                Log.d("db", "Fail to Upload a family member with avatar to the firestore");
                            }
                        }
                    });
            // End actions
            Toast.makeText(FamilyMemberPage.this, "Family Member Changed!", Toast.LENGTH_SHORT).show();
            isAvatarset = false;
        }



        // case where the avatar is set
        else{
            // Delete the old document
            db.collection("user").document(user.getUid()).collection("FamilyMember").document(name)
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
                        }
                    });
            // remove it in the local family member list
            ((global) this.getApplication()).AllFMembers.remove(positionIndex);



            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Updating.....");
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
                    binding.profileImage.setImageURI(null);
                    //Toast.makeText(FamilyMemberCreatePage.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        binding.profileImage.setImageURI(imageUri); // change the hint picture back after the uploading tab
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
                        String newInfo = binding.fminformation.getText().toString();
                        FamilyMember newFM = new FamilyMember(newName, newRelation, newInfo, downloadedUri);
                        //put the family member into the data base.
                        Map<String, Object> familyMember = new HashMap<>();
                        familyMember.put("FMName", newName);
                        familyMember.put("FMRelation", newRelation);
                        familyMember.put("Avatar", downloadedUri);
                        familyMember.put("FMInfo", newInfo);
                        // Put the new family member into cloud
                        db.collection("users")
                                .document(fAuth.getUid()).collection("FamilyMember").document(newName).set(familyMember).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("db", "Upload a family member with avatar to the firestore");
                                            updateLocalList(newFM);
                                        }
                                        else{
                                            Toast.makeText(FamilyMemberPage.this, "Fail to Upload to firestore", Toast.LENGTH_SHORT).show();
                                            Log.d("db", "Fail to Upload a family member with avatar to the firestore");
                                        }
                                    }
                                });
                    }
                    // The case where the upload is unsuccessful
                    else
                    {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(FamilyMemberPage.this, "Failed to Upload, check Internet", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            // End actions
            Toast.makeText(FamilyMemberPage.this, "Family Member Changed!", Toast.LENGTH_SHORT).show();
            isAvatarset = false;
        }
        Intent myIntent = new Intent(this, FamilyMemberMainPage.class);
        this.startActivity(myIntent);
    }

    public void delete(View v){

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

}