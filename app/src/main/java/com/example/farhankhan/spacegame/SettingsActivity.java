package com.example.farhankhan.spacegame;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);

        //get firebase reference
        final DatabaseReference leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboards");

        //read/write from shared preferences
        final SharedPreferences settings = SettingsActivity.this.getPreferences(Context.MODE_PRIVATE);
        final String defaultValueUsername = getResources().getString(R.string.pref_username_default);
        final int defaultValueHighScore = Integer.parseInt(getResources().getString(R.string.pref_highscore_default));
        final SharedPreferences.Editor editor = settings.edit();
        final String pref_username_key = getResources().getString(R.string.pref_username_key);
        final String pref_highscore_key = getResources().getString(R.string.pref_highscore_key);

        //if username is DEFAULTUSERNAME then don't write to the database (if user tries to use that then just say already taken)
        username = (TextView) findViewById(R.id.userNameView);

        //set username text view to saved username, if exists
        String saveduser = settings.getString(pref_username_key, defaultValueUsername);
        if(!(saveduser.equals(defaultValueUsername)) && saveduser != null && !(saveduser.equals(""))){
            username.setText(saveduser);
        }
        else{
            username.setText("No Username");
        }

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open the change username dialog onClick
                //DialogFragment dialogFrag = new ChangeUsernameDialogFragment();
                //dialogFrag.show(getFragmentManager(), "changeUsername");

                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.username_dialog);
                dialog.setTitle("Change username");

                final EditText usernameET = (EditText) dialog.findViewById(R.id.username);

                final Button saveB = (Button) dialog.findViewById(R.id.saveButton);
                final Button cancelB = (Button) dialog.findViewById(R.id.cancelButton);

                saveB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String changedUsername = usernameET.getText().toString();
                        leaderboardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //edit text is either null or empty so let the user know and do nothing
                                if(changedUsername == null || changedUsername.equals("")){
                                    Toast.makeText(getApplicationContext(), "Please enter a new username", Toast.LENGTH_LONG).show();
                                }
                                //the username selected already exists (or chose the default name which is there in case we add preferences later)
                                else if(dataSnapshot.hasChild(changedUsername) ||
                                        changedUsername.equals(getResources().getString(R.string.pref_username_default))){
                                    Toast.makeText(getApplicationContext(),
                                            "This username is already taken, please choose another one",
                                            Toast.LENGTH_LONG).show();
                                }
                                //username is valid and free to use
                                else{
                                    //username that is already saved in phone (if no username key initialized then gives default username
                                    final String prevStoredUsername = settings.getString(pref_username_key, defaultValueUsername);

                                    //if the username is not the default then do logic, else just add new username with saved high score or 0, if default (-1)
                                    if(!prevStoredUsername.equals(defaultValueUsername)) {
                                        leaderboardRef.child(prevStoredUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //copy value of existing high score in database
                                                int highScore = dataSnapshot.getValue(Integer.class);
                                                //make new username in database with the high score that was pulled from the original
                                                leaderboardRef.child(changedUsername).setValue(highScore);
                                                //delete child with old username
                                                leaderboardRef.child(prevStoredUsername).removeValue();
                                                //update shared preferences with new username
                                                editor.putString(pref_username_key, changedUsername);
                                                editor.commit();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    //highscore shared preferences only used for storage of score until username is made (ending activity should still update the highscore shared preferences and firebase after each game)
                                    else{
                                        //add new username to shared preferences
                                        editor.putString(pref_username_key, changedUsername);
                                        editor.commit();
                                        final int prevStoredHighscore = settings.getInt(pref_highscore_key, defaultValueHighScore);
                                        //if there is no score saved for high score then set value in firebase to 0 and set shared preferences to default
                                        if(prevStoredHighscore==defaultValueHighScore) {
                                            leaderboardRef.child(changedUsername).setValue(0);
                                            editor.putInt(pref_highscore_key, defaultValueHighScore);
                                            editor.commit();
                                        }
                                        //there is a highscore in shared preferences, use that for the firebase save
                                        else{
                                            leaderboardRef.child(changedUsername).setValue(prevStoredHighscore);
                                        }
                                    }
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

                cancelB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });



    }
}
