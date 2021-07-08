package com.example.multipleactivitiesfriendsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String> friendNames = new ArrayList<>();

        friendNames.add("Javniel");
        friendNames.add("Gabriel");
        friendNames.add("Karlos");
        friendNames.add("Indio");
        friendNames.add("Emanuel");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendNames);

        ListView friendListView = findViewById(R.id.friendListView);
        friendListView.setAdapter(arrayAdapter);
        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("friendName", friendNames.get(position));
                startActivity(intent);
            }
        });



    }
}
