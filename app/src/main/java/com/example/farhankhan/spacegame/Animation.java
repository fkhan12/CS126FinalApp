package com.example.farhankhan.spacegame;

import android.graphics.Bitmap;

/**
 * Created by FarhanKhan on 4/4/2017.
 *
 * Based on tutorial video: https://www.youtube.com/watch?v=kGqKNpk6VJw
 */

public class Animation {

    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    //only want to play some animations once (i.e.explosion)
    private boolean playedOnce;

    public void setFrames(Bitmap[] frames){

        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();

    }

    public void setDelay(long d){
        delay = d;
    }

    public void setFrame(int i){
        currentFrame = i;
    }

    /**
     * helps determine which image in the array to return (display)
     */
    public void update(){
        long elapsed = (System.nanoTime() - startTime)/1_000_000;
        if(elapsed > delay){
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length){
            currentFrame = 0;
            playedOnce = true;
        }
    }

    /**
     * This method determines what the Player class is going to be drawing
     * @return
     */
    public Bitmap getImage(){
        return frames[currentFrame];
    }

    public int getFrame(){
        return currentFrame;
    }

    public boolean playedOnce(){
        return playedOnce;
    }

}
