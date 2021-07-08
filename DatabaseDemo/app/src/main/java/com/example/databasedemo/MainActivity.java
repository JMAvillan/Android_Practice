package com.example.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase myDataBase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
        myDataBase.execSQL("DROP TABLE users");
        myDataBase.execSQL("CREATE TABLE IF NOT EXISTS users(name VARCHAR, age INT(3), id INTEGER PRIMARY KEY)");
        myDataBase.execSQL("INSERT INTO users(name, age) VALUES ('Jose', 20)");
        myDataBase.execSQL("INSERT INTO users(name,age) VALUES ('Javniel', 21)");
        myDataBase.execSQL("INSERT INTO users(name,age) VALUES ('Dairy', 20)");
        myDataBase.execSQL("INSERT INTO users(name,age) VALUES ('Haysha', 39)");
        myDataBase.execSQL("INSERT INTO users(name,age) VALUES ('Jose', 18)");

        myDataBase.execSQL("DELETE FROM users WHERE name = 'Jose' AND id = 5");

        //Grab things from database using a query
        Cursor c = myDataBase.rawQuery("SELECT * FROM users", null);

        int nameIndex = c.getColumnIndex("name");
        int ageIndex = c.getColumnIndex("age");
        int idIndex = c.getColumnIndex("id");

        c.moveToFirst();
        while(!c.isAfterLast()){
            Log.i("UserResults - name", c.getString(nameIndex));
            Log.i("UserResults - age", Integer.toString(c.getInt(ageIndex)));
            Log.i("UserResults - id", Integer.toString(c.getInt(idIndex)));
            c.moveToNext();
        }

        /*SQLiteDatabase secondTable = this.openOrCreateDatabase("Events", MODE_PRIVATE, null);
        secondTable.execSQL("CREATE TABLE IF NOT EXISTS events (event VARCHAR, year INT(4))");
        secondTable.execSQL("INSERT INTO events(event, year) VALUES ('Milenieum', 2000)");
        secondTable.execSQL("INSERT INTO events(event, year) VALUES ('Started College', 2017)");

        Cursor c = secondTable.rawQuery("SELECT * FROM events", null);
        int eventIndex = c.getColumnIndex("event");
        int yearIndex = c.getColumnIndex("year");
        c.moveToFirst();

        while(!c.isAfterLast()){
            Log.i("Event", c.getString(eventIndex));
            Log.i("Year", Integer.toString(c.getInt(yearIndex)));
            c.moveToNext();
        }*/


    }
}
