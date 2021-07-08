package com.example.multipleactivitydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToNext(View view){
        Intent intent  = new Intent(getApplicationContext(), SecondActivity.class);

        intent.putExtra("Age", 20 );
        startActivity(intent);
    }

}
