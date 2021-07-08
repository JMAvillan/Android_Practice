package com.example.lamguagepreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

   private SharedPreferences sharedPreferences;
   private TextView languageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        languageTextView = findViewById(R.id.languageTextView);
        sharedPreferences = this.getSharedPreferences("com.example.lamguagepreferences", Context.MODE_PRIVATE);

        String preferredLanguage = sharedPreferences.getString("preferredLanguage", "");
        Log.i("Info", preferredLanguage);
        preferredLanguage = "";

        if (preferredLanguage.equals("")) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_btn_speak_now)
                    .setTitle("Choose Language")
                    .setMessage("Please choose your preferred language\nfor this application.")
                    .setPositiveButton("Spanish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            languageTextView.setText("Spanish");
                        }
                    })
                    .setNegativeButton("English", null)
                    .show();

            setLanguage((String) languageTextView.getText());
        }
        else
            languageTextView.setText(sharedPreferences.getString("preferredLanguage", ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.english:
                setLanguage("English");
                break;
            case R.id.spanish:
                setLanguage("Spanish");
                break;
            default:
                return false;
        }
        return true;
    }

    public void setLanguage(String language)
    {
        languageTextView.setText(language);
        try{
            sharedPreferences.edit().putString("preferredLanguage", language).apply();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
