package com.example.farhankhan.spacegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameEndActivity extends AppCompatActivity {

    TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        scoreView = (TextView) findViewById(R.id.scoreTextView);

        //score passed from GamePanel after player asteroid collision through intent (see prev lectures)
        //scoreView.setText(score);
        //set up shared preferences to save high score then if score is higher, update the database and shared preferences
        //use: https://firebase.google.com/docs/database/android/read-and-write

    }
}
