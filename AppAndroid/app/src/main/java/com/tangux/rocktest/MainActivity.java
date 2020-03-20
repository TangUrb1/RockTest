package com.tangux.rocktest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView titleImageView;
    private Button playButton, settingsButton, aboutButton, listButton;
    private JSONObject musicJson;
    private String[] difficulties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Recuperation of the elemont of the main view
        titleImageView = findViewById(R.id.titleImageView);
        playButton = findViewById(R.id.playButton);
        settingsButton = findViewById(R.id.settingsButton);
        aboutButton = findViewById(R.id.aboutButton);
        listButton = findViewById(R.id.questionsButton);

        //Set a lst of difficulties
        difficulties = new String[]{"easy", "medium", "hard"};

        //Load of the animation of every buttons
        final Animation titleAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.titlepop);
        final Animation playButtonAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.playbuttonpop);
        final Animation settingsButtonAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.settingsbuttonpop);
        final Animation aboutButtonAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.aboutbuttonpop);
        final Animation listButtonAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.listbuttonpop);

        //Then, set an AnimationListener to set a "chain" of appearance for the buttons
        //One by one
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
                listButton.setVisibility(View.VISIBLE);
                listButton.startAnimation(listButtonAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        listButtonAnimation.setAnimationListener(new Animation.AnimationListener() {
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

        //And then we start the animation at the start of the Activity
        titleImageView.startAnimation(titleAnimation);

        //Set the onClick events
        settingsButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);
        listButton.setOnClickListener(this);

    }

    //Do something different according to the pressed button's id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playButton:
                //Create an intent to start another activity
                final Intent playIntent = new Intent(MainActivity.this, PlayActivity.class);

                //Display an AlertDialog to choose the difficulty of the game and then share it with the activity which will start
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.difficulty));
                builder.setItems(difficulties, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playIntent.putExtra("difficulty", difficulties[which]);
                        startActivity(playIntent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.settingsButton:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.aboutButton:
                Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.questionsButton:
                Intent questionsIntent = new Intent(MainActivity.this, QuestionsActivity.class);
                startActivity(questionsIntent);
                break;

        }
    }
}
