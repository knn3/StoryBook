package com.example.storbook;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.storbook.databinding.ActivityFamilyMemberMainPageBinding;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class FM_listAdapter extends ArrayAdapter<FamilyMember> {


    public FM_listAdapter(Context context, ArrayList<FamilyMember> userArrayList){

        super(context,R.layout.family_member_list_item,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        FamilyMember user = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.family_member_list_item,parent,false);

        }

        ImageView imageView = convertView.findViewById(R.id.FMAvatar);
        TextView userName = convertView.findViewById(R.id.FamilyMemberName);
        TextView relation = convertView.findViewById(R.id.FamilyMemberRelation);
        TextView info = convertView.findViewById(R.id.FamilyMemberInfo);


        String url = user.getUrl();

        Glide.with(imageView).load(url).into(imageView);
        userName.setText(user.name);
        relation.setText(user.relationship);
        info.setText(user.info);


        return convertView;
    }

}
