package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final double EURO_TO_DOLLAR = 1.10;
    private final double DOLLAR_TO_EURO = 0.91;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioButton dollar  = findViewById(R.id.dollarButton);
        dollar.performClick();
    }

    public void convert(View view) {

        EditText editText = findViewById(R.id.editText);

        double currencyAmount =  Double.parseDouble(editText.getText().toString());

        String message = getConvertionMessage(currencyAmount);

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void changeHint(View view){
        EditText editText = findViewById(R.id.editText);
        RadioGroup rg = findViewById(R.id.buttonGroup);

        String hint = String.format(rg.getCheckedRadioButtonId() == findViewById(R.id.dollarButton).getId() ? "Enter dollar amount..." : "Enter euro amount..." );
        editText.setHint(hint);

    }



    public String getConvertionMessage(double money){
        RadioGroup rg = findViewById(R.id.buttonGroup);
        if(rg.getCheckedRadioButtonId() == findViewById(R.id.dollarButton).getId())
            return String.format("$%.2f is €%.2f", money, money * DOLLAR_TO_EURO);
        else
            return String.format("€%.2f is $%.2f", money, money * EURO_TO_DOLLAR);

    }

}
