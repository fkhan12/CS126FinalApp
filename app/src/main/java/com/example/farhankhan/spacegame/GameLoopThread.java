package com.example.farhankhan.spacegame;

import android.graphics.Canvas;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by FarhanKhan on 4/3/2017.
 *
 * Code from this class based on tutorial video that provides the following link with code:
 * https://pastebin.com/qmM1yksX
 *
 */

public class GameLoopThread extends Thread {
    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public GameLoopThread(SurfaceHolder holder, GamePanel gamePanel){

        //constructor of thread
        super();
        this.surfaceHolder = holder;
        this.gamePanel = gamePanel;
    }


    @Override
    public void run(){
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        //each time we run through the game loop, we want it to take 1000/FPS ms
        long targetTime = 1000/FPS;

        while(running){
            startTime = System.nanoTime();
            canvas = null;

            //try locking the canvas for pixel editing
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    //every time we go through the game loop, we update the game once and draw the game once
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            }catch(Exception e){
                //if something goes wrong we want to fix it programmatically rather than letting the app handle it
                e.printStackTrace();
            }
            finally {
                if(canvas!=null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            //time in milliseconds passed to update and draw the canvas once
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime-timeMillis;

            if(waitTime < 0){
                waitTime = 0;
            }

            try{
                this.sleep(waitTime);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount == FPS){
                averageFPS = 1000/((totalTime/frameCount)/1_000_000);
                frameCount = 0;
                totalTime = 0;
                Log.d("Average FPS", " : " + averageFPS);
            }
        }

    }

    public void setRunning(boolean b){
        running = b;
    }
}
