package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultTextView;

    public class DownloadTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection urlConnection=null;
            String result="";
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();


                while(data != -1){
                    char current = (char) data;
                    result+= current;
                    data = reader.read();
                }
                return result;
            }catch(Exception e){
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "Could not find weather.", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String message="";
            try{
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather content" ,weatherInfo);

                JSONArray jsonArray = new JSONArray(weatherInfo);

                for(int x=0; x< jsonArray.length(); x++) {
                    JSONObject jsonPart = jsonArray.getJSONObject(x);

                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                     if(!main.equals("") && !description.equals(""))
                         message += main + ": " + description + "\r\n";
                }
                if(!message.equals("")){
                    resultTextView.setText(message);
                }
                else
                    Toast.makeText(getApplicationContext(), "Could not find weather.", Toast.LENGTH_SHORT).show();

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not find weather.", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void getWeather(View view){
        try{
            String location = editText.getText().toString();

            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute("api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=dcd2fb10fa10dcb17971f7dc2e21b2b8");

            //close keyboard after pressing button
            InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);

    }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not find weather.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        resultTextView = findViewById(R.id.resultTextView);
    }
}
