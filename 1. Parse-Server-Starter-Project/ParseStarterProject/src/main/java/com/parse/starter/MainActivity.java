/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //save information in the parse server
/*
    ParseObject score = new ParseObject("Score");
    score.put("username","miko");
    score.put("score", 65);
    score.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if(e == null){
          Log.i("Success","We saved the score");
        }else
          e.printStackTrace();
      }
    });
     */


    //get information from the server
    /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
    query.getInBackground("2qZejuPggz", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {
        if(e == null && object != null){
          Log.i("username", object.getString("username"));
          Log.i("score", Integer.toString(object.getInt("score")));

          //update information from the server
          object.put("score", 89);
          object.saveInBackground();
          Log.i("username", object.getString("username"));
          Log.i("newscore", Integer.toString(object.getInt("score")));
        }
      }
    });
     */

    //example problem: Placing data and updating
    /*ParseObject tweet = new ParseObject("Tweet");
    //tweet.put("username", "avillan");
    //tweet.put("tweet", "Hello world!");
    tweet.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if(e == null){
          Log.i("OK!", "Save success");
        }else
          e.printStackTrace();
      }
    });

    ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
    query.getInBackground("FYFvaQzW5o", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject object, ParseException e) {
        if(e == null && object != null) {
          object.put("tweet", "Hello world! Im Jose!");
          object.saveInBackground();
        }
      }
    });
     */

    //get more than one item from the server
    /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
    query.whereEqualTo("username", "jose");// search for a particular object in the server
    query.setLimit(1); //set a limit for the number of results
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        if(e == null)
          if(objects.size() > 0)
           for(ParseObject object: objects){
             Log.i("username",object.getString("username"));
             Log.i("score",Integer.toString(object.getInt("score")));
            }
          }
        });
     */

    //example problem
    /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
    query.whereGreaterThan("score", 50);
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> objects, ParseException e) {
        if(e == null)
          if(objects.size()>0)
            for(ParseObject object: objects){
              Log.i("username", object.getString("username"));
              Log.i("scoreBefore", Integer.toString(object.getInt("score")));
              object.put("score", object.getInt("score")+20);
              Log.i("scoreAfter", Integer.toString(object.getInt("score")));
              object.saveInBackground();
            }
      }
    });*/

    //Sign up user
    /*ParseUser user = new ParseUser();
    user.setUsername("Avillan");
    user.setPassword("1234");

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if(e == null)
          Log.i("Sign Up", "Complete");
          else
            e.printStackTrace();
      }
    });*/


    //login a user
    /*ParseUser.logInInBackground("Avillan", "1234", new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if(user != null)
          Log.i("Log in", "Success");
        else
          e.printStackTrace();
      }
    });
     */

    //check if user is signed in and log them out
    if(ParseUser.getCurrentUser() != null)
      Log.i("Signed In", ParseUser.getCurrentUser().getUsername());
    else
      Log.i("No luck", "Not signed in");

    ParseUser.logOut();
    if(ParseUser.getCurrentUser() != null)
      Log.i("Signed In", ParseUser.getCurrentUser().getUsername());
    else
      Log.i("No luck", "Not signed in");

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}