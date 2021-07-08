package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int score =0;
    int numberOfQuestions = 0;
    int locationOfCorrectAnswer;
    ArrayList<Integer> answers = new ArrayList<Integer>();

    Button goButton;

    TextView sumTextView;
    TextView resultTextView;
    TextView scoreTextView;
    TextView timerTextView;
    Button button0;
    Button button1;
    Button button2;
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goButton = findViewById(R.id.goButton);
        sumTextView = findViewById(R.id.sumTextView);
        resultTextView = findViewById(R.id.resultTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        mainMenu();
    }

    public void mainMenu(){
        timerTextView.setVisibility(View.INVISIBLE);
        scoreTextView.setText(String.format("%d/%d",score, numberOfQuestions));
        scoreTextView.setVisibility(View.INVISIBLE);
        resultTextView.setVisibility(View.INVISIBLE);
        sumTextView.setVisibility(View.INVISIBLE);
        resultTextView.setVisibility(View.INVISIBLE);
        findViewById(R.id.grid).setVisibility(View.INVISIBLE);
        goButton.setVisibility(View.VISIBLE);
    }


    public void playGame(View view){
        resetViews();
        goButton.setVisibility(View.INVISIBLE);

        score = 0;
        numberOfQuestions = 0;

        resetViews();

        newQuestion();
        int time = 30_000;
        final int countDownInterval = 1000;
        new CountDownTimer(time, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(String.format(Locale.US, "%ds", (int) millisUntilFinished / countDownInterval));
            }

            @Override
            public void onFinish() {
                sumTextView.animate().translationXBy(-1000).setDuration(500);
                timerTextView.animate().translationXBy(-500).setDuration(500);

                button0.setEnabled(false);
                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);

                gameOverAnimation();
            }
        }.start();
    }

    public void resetViews(){
        timerTextView.setText("30s");
        timerTextView.setTranslationX(0);
        timerTextView.setVisibility(View.VISIBLE);
        scoreTextView.setText(String.format("%d/%d",score, numberOfQuestions));
        scoreTextView.setTranslationX(0);
        scoreTextView.setVisibility(View.VISIBLE);
        resultTextView.setVisibility(View.VISIBLE);
        sumTextView.setVisibility(View.VISIBLE);
        sumTextView.setTranslationX(0);
        resultTextView.setVisibility(View.INVISIBLE);
        button0.setEnabled(true);
        button0.setBackgroundColor(Color.parseColor("#CC453A"));
        button1.setEnabled(true);
        button1.setBackgroundColor(Color.parseColor("#7556AF"));
        button2.setEnabled(true);
        button2.setBackgroundColor(Color.parseColor("#488CC4"));
        button3.setEnabled(true);
        button3.setBackgroundColor(Color.parseColor("#4E964F"));
        findViewById(R.id.grid).setVisibility(View.VISIBLE);
    }


    public void gameOverAnimation(){
        if(numberOfQuestions!=0 && ((double)score/numberOfQuestions) * 100.0 >= 65.0)
            winAnimation();
        else
            looseAnimation();
    }

    public void looseAnimation(){
        resultTextView.setText("You loose");
        new CountDownTimer(2500, 250){
            boolean doAnimationOne = true;

            @SuppressLint("NewApi")
            @Override
            public void onTick(long millisUntilFinished) {

                if (doAnimationOne) {
                    scoreTextView.animate().translationYBy(-35).translationXBy(-53).setDuration(250);
                    doAnimationOne = false;
                } else {
                    scoreTextView.animate().translationYBy(35).translationXBy(-53).setDuration(250);
                    doAnimationOne = true;
                }
            }
            @Override
            public void onFinish() {
                goButton.setVisibility(View.VISIBLE);
                goButton.setText("Play Again?");
            }
        }.start();
    }

    public void winAnimation(){
        scoreTextView.animate().translationX(-520).setDuration(300);
        resultTextView.setText("You win");
        new CountDownTimer(4000, 500){
            boolean doAnimationOne = true;

            @SuppressLint("NewApi")
            @Override
            public void onTick(long millisUntilFinished) {
                ColorDrawable[] buttonColor = {(ColorDrawable) button0.getBackground(),
                        (ColorDrawable) button1.getBackground(),
                        (ColorDrawable) button2.getBackground(),
                        (ColorDrawable) button3.getBackground()};

                button0.setBackground(buttonColor[2]);
                button1.setBackground(buttonColor[0]);
                button2.setBackground(buttonColor[3]);
                button3.setBackground(buttonColor[1]);

                if(doAnimationOne) {
                    resultTextView.animate().alpha(0).setDuration(500);
                    scoreTextView.animate().scaleX(0.50f).scaleY(0.50f).setDuration(500);
                    doAnimationOne = false;
                }
                else {
                    resultTextView.animate().alpha(1).setDuration(500);
                    scoreTextView.animate().scaleX(1).scaleY(1).setDuration(500);
                    doAnimationOne = true;
                }
            }
            @Override
            public void onFinish() {
                goButton.setVisibility(View.VISIBLE);
                goButton.setText("Play Again?");
            }
        }.start();
    }

    public void newQuestion(){
        Random rand = new Random();
        int value1 = rand.nextInt(21);
        int value2 = rand.nextInt(21);

        sumTextView.setText(String.format("%d + %d", value1, value2));

        locationOfCorrectAnswer = rand.nextInt(4);

        answers.clear();
        for(int x = 0; x < 4; x++){
            if(x == locationOfCorrectAnswer)
                answers.add(value1 + value2);
            else {
                int wrongAnswer = rand.nextInt(41);
                while(wrongAnswer == value1 + value2 || (!answers.isEmpty() && answers.contains(wrongAnswer))) {
                    wrongAnswer = rand.nextInt(41);
                }

                answers.add(rand.nextInt(41));
            }
        }

        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
    }

    public void chooseAnswer(View view){
        resultTextView.setVisibility(View.VISIBLE);
        resultTextView.setAlpha(1);
        if(Integer.toString(locationOfCorrectAnswer).equals(view.getTag().toString())) {

            resultTextView.setText("Correct");
            resultTextView.animate().alpha(0).setDuration(1000);
            score++;
        }
        else
            resultTextView.setText("Wrong");

        numberOfQuestions++;
        scoreTextView.setText(String.format("%d/%d",score, numberOfQuestions));
        newQuestion();
    }
}
