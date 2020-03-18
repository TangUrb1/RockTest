package com.tangux.rocktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView titleImageView;
    private Button playButton, settingsButton, aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        titleImageView = findViewById(R.id.titleImageView);
        playButton = findViewById(R.id.playButton);
        settingsButton = findViewById(R.id.settingsButton);
        aboutButton = findViewById(R.id.aboutButton);

        final Animation titleAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.titlepop);
        final Animation playButtonAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.playbuttonpop);
        final Animation settingsButtonAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.settingsbuttonpop);
        final Animation aboutButtonAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.aboutbuttonpop);

        titleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                playButton.setVisibility(View.VISIBLE);
                playButton.startAnimation(playButtonAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        playButtonAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                settingsButton.setVisibility(View.VISIBLE);
                settingsButton.startAnimation(settingsButtonAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        settingsButtonAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                aboutButton.setVisibility(View.VISIBLE);
                aboutButton.startAnimation(aboutButtonAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        titleImageView.startAnimation(titleAnimation);

        settingsButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playButton:
                Intent playIntent = new Intent(MainActivity.this, PlayActivity.class);
                startActivity(playIntent);
                break;
            case R.id.settingsButton:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.aboutButton:
                Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(aboutIntent);

        }
    }
}
