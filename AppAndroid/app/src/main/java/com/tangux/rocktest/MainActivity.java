package com.tangux.rocktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView titleImageView;
    private Button playButton, settingsButton;
    private TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        titleImageView = findViewById(R.id.titleImageView);
        playButton = findViewById(R.id.playButton);
        settingsButton = findViewById(R.id.settingsButton);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        final Animation titleAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.titlepop);
        final Animation playButtonAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.playbuttonpop);
        final Animation settingsButtonAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.settingsbuttonpop);
        final Animation descriptionAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.descriptionpop);

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
                descriptionTextView.setVisibility(View.VISIBLE);
                descriptionTextView.startAnimation(descriptionAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        titleImageView.startAnimation(titleAnimation);
    }
}
