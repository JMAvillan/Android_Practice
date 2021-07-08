package com.example.exampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickFunction(View view){
        EditText nameEditText = (EditText) findViewById(R.id.nameEditText);

        Toast.makeText(this,"Hello " + nameEditText.getText().toString(), Toast.LENGTH_SHORT).show();

        ImageView image = findViewById(R.id.imageView);

        image.setImageResource(R.drawable.cylinder);

    }
}
