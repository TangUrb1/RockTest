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
    private Boolean isPlaying;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        countQuestion = 1;
        correctAnswered = 0;

        //Initialisation of each ArrayList
        easyJSON = new ArrayList<>();
        mediumJSON = new ArrayList<>();
        hardJSON = new ArrayList<>();

        testMusics = new ArrayList<>();

        //Get the parameters the last activity sent
        srcIntent = getIntent();

        //Change the color of the ActionBar to orange
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(227,141,71)));

        //Set each element of the view
        textCurrentPosition = findViewById(R.id.currentTimeTextView);
        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerTextView);
        titleTextView = findViewById(R.id.titleTextView);

        //Set onClick events
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
        //This button will not be able to be pressed
        nextQuestion.setEnabled(false);
        //If we are on the tenth question, then we show the results
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countQuestion < 10){
                    nextQuestion();
                } else {
                    showResult();
                }

            }
        });

        threadHandler = new Handler();

        //Instance a seekBar for the mediaPlayer that will not be able to be clickable
        seekBar = findViewById(R.id.seekBar);
        seekBar.setClickable(false);




        try {
            //Get the json file from the directory assets to an json object
            musicJSON = new JSONObject(loadJSONFromAssets(PlayActivity.this));
            //Get the json array of each difficulty in the json object
            easy = musicJSON.getJSONArray("easy");
            medium = musicJSON.getJSONArray("medium");
            hard = musicJSON.getJSONArray("hard");


            //Create a list of Music object according the difficulty chosen in the last view
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
            //Shuffle / randomise the list of object Music
            Collections.shuffle(testMusics);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        launchTest();
        //Boolean to know if the song is playing
        isPlaying = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Stop the activity if we go back
        finish();
    }

    public void launchTest() {
        //Set the title of the ActionBar to the difficulty chosen + the number of current question
        getSupportActionBar().setTitle(srcIntent.getStringExtra("difficulty").toUpperCase() + " - " + getString(R.string.question) + " " + countQuestion + "/10");

        //Get the songId according to current question number to get the information of the current song (title, artist, path and possible answers)
        currentMusic = testMusics.get(countQuestion - 1);
        int songId = getRawResIdByName(currentMusic.path);
        mediaPlayer = MediaPlayer.create(PlayActivity.this, songId);
        mediaPlayer.seekTo(30000);
        Log.i("musique", ""+songId);

        //Shuffle the list of answers and display it on each button
        Collections.shuffle(currentMusic.answers);

        firstAnswer.setText(currentMusic.answers.get(0));
        secondAnswer.setText(currentMusic.answers.get(1));
        thirdAnswer.setText(currentMusic.answers.get(2));
        fourthAnswer.setText(currentMusic.answers.get(3));
    }

    //Function to get the JSON file from the directory asssets
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

    //Function to get the id of a file (in that case a song) according to the name of the file
    public int getRawResIdByName(String resName) {

        String pkgName = getPackageName();
        int resID = getResources().getIdentifier(resName, "raw", pkgName);
        return resID;
    }

    //Transform the time to a string
    private String millisecondsToString(int milliseconds) {

        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds((long) milliseconds);
        return minutes + ":" + seconds;
    }

    public void doStart() {

        //Set the max length of the seekbar to 60 seconds
        seekBar.setMax(60000);

        //Set a timer of 30 seconds, at the end of it, the song will stop
        musicTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mediaPlayer.start();
                playButton.setEnabled(false);
                isPlaying = true;
            }

            @Override
            public void onFinish() {
                mediaPlayer.stop();
                musicTimer.cancel();
                isPlaying = false;
                playButton.setEnabled(true);
            }
        };
        musicTimer.start();

        UpdateSeekBarThread updateSeekBarThread = new UpdateSeekBarThread();
        threadHandler.postDelayed(updateSeekBarThread, 50);
        playButton.setEnabled(false);
    }

    //Set the seekBar thread according to the current position of the song
    class UpdateSeekBarThread implements Runnable {

        public void run() {
            int currentPosition = mediaPlayer.getCurrentPosition();
            String currentPositionStr = millisecondsToString(currentPosition);
            textCurrentPosition.setText(currentPositionStr);

            seekBar.setProgress(currentPosition);
            threadHandler.postDelayed(this, 50);
        }
    }

    //Check the answer, if it's a good one -> +1 on correctAnswers and display a good text, otherwise display a bad text
    public void checkAnswer(Button buttonPressed) {


        if(isPlaying == true){
            mediaPlayer.stop();
            mediaPlayer.reset();
            musicTimer.cancel();
        }

        if(buttonPressed.getText().equals(currentMusic.title)) {
            correctAnswered++;
            answerTextView.setText(getString(R.string.goodanswer));
        }
        else{
            answerTextView.setText(getString(R.string.badanswer));
        }

        //Don't show the answer of the question on another view, just hide the element of the question and display the answer
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

        //Set the text of the button to "next question", or if it was the last question to "end game"
        if(countQuestion == 10){
            nextQuestion.setText(getString(R.string.endgame));
        }
        nextQuestion.setVisibility(View.VISIBLE);
        nextQuestion.setEnabled(true);
    }

    //Reverse, hide the element of the answer and display the next question
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

    //If it was the last question, after the answer, go to another view to display the results of the test
    //And finish the activity
    public void showResult() {
        Intent resultIntent = new Intent(PlayActivity.this, ResultActivity.class);
        resultIntent.putExtra("goodAnswers", correctAnswered);
        resultIntent.putExtra("totalQuestions", countQuestion);
        startActivity(resultIntent);
        finish();
    }
}
