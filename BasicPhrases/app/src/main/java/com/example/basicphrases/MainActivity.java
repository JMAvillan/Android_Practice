package com.example.basicphrases;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    MediaPlayer phrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playPhrase(View view){

        MediaPlayer mediaPlayer = MediaPlayer.create(this, getResources().getIdentifier(view.getTag().toString(), "raw", getPackageName()));
        mediaPlayer.start();
    }
}
