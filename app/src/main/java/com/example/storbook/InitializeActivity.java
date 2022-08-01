package com.example.storbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InitializeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Jump to login page if did not log in
        if(currentUser == null){
            Intent i = new Intent(InitializeActivity.this, CaretakerLogin.class);
            startActivity(i);
        }
        // Refresh the local FM list if have internet access
        else if (isNetworkAvailable()) {
            //((global) this.getApplication()).refreshFMlist();
            ((global) this.getApplication()).refreshpictureUrls();

            ((global) this.getApplication()).refreshpictureUrls();
            Toast.makeText(getApplicationContext(), "Synced with cloud", Toast.LENGTH_SHORT).show();
        }
        else{
            ((global) this.getApplication()).refreshFMlist();
            ((global) this.getApplication()).refreshpictureUrls();

            ((global) this.getApplication()).refreshFRlist();
            ((global) this.getApplication()).refreshpictureUrls();
            Toast.makeText(getApplicationContext(), "No internet, Entering offline mode!", Toast.LENGTH_SHORT).show();
        }
        // So already logged in go the the main page
        Intent i = new Intent(InitializeActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Jump to login page if did not log in
        if(currentUser == null){
            Intent i = new Intent(InitializeActivity.this, CaretakerLogin.class);
            startActivity(i);
        }
        // So already logged in go the the main page
        else{
            Intent i = new Intent(InitializeActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean hasInternetAccess() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error Checking Internet Status", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet, Entering offline mode!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }
}