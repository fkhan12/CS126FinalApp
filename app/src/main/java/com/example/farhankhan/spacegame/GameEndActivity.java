package com.example.farhankhan.spacegame;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameEndActivity extends AppCompatActivity {

    TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        scoreView = (TextView) findViewById(R.id.scoreTextView);

        //score passed from GamePanel after player asteroid collision through intent (see prev lectures)
        //remove this variable and change to score from intent after adding asteroid collisions
        int score = 100;
        scoreView.setText(score + "");
        //set up shared preferences to save high score then if score is higher, update the database and shared preferences
        //use: https://firebase.google.com/docs/database/android/read-and-write
        final SharedPreferences settings = GameEndActivity.this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();
        final int defaultHighScore = Integer.parseInt(getResources().getString(R.string.pref_highscore_default));
        final String defaultUsername = getResources().getString(R.string.pref_username_default);
        final String pref_username_key = getResources().getString(R.string.pref_username_key);
        final String pref_highscore_key = getResources().getString(R.string.pref_highscore_key);

        //update shared preferences
        int savedHighScore = settings.getInt(pref_highscore_key, defaultHighScore);
        //savedHighScore has the default value so should set shared preferences to score no matter what
        if(savedHighScore == defaultHighScore){
            editor.putInt(pref_highscore_key, score);
            editor.commit();
        }
        //saved high score is not default so need to make check whether new is higher than saved or not
        else{
            if(score > savedHighScore){
                editor.putInt(pref_highscore_key, score);
                editor.commit();
            }
            else{
                editor.putInt(pref_highscore_key, savedHighScore);
                editor.commit();
            }
        }

        final int savedHighScoreUpdated = settings.getInt(pref_highscore_key, defaultHighScore);
        //check that username in shared preferences is not null, empty string, nor defaultUsername
        final String savedUsername = settings.getString(pref_username_key, defaultUsername);
        if(savedUsername != null && !(savedUsername.equals("")) && !(savedUsername.equals(defaultUsername))) {
            //update firebase
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference leaderboardRef = database.getReference("leaderboards");

            leaderboardRef.child(savedUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int value;
                    Object valObj = dataSnapshot.getValue();
                    if(valObj != null){
                        value = dataSnapshot.getValue(Integer.class);
                        //the saved high score is greater than the value in the database
                        if(savedHighScoreUpdated > value){
                            leaderboardRef.child(savedUsername).setValue(savedHighScoreUpdated);
                        }
                        else{
                            leaderboardRef.child(savedUsername).setValue(value);
                        }
                    }
                    //value is null for some reason (even though the username should have been initialized in settings) so just set to 0
                    else{
                        leaderboardRef.child(savedUsername).setValue(0);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
