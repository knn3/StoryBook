package com.example.storbook;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class language extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);


        findViewById(R.id.english).setOnClickListener(this);
        findViewById(R.id.chinese).setOnClickListener(this);

        findViewById(R.id.korean).setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chinese:
                showSaveLanguage("ch");
                break;
            case R.id.english:
                showSaveLanguage("en");
                break;
            case R.id.korean:
                showSaveLanguage("ko");
                break;
            default:
                break;
        }
    }

        private void showSaveLanguage(String language){
            //设置的语言、重启的类一般为应用主入口（微信也是到首页）
            LanguageUtil.changeAppLanguage(this, language, MainActivity.class);
            //保存设置的语言
            SpUserUtils.putString(this, "language", language);
        }

    //back button
    public void onBackClick(View v){
        Intent myIntent = new Intent(this, Setting.class);
        this.startActivity(myIntent);
    }

}