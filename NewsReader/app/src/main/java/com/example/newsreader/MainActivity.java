package com.example.newsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> titles;
    private ArrayList<String> articleURL;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    SQLiteDatabase articlesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articlesDB = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);
       // articlesDB.execSQL("DROP TABLE articles");
        articlesDB.execSQL("CREATE TABLE IF NOT EXISTS articles (Id INTEGER PRIMARY KEY, articleId INTEGER, title VARCHAR, url VARCHAR)");

        DownloadTask task = new DownloadTask();
        try{
            task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
        }catch (Exception e){
            e.printStackTrace();
        }


        titles = new ArrayList<>();
        articleURL =  new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);

        listView = findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent webView = new Intent(getApplicationContext(), Article.class);
                webView.putExtra("articleURL", articleURL.get(position));
                startActivity(webView);
            }
        });
        updateListView();
    }

    public void updateListView(){
        Cursor c = articlesDB.rawQuery("SELECT * FROM articles", null);

        int urlIndex = c.getColumnIndex("url");
        int titleIndex = c.getColumnIndex("title");

        if(c.moveToFirst()){
            titles.clear();
            articleURL.clear();

            do{
                titles.add(c.getString(titleIndex));
                articleURL.add(c.getString(urlIndex));
            }while(c.moveToNext());

            arrayAdapter.notifyDataSetChanged();

        }

    }

    public class DownloadTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection urlConnection = null;
            String result= "";

            //Use the API to grab the ids
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream  = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();

                while(data != -1){
                    char current = (char) data;
                    result+= current;
                    data = reader.read();
                }
               //Log.i("UrlContent", result);

                JSONArray jsonArray = new JSONArray(result);
                int numberOfItems = 20;
                if(jsonArray.length()< 20)
                    numberOfItems = jsonArray.length();

                articlesDB.execSQL("DELETE FROM articles");

                //Use the given ids to obtain information and then extract the title and url
                for(int i =0; i < numberOfItems; i++){
                    String articleId = jsonArray.getString(i);
                    url = new URL("https://hacker-news.firebaseio.com/v0/item/" + articleId + ".json?print=pretty");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();

                    inputStream  = urlConnection.getInputStream();
                    reader = new InputStreamReader(inputStream);

                    data = reader.read();

                    String articleInfo = "";
                    while(data != -1){
                        char current = (char) data;
                        articleInfo+= current;
                        data = reader.read();
                    }
                    //Log.i("ArticleInfo", articleInfo);

                    JSONObject jsonObject = new JSONObject(articleInfo);
                    if(!jsonObject.isNull("title") && !jsonObject.isNull("url")){
                        String articleTitle =  jsonObject.getString("title");
                        String articleUrl = jsonObject.getString("url");
                        Log.i("Title and URL", articleTitle + " " +articleUrl);
/*
                        url = new URL(articleUrl);
                        urlConnection = (HttpsURLConnection)url.openConnection();
                        inputStream = urlConnection.getInputStream();
                        reader = new InputStreamReader(inputStream);
                        data = reader.read();
                        String articleContent = "";
                        while(data !=-1){
                            char current = (char) data;
                            articleContent += current;
                            data = reader.read();
                        }
                        */

                        //Log.i("Url", articleContent);
                        String sql = "INSERT INTO articles (articleId, title, url) VALUES (?, ?, ?)";
                        SQLiteStatement statement = articlesDB.compileStatement(sql);
                        statement.bindString(1, articleId);
                        statement.bindString(2, articleTitle);
                        statement.bindString(3, articleUrl);
                        statement.execute();
                    }
                }//end of for loop
                return result;
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateListView();
        }
    }

}
