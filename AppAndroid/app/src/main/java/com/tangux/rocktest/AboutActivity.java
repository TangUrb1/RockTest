package com.tangux.rocktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Recuperation of the TextView & set the version of the app to its text
        TextView versionTextView = findViewById(R.id.versionTextView);
        versionTextView.setText(BuildConfig.VERSION_NAME);
    }
}
