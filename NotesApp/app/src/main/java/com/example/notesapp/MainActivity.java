package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    static public ArrayList<String> arrayList = new ArrayList<>();
    static public ArrayAdapter<String> arrayAdapter;
    public SharedPreferences sharedPreferences;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Pull notes saved in sharedPreferences
        sharedPreferences = getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
        HashSet<String> set  = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        if(set == null) {
            //arrayList.add("Example Note");
        } else {
            arrayList = new ArrayList(set);
        }


        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , arrayList );

        listView = findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete File?")
                        .setMessage("Are you sure you want to delete this note file?")
                        .setPositiveButton("No", null)
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                arrayList.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                HashSet<String> set = new HashSet<>(MainActivity.arrayList);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                            }
                        }).show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent notePage = new Intent(getApplicationContext(), NotesPage.class);
                notePage.putExtra("noteId", position);
                startActivity(notePage);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        Intent newNote = new Intent(getApplicationContext(), NotesPage.class);
        startActivity(newNote);

        return true;
    }
}
