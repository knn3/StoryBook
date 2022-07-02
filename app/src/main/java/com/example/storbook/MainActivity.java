package com.example.storbook;
import android.content.Intent;
import android.text.Html;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String s1="<a> href='www.google.com'>create new account</a>";
        CharSequence web= "create new account";

    }
    public void onpwdClicked(View view){
    }
    public void onctClicked(View view){
        Intent myIntent = new Intent(MainActivity.this, CaretakerLogin.class);
        MainActivity.this.startActivity(myIntent);

    }

}
