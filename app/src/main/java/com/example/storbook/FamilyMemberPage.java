package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.storbook.databinding.ActivityFamilyMemberPageBinding;

import java.io.InputStream;
import java.net.URL;


public class FamilyMemberPage extends AppCompatActivity {

    ActivityFamilyMemberPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFamilyMemberPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        // Get the carried out information
        if (intent != null){

            String name = intent.getStringExtra("Name");
            String relation = intent.getStringExtra("Relation");
            String info = intent.getStringExtra("Info");
            String imageurl = intent.getStringExtra("imageID");

            binding.fmname.setText(name);
            binding.fmrelation.setText(relation);
            binding.fminformation.setText(info);

            Glide.with(this).load(imageurl).into(binding.profileImage);

        }

    }
}