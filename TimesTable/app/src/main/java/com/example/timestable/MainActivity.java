package com.example.timestable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView timesTableView;

    public void generateTimesTable(int timesTableNumber){
        ArrayList<Integer> tableValues = new ArrayList<>();
        ArrayAdapter<Integer> tableValuesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tableValues);
        for(int x = 1; x <= 100; x++)
            tableValues.add(timesTableNumber * x);

        timesTableView.setAdapter(tableValuesAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        timesTableView = findViewById(R.id.timesTableView);

        int max = 100;
        int startingPosition = 10;
        SeekBar seekBar =  findViewById(R.id.seekBar);
        seekBar.setMax(max);
        seekBar.setProgress(startingPosition);
        generateTimesTable(startingPosition);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = 1;
                int timesTableNumber;
                if(progress < min)
                {
                    timesTableNumber = min;
                    seekBar.setProgress(min);
                }
                else
                    timesTableNumber = progress;

                generateTimesTable(timesTableNumber);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
}
