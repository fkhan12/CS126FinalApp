package com.example.farhankhan.spacegame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

/**
 * Created by FarhanKhan on 4/4/2017.
 *
 * Based on tutorial: https://www.youtube.com/watch?v=kGqKNpk6VJw
 */

public class Player extends GameObject {

    private Bitmap spriteSheet;
    private int score;
    private double dxa; //only have acceleration in the left or right direction
    private boolean up;
    private boolean playing;
    private boolean hitLeftBorder;
    private boolean hitRightBorder;
    private Animation animation;
    private long startTime; //used to count for incrementing score

    //delX = 0 for no acceleration, negative delX gives rightwards acceleration, positive gives left
    private int delX; //used for determining the delta x movement calculated in the GamePanel by accelerometer measurements

    /**
     * player constructor creates the bitmap representation for the Player object
     * @param res: the bitmap representation of the png images in the drawable-nodpi folder
     * @param w: the width of the image
     * @param h: the height of the image
     * @param numFrames: the number of frames the animation has in the bitmap
     * @param context: the context of the activity, used only for height of screen and navBar for
     *               calculation in the y assignment
     * @param d: the Drawable being used to represent the player (may change based on settings later
     *         on); used in determining the height of the player image used and in y assignment calculation
     */
    public Player(Bitmap res, int w, int h, int numFrames, Context context, Drawable d){

        animation = new Animation();

        //all of the below player width/height calculations only work with a 1 frame of animation (static player)
        int playerHeight = d.getIntrinsicHeight();
        int playerWidth = d.getIntrinsicWidth();

        //it seems to draw from the top left of the image, width seems to be measured from the left of the screen
        //so need to offset the width of the background by half the width of the player
        x = GamePanel.WIDTH/2 - playerWidth/2;
//        x = 100;
//        y = 100;
//        y = 200;
        //it seems to draw from the top of the image because when image size changes, the position moves
        //the height also seems to be measured from the top of the screen
        //the below will give the height of the navigation bar which will be taken into account for y
        int navBarHeight = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if(resourceId>0){//the device does not have physical navigation buttons
            navBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        y = (int)(Resources.getSystem().getDisplayMetrics().heightPixels*(3.0/4)) - playerHeight - navBarHeight;
//        y = GamePanel.HEIGHT/2;
        dx = 0;
        score = 0;
        height = h;
        width = w;

        //create a Bitmap array that will store all the different sprites
        Bitmap[] image = new Bitmap[numFrames];
        spriteSheet = res;

        for(int i = 0; i < image.length; i++){
            image[i] = Bitmap.createBitmap(spriteSheet, 0, i*height, width, height);
//            image[i] = Bitmap.createBitmap(spriteSheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime();

        //used for accelerometer implementation
        delX = 0;

    }

    public void update(){
        //increment score
        long elapsed = (System.nanoTime()-startTime)/1_000_000;
        //every 1/10 of a second, the score increments by 1
        if(elapsed>100){
            score++;
            startTime = System.nanoTime();
        }
        animation.update();


        //below code is for the accelerometer implementation; have either below or 'up' in use otherwise won't work
        //if abs(currX) exceeds 6 then the phone is being tilted a lot so dx will = 14 in the respective direction
        //delX is negative, so move to right, no acceleration, constant speed
        if(delX < 0 && delX > -3){
            if(!hitRightBorder) {
                dx = 10;
            }
            //if hitting the left border then stop moving
            else{
                dx = 0;
            }
        }
        else if(delX <= -3 && delX > -6){
            if(!hitRightBorder){
                dx = 14;
            }
            else{
                dx = 0;
            }
        }
        //if phone is being severely tilted then will get a speed boost
        // (only b/c at that point the user is probably trying to dodge something fast)
        else if(delX <= -6){
            if(!hitRightBorder){
                dx = 18;
            }
            else{
                dx = 0;
            }
        }
        //delX is positive, so move to left, no acceleration, constant speed
        else if(delX > 0 && delX < 3){
            if(!hitLeftBorder) {
                dx = -10;
            }
            //if hitting the right border then stop moving
            else{
                dx = 0;
            }
        }
        else if(delX >= 3 && delX < 6){
            if(!hitLeftBorder){
                dx = -14;
            }
            else{
                dx = 0;
            }
        }
        else if(delX >= 6){
            if(!hitLeftBorder){
                dx = -18;
            }
            else{
                dx = 0;
            }
        }
        //delX is zero, so no movement, no acceleration, no speed
        else{
            dx = 0;
        }

/** //NOTE: Only keep either below in or above in, not both at same time
        //we are pressing down on the screen
        if(up
                && !hitLeftBorder//MYLEFTRIGHT: if this doesn't work then remove just the stuff on this line
                ){
            dx = (int)(dxa -= 1.1);
        }
        else if (!up && !hitRightBorder){ //MYLEFTRIGHT: was just else{...}, if doesn't work then revert this
            dx = (int)(dxa += 1.1);
        }

        //cap player speed
        if(dx > 14){
            dx = 14;
        }
        if(dx < -14){
            dx = -14;
        }

        //player has hit the left border
        if(hitLeftBorder && up){
            dx = 0;
//            x=0;
            //hitLeftBorder = false;
        }
        else if(hitLeftBorder && !up){
            dx = 1;
            hitLeftBorder = false;
        }

        //player has hit the right border
        if(hitRightBorder && !up){
            dx = 0;
            //hitRightBorder = false;
        }
        else if(hitRightBorder && up){
            dx = -1;
            hitRightBorder = false;
        }
*/
        x += dx*2;
//        y += dx*2;
        dx = 0;
    }

    public void draw(Canvas canvas){
        Log.d("Player", "draw(canvas) called");
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

    public int getScore(){
        return score;
    }

    public boolean getPlaying(){
        return playing;
    }

    public void setPlaying(boolean playing){
        this.playing = playing;
    }

    /**
     * when the screen is pressed, the player will move 'up'
     * TODO: change this method and other parts of the game later to account for vertical orientation and accelerometer use
     * @param b
     */
    public void setUp(boolean b){
        up = b;
    }

    public void setHitLeftBorder(boolean h){
        hitLeftBorder = h;
    }

    public void setHitRightBorder(boolean h){
        hitRightBorder = h;
    }

    public void resetDXA(){
        dxa = 0;
    }

    public void resetScore(){
        score = 0;
    }

    public void setDelX(int delx){
        delX = delx;
    }

}
