package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> locations = new ArrayList<>();
    static  ArrayAdapter<String> listAdapter;
    static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);
        //sharedPreferences.edit().remove("places").apply();
        //sharedPreferences.edit().remove("locations").apply();
        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();
        places.clear();
        latitudes.clear();
        longitudes.clear();
        locations.clear();

        try {
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places", ObjectSerializer.serialize(new ArrayList<String>())));

            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats", ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes  = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lons", ObjectSerializer.serialize(new ArrayList<String>())));
        }catch (Exception  e){
            e.printStackTrace();
        }

        if(places.size() > 0 && latitudes.size() > 0  && longitudes.size() > 0) {
            if(places.size() == latitudes.size() && places.size() == longitudes.size() ) {
                for (int i = 0; i < latitudes.size(); i++) {
                    locations.add(new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i))));
                }
            }
        } else{
            Log.i("First Time", "");
            places.add("Add a new place...");
            locations.add(new LatLng(0, 0));
        }

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("position1", Integer.toString(position));
                Intent goToMap  = new Intent(getApplicationContext(), MapsActivity.class);
                goToMap.putExtra("placeNumber", position);

                Log.i("position3", Integer.toString(goToMap.getIntExtra("placeNumber", -1)));
                startActivity(goToMap);
            }
        });


    }
}
