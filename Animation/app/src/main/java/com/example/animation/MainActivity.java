package com.example.animation;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    boolean bartIsShowing = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView bart = findViewById(R.id.bart);
        bart.performClick();
    }

    public void fade(View view){
        Log.i("Info", "It worked");
        ImageView bart = findViewById(R.id.bart);
        ImageView homer = findViewById(R.id.homer);

       // bart.animate().translationXBy(-2000).setDuration(500);
       // bart.animate().translationYBy(1000).setDuration(2000);
        // bart.animate().rotation(1800).alpha(0).setDuration(1000);
        //bart.animate().scaleX(0.5f).scaleY(0.5f).setDuration(1000);
        bart.animate().rotation(720).translationX(0).scaleX(1).scaleY(1).setDuration(2000);
    }


//    public void fade(View view){
//        Log.i("Info", "It worked");
//        ImageView bart = findViewById(R.id.bart);
//        ImageView homer = findViewById(R.id.homer);
//        Log.i("Info", String.format("%f",bart.getAlpha()));
//        if(bartIsShowing) {
//            bart.animate().alpha(0).setDuration(2000);
//            homer.animate().alpha(1).setDuration(2000);
//            bartIsShowing = false;
//        }
//        else
//        {
//            bart.animate().alpha(1).setDuration(2000);
//            homer.animate().alpha(0).setDuration(2000);
//            bartIsShowing = true;
//        }
//    }
}
