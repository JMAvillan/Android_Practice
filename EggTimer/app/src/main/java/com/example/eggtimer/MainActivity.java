package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView countDownTextView;
    SeekBar seekBar;
    Button activateButton;
    CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer;
    boolean activated=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        countDownTextView = findViewById(R.id.countDownTextView);
        activateButton = findViewById(R.id.activateButton);

        int maxTime = 600_000; //10 minutes
        int startingPosition = 30_000; // 30 seconds
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(maxTime);
        seekBar.setProgress(startingPosition);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DateFormat formatter = new SimpleDateFormat("mm:ss", Locale.US);
                // formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                countDownTextView.setText(formatter.format(new Date(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void startTimer(){
        final int countDownInterval =1000;

        countDownTimer = new CountDownTimer(seekBar.getProgress(), countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                DateFormat formatter = new SimpleDateFormat("mm:ss");
                // formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                countDownTextView.setText(formatter.format(new Date(millisUntilFinished)));

                seekBar.setProgress((int) millisUntilFinished);
            }

            @Override
            public void onFinish() {
                resetDisplay();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
                mediaPlayer.start();
                new CountDownTimer(3500, 1000) {
                    public void onTick(long millisUntilFinished){ /*Do nothing on tick*/}
                    public void onFinish(){mediaPlayer.stop();}
                }.start();

            }
        }.start();
    }

    public void activateButton(View view){
        if(!activated){
            if(seekBar.getProgress() != 0){
            Log.i("Update", "Countdown activated.");
            seekBar.setEnabled(false);
            activateButton.setText("Stop");
            startTimer();
            activated = true;
            }
        }
        else {
            resetDisplay();
        }
    }

    public void resetDisplay(){
        Log.i("Update", "Countdown deactivated.");
        int defaultStartPosition = 30_000;

        countDownTimer.cancel();
        seekBar.setEnabled(true);
        seekBar.setProgress(defaultStartPosition);
        activateButton.setText("Start");
        activated = false;
    }
}
