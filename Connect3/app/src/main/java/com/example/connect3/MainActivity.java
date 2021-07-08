package com.example.connect3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private boolean playerOneTurn = true;
    private Map gameTable  = new HashMap<String, Boolean>();
    private Map gamePieces = new HashMap<String,ImageView>();
    private final int[][] WIN_CONDITIONS = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6},
                                            {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};
    private int yellowPieces = 0;
    private int redPieces = 0;
    private int occupiedSpaces =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for(int x =1; x<=9;x++)
        {
            String viewName =  String.format("boardSegment%d",x);
            gameTable.put(viewName, true);
            ImageView imgView = (ImageView)findViewById(getResources().getIdentifier(viewName,"id", getPackageName()));
            gamePieces.put(viewName, imgView );


    }

        Log.i("Important", gamePieces.toString());
        Log.i("Important",gameTable.toString());
    }

    public void placeToken(View view){

        ImageView boardSegment = findViewById(view.getId());

        String viewName = getResources().getResourceName(boardSegment.getId());
        viewName = viewName.substring(viewName.lastIndexOf("/") +1);
        boolean availableSegment = (boolean)gameTable.get(viewName);            //returns the boolean value of the viewName key
        
        
        
        if(playerOneTurn && availableSegment) {
            //************************ANIMATION*************************************
            boardSegment.setY(-500);                                             //*
            boardSegment.setImageResource(R.drawable.yellow);                    //*
            boardSegment.animate().translationY(0).setDuration(300);             //*
            //**********************************************************************

            boardSegment.setTag("yellow");      //set tag to identify that the boardSegment has a yellow token

            playerOneTurn = false;              //change the player's turn
            gameTable.put(viewName, false);     //change the availability to false
            occupiedSpaces++;
            if(++yellowPieces >= 3)
            {

                Log.i("Important", "Starting validation for yellow");
                if(validateWinCondition(boardSegment.getTag().toString()))
                    results("Player One Wins");
                else if(occupiedSpaces ==9)
                    results("Tied");
            }

        }
        else if(availableSegment)
        {
            //************************ANIMATION*************************************
            boardSegment.setY(-500);                                             //*
            boardSegment.setImageResource(R.drawable.red);                       //*
            boardSegment.animate().translationY(0).setDuration(300);             //*
            //**********************************************************************


            boardSegment.setTag("red");         //set tag to identify that the boardSegment has a red token
            playerOneTurn = true;               //change the player's turn
            Log.i("Info", gameTable.toString());
            gameTable.put(viewName, false);     //change the availability to false
            Log.i("Info", gameTable.toString());
            occupiedSpaces++;
            if(++redPieces >= 3)
            {

                Log.i("Important","Starting validation for red");
                if(validateWinCondition("red"))
                    results("Player Two Wins");
                else if(occupiedSpaces ==9)
                    results("Tied");
            }

        }

    }

    public boolean validateWinCondition(String pieceName) {

        ArrayList keys =  new ArrayList();
        ImageView imgView;
        Map.Entry mapElement;

        //Getting an iterator
        Iterator gtIterator = gameTable.entrySet().iterator();

        //Iterate through hashmap
        while (gtIterator.hasNext()) {
            mapElement = (Map.Entry) gtIterator.next();
            if (!(boolean) mapElement.getValue())       //if the boardSegment has a token do the following
            {
                //Utilize the description tags to filter the values and only obtain the ones we want evaluated
                imgView = (ImageView) gamePieces.get(mapElement.getKey().toString());
                if(pieceName.equals(imgView.getTag().toString()))
                    keys.add(mapElement.getKey().toString());
            }
        }

        Log.i("Important",keys.toString());

        int[] check = new int[keys.size()];
        //abstract the index number from the string name
        for(int x =0; x < keys.size();x++)
        {
            String index = (String) keys.get(x);
            index = index.substring(index.lastIndexOf("t")+1);

            check[x] = Integer.parseInt(index)-1;
        }

        for(int x =0; x< check.length; x++)
         Log.i("Important",String.format("%d", check[x]));

        int row =0;
        int col=0;
                for(int index = 0; index < keys.size(); index++)
                {
                    Log.i("Important", String.format("check[%d] = %d | WIN_CONDITION[%d][%d] = %d", index,check[index], row, col, WIN_CONDITIONS[row][col]));
                    if((check[index] == WIN_CONDITIONS[row][col])) {
                        Log.i("Important", String.format("Value in index %d (%d) is equal to value in row =%d and col=%d (%d)", index, check[index], row, col, WIN_CONDITIONS[row][col]));
                        if (col == 2) {
                            Log.i("Important", String.format("Pieces in segments %d, %d, and %d are %s", WIN_CONDITIONS[row][0] + 1, WIN_CONDITIONS[row][1] + 1, WIN_CONDITIONS[row][2] + 1, pieceName));
                            return true;
                        } else {
                            Log.i("Important", "Increment col by 1");
                            col++;
                            if(index + 1 == keys.size())
                                index = -1;
                        }
                    }

                    else if(index + 1 == keys.size()){        //if the values in the specified indexes are not equal reset the loop and column and update the row value
                        Log.i("Important","Values on index not equal, increment row and col = 0 and index =0;");
                        row++;
                        col = 0;
                        index=(row == 8? keys.size() :-1);
                        Log.i("Important", String.format("row = %d col=%d index=%d", row, col, index));
                    }


                }
        return false;
    }

   public void results(String message){
       TextView winnerTextView = findViewById(R.id.winnerTextView);
       Button playAgain = findViewById(R.id.playAgain);

       winnerTextView.setText(message);
       winnerTextView.setScaleX(.10f);
       winnerTextView.setScaleX(.10f);


       winnerTextView.animate().scaleX(1).scaleY(1).alpha(1).setDuration(700);

       playAgain.setY(-150);
       playAgain.animate().translationY(0).alpha(1).setDuration(300);
       playAgain.setClickable(true);

       removeClickable();
    }


    public void removeClickable() {
        ImageView[] boardSegment = new ImageView[9];

        for (int x = 1; x <= 9; x++){
            String viewName =  String.format("boardSegment%d",x);
            boardSegment[x-1] = (ImageView)findViewById(getResources().getIdentifier(viewName,"id", getPackageName()));
            boardSegment[x-1].setClickable(false);
        }

    }

    public void resetGame(View view){
        TextView winnerTextView = findViewById(R.id.winnerTextView);
        Button playAgain = findViewById(R.id.playAgain);

        playAgain.setAlpha(0);
        winnerTextView.setAlpha(0);

        ImageView[] boardSegment = new ImageView[9];

        for (int x = 1; x <= 9; x++){
            String viewName =  String.format("boardSegment%d",x);
            boardSegment[x-1] = (ImageView)findViewById(getResources().getIdentifier(viewName,"id", getPackageName()));
            boardSegment[x-1].setClickable(false);
            boardSegment[x-1].setImageDrawable(null);
            gameTable.put(viewName, true);
        }


    }
}
