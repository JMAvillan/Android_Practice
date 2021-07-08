package com.example.multipleactivitydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        int age = intent.getIntExtra("Age", 0);
        Toast.makeText(this, Integer.toString(age), Toast.LENGTH_SHORT).show();

    }


    public void goBack(View view){
        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //startActivity(intent);
        finish();
    }
}
