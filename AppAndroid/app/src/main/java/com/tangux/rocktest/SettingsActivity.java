package com.tangux.rocktest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Set the color of the ActionBar to orange and set the title to "settings"
        getSupportActionBar().setTitle(R.string.settings);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(227,141,71)));
    }
}
