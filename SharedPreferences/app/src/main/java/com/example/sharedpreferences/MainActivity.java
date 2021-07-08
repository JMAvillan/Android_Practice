package com.example.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.sharedpreferences", Context.MODE_PRIVATE);

        ArrayList<String> friends = new ArrayList<>();
        friends.add("Javniel");
        friends.add("Gabriel");
        friends.add("Dairy");

        try {
            sharedPreferences.edit().putString("Friends", ObjectSerializer.serialize(friends)).apply();

            Log.i("Serialized String", ObjectSerializer.serialize(friends));
        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayList<String> newFriends = new ArrayList<>();
        try{
        newFriends = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("Friends", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e){
            e.printStackTrace();
        }

        Log.i("New Friends", newFriends.toString());
        //sharedPreferences.edit().putString("username", "AvillanDz").apply();
        //String username = sharedPreferences.getString("username", "");
        //Log.i("This is a username", username);

    }
}
