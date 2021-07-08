package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.HashSet;

public class NotesPage extends AppCompatActivity {

    private TextView textView;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_page);


        textView = findViewById(R.id.editText);
        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);
        if( noteId == -1){
            MainActivity.arrayList.add("");
            noteId = MainActivity.arrayList.size()-1;
        }
        else
            textView.setText(MainActivity.arrayList.get(noteId));

            textView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    MainActivity.arrayList.set(noteId, s.toString());
                    MainActivity.arrayAdapter.notifyDataSetChanged();


                    SharedPreferences sharedPreferences = getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);
                    HashSet<String> set = new HashSet<>(MainActivity.arrayList);
                    sharedPreferences.edit().putStringSet("notes", set).apply();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
    }
}
