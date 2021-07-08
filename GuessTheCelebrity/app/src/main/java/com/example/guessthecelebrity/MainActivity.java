package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView celebImageView;

    ArrayList<String> celebURLs = new ArrayList<>();
    ArrayList<String> celebNames = new ArrayList<>();
    int chosenCeleb =0;
    int chosenCelebButton;

    public void chooseOption(View view){
        if(view.getTag().toString().equals(Integer.toString(chosenCelebButton)))
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();

        newCelebrity();
    }

    public void newCelebrity(){
        try{
            Random rand = new Random();

            chosenCeleb = rand.nextInt(celebURLs.size());

            ImageDownloader imageTask = new ImageDownloader();
            Bitmap celebImage = imageTask.execute(celebURLs.get(chosenCeleb)).get();
            celebImageView = findViewById(R.id.celebImageView);
            celebImageView.setImageBitmap(celebImage);

            chosenCelebButton = rand.nextInt(4);
            for(int x=0; x < 4;x++){
                int randomCeleb;
                Button button = findViewById(getResources().getIdentifier("button"+x, "id", getPackageName()));
                do {
                    randomCeleb = rand.nextInt(celebNames.size());
                }while(randomCeleb == chosenCeleb);
                if(x == chosenCelebButton) {
                    Log.i("Chosen Celeb", celebNames.get(chosenCeleb));
                    button.setText(celebNames.get(chosenCeleb));
                }else{
                    Log.i("Random Celeb", celebNames.get(randomCeleb));
                    button.setText(celebNames.get(randomCeleb));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls){

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result ="";
            URL url;
            HttpURLConnection urlConnection = null;

            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data !=-1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        String result = "";

        try{
            result = task.execute("https://www.forbes.com/lists/2012/celebrities/celebrity-100_2011.html").get();
           /// String[] splitResult = result.split("<div class=\"listedArticles\">");

            Log.i("Website", result);
            Pattern p = Pattern.compile("img src=\\\"(.*?)\"");
            Matcher m = p.matcher(result);

            while(m.find()){
                celebURLs.add(m.group(1));
                Log.i("URLs", m.group(1));
            }

            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(result);

            while(m.find()){
                celebNames.add(m.group(1));
                Log.i("Names", m.group(1));
            }

            Log.i("Sizes", String.format("Names: %d | URLs: %d", celebNames.size(), celebURLs.size()));

        }catch(Exception e){
            e.printStackTrace();
        }

        newCelebrity();

    }
}
