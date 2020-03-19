package com.tangux.rocktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayActivity extends AppCompatActivity {

    private TextView textCurrentPosition, answerTextView, titleTextView, questionTextView;
    private Button playButton, firstAnswer, secondAnswer, thirdAnswer, fourthAnswer, nextQuestion;
    private SeekBar seekBar;
    private Handler threadHandler;
    private MediaPlayer mediaPlayer;
    private JSONObject musicJSON;
    private JSONArray easy, medium, hard;
    private List<Music> testMusics;
    private List<JSONObject> easyJSON, mediumJSON, hardJSON;
    private int countQuestion, correctAnswered;
    private Music currentMusic;
    private Intent srcIntent;
    private CountDownTimer musicTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        countQuestion = 1;
        correctAnswered = 0;

        easyJSON = new ArrayList<>();
        mediumJSON = new ArrayList<>();
        hardJSON = new ArrayList<>();

        testMusics = new ArrayList<>();

        srcIntent = getIntent();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(227,141,71)));

        textCurrentPosition = findViewById(R.id.currentTimeTextView);
        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerTextView);
        titleTextView = findViewById(R.id.titleTextView);

        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStart();
            }
        });
        firstAnswer = findViewById(R.id.firstAnswer);
        firstAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(firstAnswer);
            }
        });
        secondAnswer = findViewById(R.id.secondAnswer);
        secondAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(secondAnswer);
            }
        });
        thirdAnswer = findViewById(R.id.thirdAnswer);
        thirdAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(thirdAnswer);
            }
        });
        fourthAnswer = findViewById(R.id.fourthAnswer);
        fourthAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(fourthAnswer);
            }
        });

        nextQuestion = findViewById(R.id.nextButton);
        nextQuestion.setEnabled(false);
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        threadHandler = new Handler();

        seekBar = findViewById(R.id.seekBar);
        seekBar.setClickable(false);




        try {
            musicJSON = new JSONObject(loadJSONFromAssets(PlayActivity.this));
            easy = musicJSON.getJSONArray("easy");
            medium = musicJSON.getJSONArray("medium");
            hard = musicJSON.getJSONArray("hard");


            switch (srcIntent.getStringExtra("difficulty")) {

                case "easy":
                    for (int i = 0; i < easy.length(); i++) {
                        JSONObject musicObject = easy.getJSONObject(i);
                        easyJSON.add(musicObject);
                    }

                    for (JSONObject musicObject : easyJSON) {
                        Music easyMusic = new Music();
                        easyMusic.title = musicObject.getString("title");
                        easyMusic.singer = musicObject.getString("singer");
                        easyMusic.path = musicObject.getString("path");
                        easyMusic.answers = new ArrayList<>();
                        for (int j = 0; j < 4; j++){
                            easyMusic.answers.add((String)musicObject.getJSONArray("answers").get(j));
                        }
                        Log.i("test", "titre : " + easyMusic.title);
                        testMusics.add(easyMusic);
                    }
                    break;
                case "medium":
                    for (int i = 0; i < medium.length(); i++) {
                        JSONObject musicObject = medium.getJSONObject(i);
                        mediumJSON.add(musicObject);
                    }

                    for (JSONObject musicObject : mediumJSON) {
                        Music mediumMusic = new Music();
                        mediumMusic.title = musicObject.getString("title");
                        mediumMusic.singer = musicObject.getString("singer");
                        mediumMusic.path = musicObject.getString("path");
                        Log.i("test", "titre : " + mediumMusic.title);
                        testMusics.add(mediumMusic);
                    }
                    break;
                case "hard":
                    for (int i = 0; i < hard.length(); i++) {
                        JSONObject musicObject = hard.getJSONObject(i);
                        hardJSON.add(musicObject);
                    }

                    for (JSONObject musicObject : hardJSON) {
                        Music hardMusic = new Music();
                        hardMusic.title = musicObject.getString("title");
                        hardMusic.singer = musicObject.getString("singer");
                        hardMusic.path = musicObject.getString("path");
                        Log.i("test", "titre : " + hardMusic.title);
                        testMusics.add(hardMusic);
                    }
                    break;
            }
            Collections.shuffle(testMusics);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        launchTest();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void launchTest() {
        getSupportActionBar().setTitle(srcIntent.getStringExtra("difficulty").toUpperCase() + " - " + getString(R.string.question) + " " + countQuestion);

        currentMusic = testMusics.get(countQuestion - 1);
        int songId = getRawResIdByName(currentMusic.path);
        mediaPlayer = MediaPlayer.create(PlayActivity.this, songId);
        mediaPlayer.seekTo(30000);
        Log.i("musique", ""+songId);

        Collections.shuffle(currentMusic.answers);

        firstAnswer.setText(currentMusic.answers.get(0));
        secondAnswer.setText(currentMusic.answers.get(1));
        thirdAnswer.setText(currentMusic.answers.get(2));
        fourthAnswer.setText(currentMusic.answers.get(3));
    }

    public String loadJSONFromAssets(Context context) {

        String json = null;

        try {
            InputStream is = context.getAssets().open("bd.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
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

    public void doStart() {

        int currentPosition = mediaPlayer.getCurrentPosition();
        seekBar.setMax(60000);

        musicTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mediaPlayer.start();
                playButton.setEnabled(false);
            }

            @Override
            public void onFinish() {
                mediaPlayer.stop();
                playButton.setEnabled(true);
            }
        };
        musicTimer.start();

        UpdateSeekBarThread updateSeekBarThread = new UpdateSeekBarThread();
        threadHandler.postDelayed(updateSeekBarThread, 50);
        playButton.setEnabled(false);
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

    public void checkAnswer(Button buttonPressed) {

        mediaPlayer.stop();
        mediaPlayer.reset();
        musicTimer.cancel();

        if(buttonPressed.getText().equals(currentMusic.title)) {
            correctAnswered++;
            answerTextView.setText(getString(R.string.goodanswer));
        }
        else{
            answerTextView.setText(getString(R.string.badanswer));
        }

        questionTextView.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.INVISIBLE);
        textCurrentPosition.setVisibility(View.INVISIBLE);
        playButton.setVisibility(View.INVISIBLE);
        playButton.setEnabled(false);
        firstAnswer.setVisibility(View.INVISIBLE);
        firstAnswer.setEnabled(false);
        secondAnswer.setVisibility(View.INVISIBLE);
        secondAnswer.setEnabled(false);
        thirdAnswer.setVisibility(View.INVISIBLE);
        thirdAnswer.setEnabled(false);
        fourthAnswer.setVisibility(View.INVISIBLE);
        fourthAnswer.setEnabled(false);

        titleTextView.setText(currentMusic.title + " - " + currentMusic.singer);
        answerTextView.setVisibility(View.VISIBLE);
        titleTextView.setVisibility(View.VISIBLE);
        nextQuestion.setVisibility(View.VISIBLE);
        nextQuestion.setEnabled(true);
    }

    public void nextQuestion() {
        countQuestion++;
        answerTextView.setVisibility(View.INVISIBLE);
        titleTextView.setVisibility(View.INVISIBLE);
        nextQuestion.setVisibility(View.INVISIBLE);
        nextQuestion.setEnabled(false);

        questionTextView.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        textCurrentPosition.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.VISIBLE);
        playButton.setEnabled(true);
        firstAnswer.setVisibility(View.VISIBLE);
        firstAnswer.setEnabled(true);
        secondAnswer.setVisibility(View.VISIBLE);
        secondAnswer.setEnabled(true);
        thirdAnswer.setVisibility(View.VISIBLE);
        thirdAnswer.setEnabled(true);
        fourthAnswer.setVisibility(View.VISIBLE);
        fourthAnswer.setEnabled(true);

        launchTest();
    }
}
