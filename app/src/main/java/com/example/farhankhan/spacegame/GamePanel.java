package com.example.farhankhan.spacegame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by FarhanKhan on 4/3/2017.
 *
 * GamePanel is a type of SurfaceView
 * For now this class will respond to touch events, later on it will be adapted to tilt
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener{

    public static final int WIDTH = 517;
//public static final int WIDTH = 810;
    public static final int HEIGHT = 810;
//public static final int HEIGHT = 517;
    public static final int MOVE_SPEED = -5;

    private GameLoopThread thread;
    private Background bg;
    private Player player;

    //below two are my attempt at just drawing the borders right on the edges of the screen to see if the player will collide with them
    //everything with the following comment is this attempt: MYLEFTRIGHT
    private LeftBorder leftBorder;
    private RightBorder rightBorder;

    private ArrayList<LeftBorder> leftBorders;
    private ArrayList<RightBorder> rightBorders;

    //instance fields for registering and accessing the accelerometer
    private Sensor mSensor;
    private SensorManager sensorManager;

    public GamePanel(Context context){
        //SurfaceView's constructor
        super(context);

        //add the callback to the surfaceHolder to intercept events
        getHolder().addCallback(this);

        thread = new GameLoopThread(getHolder(), this);

        //below is attempted implementation of the accelerometer
        //create sensor manager
        sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        //create accelerometer sensor
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

        //stop thread when surface is destroyed here
        boolean retry = true;
        //to prevent an infinite loop, create a counter
        int counter = 0;
        while(retry && counter < 1000){
            counter++;
            try{
                thread.setRunning(false);
                thread.join();
                retry = false;
                sensorManager.unregisterListener(this);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        //set the background
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.space_image_asset));

        //instantiate the player -- all of below are different tries with different images and sizes
//        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player_many_frames_transparent_cropped), 1, 540, 3);
//        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player_many_frames_transparent_cropped), 415, 450, 3);
//        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player_many_frames_transparent_cropped_sized), 415, 413, 3);
//        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player), 610, 594, 1);
//        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player3_nofire_transp), 610, 594, 1);
        //note that getResources().getDrawable(R.drawable.image) is deprecated so was replaced with
        // ResourcesCompat.getDrawable, whose params here have the same effect as the former
        player = new Player(BitmapFactory.decodeResource(getResources(),
                  R.drawable.player3_nofire_transp_rescaled),
                  190,
                  151,
                  1,
                  getContext(), ResourcesCompat.getDrawable(getResources(), R.drawable.player3_nofire_transp_rescaled, null));

        //MYLEFTRIGHT
        int navBarHeight = 0;
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if(resourceId>0){//the device does not have physical navigation buttons
            navBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        //the height at which to draw the border image:
        int drawHeight = (int)(Resources.getSystem().getDisplayMetrics().heightPixels*(3.0/4)) - ResourcesCompat.getDrawable(getResources(), R.drawable.brick, null).getIntrinsicHeight() - navBarHeight;

        leftBorder = new LeftBorder(BitmapFactory.decodeResource(getResources(), R.drawable.border_walls_transparent), 2 - ResourcesCompat.getDrawable(getResources(), R.drawable.brick, null).getIntrinsicWidth(), drawHeight, 0);
        rightBorder = new RightBorder(BitmapFactory.decodeResource(getResources(), R.drawable.border_walls_transparent), WIDTH-1, drawHeight);

        leftBorders = new ArrayList<>();
        rightBorders = new ArrayList<>();


        //register sensor listener
        sensorManager.registerListener(this, mSensor, sensorManager.SENSOR_DELAY_GAME);

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //this is the first time the player is pressing down (the player is not playing yet)
            if(!player.getPlaying()){
                player.setPlaying(true);
            }
            //player is already playing
            player.setUp(true);
            return true;
        }
        //listen for touch release
        if(event.getAction() == MotionEvent.ACTION_UP){
            player.setUp(false);
            return true;
        }

        return super.onTouchEvent(event);

    }

    public void update(){

        //only update background and player if player is playing
        if(player.getPlaying()) {
            bg.update();
            player.update();

            //check for left border collision
            for(int i = 0; i < leftBorders.size(); i++){
                if(collision(leftBorders.get(i), player)){
                    player.setHitLeftBorder(true);
                }
                else{
                    player.setHitLeftBorder(false);
                }
            }

            //check for right border collision
            for (int i = 0; i < rightBorders.size(); i++){
                if(collision(rightBorders.get(i), player)){
                    player.setHitRightBorder(true);
                }
                else{
                    player.setHitRightBorder(false);
                }
            }

            //FIXME: bug in code somewhere; when player hits the border, stutters before going in opposite direction; stutters longer, the longer it touches the border
            if(collision(leftBorder, player)){
                player.setHitLeftBorder(true);
            }
            else{
                player.setHitLeftBorder(false);
            }

            if(collision(rightBorder, player)){
                player.setHitRightBorder(true);
            }
            else{
                player.setHitRightBorder(false);
            }

        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas){
        //need to create a scale factor to allow for higher resolution screens to properly render the background
        //getHeight() gives the height of the whole screen/surfaceView
        final float scaleFactorY = getHeight()/(float)HEIGHT;
        final float scaleFactorX = getWidth()/(float)WIDTH;
        if(canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);

            //MYLEFTRIGHT
            leftBorder.draw(canvas);
            rightBorder.draw(canvas);

            //return back to saved state to stop scaling after every call to draw
            canvas.restoreToCount(savedState);
        }
    }

    /**
     * checks for collision between two game objects (i.e. player and obstacle or border)
     * Based on code from video tutorial (physical code can be found here: https://pastebin.com/0zgi2NGc)
     * Documentation for the Rect.intersects method can be found here:
     * https://developer.android.com/reference/android/graphics/Rect.html#intersects(android.graphics.Rect,%20android.graphics.Rect)
     * @param a: one of the GameObjects to check for collision
     * @param b: the other GameObject to check for collision
     * @return: true if collided, false otherwise
     */
    public boolean collision(GameObject a, GameObject b){

        return Rect.intersects(a.getRectangle(), b.getRectangle());

    }

    /**
     * This method is used because of the implemented class, SensorEventListener. It will be used
     * for registering the accelerometer movements and translating that into player movements.
     * Will need to pass the x values into Player (and set up a new variable and adjust for this new variable).
     * Code should be similar at least in function to the above method, onTouchEvent.
     *
     * Called after a set delay that is indicated when registering the listener
     *
     * @param event: The sensor event that contains the sensor information on change
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        //later if have sensitivity option in settings, can adjust so that higher sensitivity equates to smaller zero'ed value
        //and lower sensitivity equates to higher zero'ed value (so more range of tilt from zero without movement)

        //the sensor values are stored in an array with x as the first value (0 index), y as second, and z last
        int currX = (int)event.values[0];
        player.setDelX(currX);

        Log.d("onSensorChanged", "currX: " + currX);
    }

    /**
     * Method required by the SensorEventListener class that was implemented above. It will likely
     * remain unused.
     *
     * @param sensor: A reference to the sensor whose accuracy has changed (in this case the accelerometer)
     * @param accuracy: The changed value of the accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
