package com.example.farhankhan.spacegame;

/**
 * Class to be used in Fire base saving of users and highscores
 *
 * Created by FarhanKhan on 4/16/2017.
 */

public class User {

    public String username;
    public int highscore;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, int highscore){
        this.username = username;
        this.highscore = highscore;
    }

}
