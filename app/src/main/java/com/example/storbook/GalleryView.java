package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class GalleryView extends AppCompatActivity implements View.OnClickListener {

    ViewFlipper viewFlipper;
    Button next;
    Button previous;

    public void loginLinkClicked(View view){
        Intent myIntent = new Intent(GalleryView.this, GalleryView.class);
        GalleryView.this.startActivity(myIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_view);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        next = (Button)findViewById(R.id.next);
        previous = (Button)findViewById(R.id.previous);

        next.setOnClickListener(this);
        previous.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == next) {
            viewFlipper.showNext();
        } else if (v == previous) {
            viewFlipper.showPrevious();
        }
    }

}