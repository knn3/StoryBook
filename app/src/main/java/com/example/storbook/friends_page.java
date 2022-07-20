package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.storbook.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class friends_page extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imageId={R.drawable.album,R.drawable.ancestors,R.drawable.gallery};
        String[] name={"ada","wendy","alice"};
        String[] phoneno={"huioho","huhu","dhuwqa"};
        String[] email={"12345","78906","adhiwoq"};

        ArrayList<User> userArrayList= new ArrayList<>();

        for(int i=0;i<imageId.length;i++){
            User user = new User(name[i],phoneno[i],email[i],imageId[i]);
            userArrayList.add(user);



        }

        ListAdapter listAdapter= new ListAdapter(friends_page.this,userArrayList);
        binding.listview.setAdapter(listAdapter);

    }
}