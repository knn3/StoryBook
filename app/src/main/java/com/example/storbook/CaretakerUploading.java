package com.example.storbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.storbook.databinding.ActivityCaretakerUploadingBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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


public class CaretakerUploading extends AppCompatActivity {

    ActivityCaretakerUploadingBinding binding;
    Uri mediaUrl;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    FirebaseUser user;
    FirebaseFirestore db;
    String media;
    String email;
    String downloadedUri;

    // The member that the media linked to (limit to one)
    String Belonged;

    // picture, video, audio
    String MeidaType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCaretakerUploadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MeidaType = "";


        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        binding.videoView.setVisibility(View.INVISIBLE);

        binding.selectImagebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectImage();
            }
        });

        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        binding.videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectVideo();
            }
        });
        binding.chooserelationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CaretakerUploading.this, FamilyMemberMainPage.class);
                i.putExtra("Mode",1);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Belonged = "";
        binding.chooserelationbtn.setText("choose belong to");

        Intent intent = this.getIntent();
        // Get the carried out information
        if (intent != null){
            String name = intent.getStringExtra("Name");
            if (name != null) {
                Belonged = name;
                binding.chooserelationbtn.setText("Belong to " + Belonged);
            }
        }

    }

    private void uploadImage(){
        if (MeidaType.equals("")){
            Toast.makeText(CaretakerUploading.this, "Choose the media first", Toast.LENGTH_SHORT).show();
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
            UploadTask uploadTask = storageReference.putFile(mediaUrl);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    //handle progressDialog
                    binding.imageView.setImageURI(null);
                    Toast.makeText(CaretakerUploading.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
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

                        //upload and create a new document for this uploaded media to firestore
                        Map<String, Object> media = new HashMap<>();
                        media.put("Url", downloadedUri);
                        media.put("ClickedTime", 0);
                        // Synced with target family member if selected
                        if (!Belonged.equals("")){
                            db.collection("users").document(user.getUid()).collection("FamilyMember").document(Belonged).update("media", FieldValue.arrayUnion(downloadedUri))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CaretakerUploading.this, "Successfully Synced with target Family Member", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        media.put("Belonged", Belonged);
                        media.put("Type", MeidaType);
                        db.collection("users")
                                .document(user.getUid())
                                .collection("Media")
                                .document(fileName)
                                .set(media).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("db", "Media Successfully Uploaded to Firestore");
                                        } else {
                                            Toast.makeText(CaretakerUploading.this, "Fail to Upload a media to cloud", Toast.LENGTH_SHORT).show();
                                            Log.d("db", "Fail to Upload a family member without avatar to the firestore");
                                        }
                                    }
                                });


                        db.collection("users").document(user.getUid()).update("media", FieldValue.arrayUnion(downloadedUri))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(CaretakerUploading.this, "Successfully Uploaded to Firestore", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    } else {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(CaretakerUploading.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void selectImage(){
        MeidaType = "picture";
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    private void selectVideo(){
        MeidaType = "video";
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){
            mediaUrl = data.getData();
            binding.videoView.setVisibility(View.INVISIBLE);
            binding.imageView.setImageURI(mediaUrl);
        }
        else if (requestCode == 101 && data != null && data.getData() != null){
            mediaUrl = data.getData();
            binding.videoView.setVisibility(View.VISIBLE);
            binding.videoView.setVideoURI(mediaUrl);

            MediaController mediaController = new MediaController(this);
            binding.videoView.setMediaController(mediaController);
            mediaController.setAnchorView(binding.videoView);
        }
    }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }
}