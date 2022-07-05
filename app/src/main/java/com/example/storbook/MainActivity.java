package com.example.storbook;
import android.content.Intent;
import android.text.Html;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.storbook.databinding.ActivityMainBinding;
import com.google.firebase.functions.FirebaseFunctions;

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
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        functions.useEmulator("10.0.2.2", 5001);

//        imgButton = (ImageButton) findViewById(R.id.btnct);
//        imgButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openCaretakerActivity();
//            }
//        });





//        ImageButton button = findViewById(R.id.btnct);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    Intent intent = new Intent(MainActivity.this, Caretaker.class);
//                    startActivity(intent);
//            }
//        });

    }
    public void onpwdClicked(View view){

    }

    public void onctClicked( View view){
        Intent intent = new Intent(MainActivity.this, Caretaker.class);
        startActivity(intent);
    }

}
