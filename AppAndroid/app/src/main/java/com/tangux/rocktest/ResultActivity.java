package com.tangux.rocktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView encouragementTextView, correctResult, wrongResult;
    int correctAnswers, wrongAnswers;
    ProgressBar progressBar;
    Intent srcIntent;
    Button endGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(227,141,71)));
        getSupportActionBar().setTitle(getString(R.string.resultstitle));

        encouragementTextView = findViewById(R.id.encourageTextView);
        correctResult = findViewById(R.id.correctAnswersResult);
        wrongResult = findViewById(R.id.wrongAnswersResult);
        progressBar = findViewById(R.id.resultProgressBar);
        endGame = findViewById(R.id.endGameButton);

        srcIntent = getIntent();

        //get the number of good and wrong answer from the PlayActivity
        correctAnswers = srcIntent.getIntExtra("goodAnswers",1);
        wrongAnswers = srcIntent.getIntExtra("totalQuestions", 1) - correctAnswers;

        showResult();

        //finish the activity when clicked on the button
        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Display a bar according to the result (percentage) and display number of correct and wrong answers et display a message according to result
    public void showResult() {

        progressBar.setMax(srcIntent.getIntExtra("totalQuestions", 1));
        progressBar.setProgress(correctAnswers);

        correctResult.setText(""+correctAnswers);
        wrongResult.setText(""+wrongAnswers);

        if (wrongAnswers == 0){
            encouragementTextView.setText(getString(R.string.awesome));
        }
        else if (wrongAnswers == 1 || wrongAnswers == 2){
            encouragementTextView.setText(getString(R.string.thatisgood));
        }
        else if (wrongAnswers > 2 && wrongAnswers < 5){
            encouragementTextView.setText(getString(R.string.notbad));
        }
        else if (wrongAnswers > 5 && wrongAnswers < 8){
            encouragementTextView.setText(getString(R.string.notgood));
        }
        else if (wrongAnswers > 8){
            encouragementTextView.setText(getString(R.string.sobad));
        }
    }
}
