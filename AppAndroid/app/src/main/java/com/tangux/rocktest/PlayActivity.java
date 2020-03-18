package com.tangux.rocktest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class PlayActivity extends AppCompatActivity {

    private TextView textCurrentPosition;
    private Button pauseButton, startButton;
    private SeekBar seekBar;
    private Handler threadHandler;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        getSupportActionBar().setTitle(getString(R.string.question) + "");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(227,141,71)));

        textCurrentPosition = findViewById(R.id.currentTimeTextView);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        threadHandler = new Handler();

        pauseButton.setEnabled(false);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setClickable(false);

        int songId = getRawResIdByName("taintedlove");

        mediaPlayer = MediaPlayer.create(this, songId);

    }

    public int getRawResIdByName(String resName) {

        String pkgName = getPackageName();
        int resID = getResources().getIdentifier(resName, "raw", pkgName);
        return resID;
    }

    private String millisecondsToString(int milliseconds) {

        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds((long) milliseconds);
        return minutes + ":" + seconds;
    }

    public void doStart(View view) {

        int duration = mediaPlayer.getDuration();

        int currentPosition = mediaPlayer.getCurrentPosition();
        if (currentPosition == 0){
            seekBar.setMax(duration);
        }else if(currentPosition == duration) {
            mediaPlayer.reset();
        }
        mediaPlayer.start();

        UpdateSeekBarThread updateSeekBarThread = new UpdateSeekBarThread();
        threadHandler.postDelayed(updateSeekBarThread, 50);
        pauseButton.setEnabled(true);
        startButton.setEnabled(false);
    }

    class UpdateSeekBarThread implements Runnable {

        public void run() {
            int currentPosition = mediaPlayer.getCurrentPosition();
            String currentPositionStr = millisecondsToString(currentPosition);
            textCurrentPosition.setText(currentPositionStr);

            seekBar.setProgress(currentPosition);
            threadHandler.postDelayed(this, 50);
        }
    }

    public void doPause(View view) {

        mediaPlayer.pause();
        pauseButton.setEnabled(false);
        startButton.setEnabled(true);
    }
}
