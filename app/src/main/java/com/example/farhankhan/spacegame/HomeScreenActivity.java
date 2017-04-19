package com.example.farhankhan.spacegame;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home_screen);

        final ImageButton playButton = (ImageButton) findViewById(R.id.playImageButton);
        final ImageButton leaderBoardB = (ImageButton) findViewById(R.id.leaderBoardImageButton);
        final ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsImageButton);

        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(HomeScreenActivity.this, GameActivity.class));
            }
        });

        leaderBoardB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               startActivity(new Intent(HomeScreenActivity.this, LeaderBoardActivity.class));
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, SettingsActivity.class));
            }
        });

    }
}
