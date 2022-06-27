package com.example.storbook;
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
        TextView t1 = (TextView)findViewById(R.id.createacc);
        String s1="<a> href='www.google.com'>create new account</a>";
        CharSequence web= "create new account";
        t1.setText(web);
        t1.setMovementMethod(LinkMovementMethod.getInstance());

    }
    public void onpwdClicked(View view){

    }
    public void onctClicked(View view){

    }

}
