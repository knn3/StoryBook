package com.example.storbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<User> {

    public ListAdapter(Context context, ArrayList<User> userArrayList){

        super(context,R.layout.list_item,userArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        User user =getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);

        }

        ImageView imageView = convertView.findViewById(R.id.photo);
        TextView userName = convertView.findViewById(R.id.name);
        ImageView imageView2 = convertView.findViewById(R.id.photo2);
        TextView userName2 = convertView.findViewById(R.id.name2);

        imageView.setImageResource(user.ImageId);
        userName.setText(user.name);
        imageView2.setImageResource(user.ImageId);
        userName2.setText(user.name);

        return super.getView(position, convertView, parent);
    }
}
