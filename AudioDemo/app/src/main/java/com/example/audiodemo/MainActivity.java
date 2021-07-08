package com.example.audiodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    Timer progressTimer;
    SeekBar progressSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //give the audio manager access to the system's audio
        audioManager =(AudioManager) getSystemService(AUDIO_SERVICE);

        //get the maximum and current volume from the device
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        //instantiate mediaPlayer to control the sound
        mediaPlayer = MediaPlayer.create(this, R.raw.spaceship);

        //create seekBar to control the volume
        SeekBar volumeControl = findViewById(R.id.volumeSeekBar);

        //set the seekBar maximum value to the device's maximum volume
        volumeControl.setMax(maxVolume);
        //set the seekBar position to the current volume
        volumeControl.setProgress(currentVolume);

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("SeekBar changed", Integer.toString(progress));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        progressSeekBar = findViewById(R.id.progressSeekBar);

        //set the end of the seekBar to the maximum duration of the audio
        progressSeekBar.setMax(mediaPlayer.getDuration());

        progressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });

    }

    public void playSound(View view) {
        mediaPlayer.start();
        progressTimer =  new Timer();
        progressTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                progressSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        },0, 100);
    }

    public void pauseSound(View view) {
        mediaPlayer.pause();
        progressTimer.cancel();

    }
}
