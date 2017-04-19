package com.example.farhankhan.spacegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by FarhanKhan on 4/3/2017.
 *
 * This class is based on a tutorial video: https://www.youtube.com/watch?v=GPzTSpZwFoU
 */

public class Background {

    private Bitmap image;
    private int x = 0, y = 0, dy = 0;

    public Background(Bitmap res){
        image = res;
        dy = GamePanel.MOVE_SPEED;
    }

    public void update(){

        y-=dy;
        if(y>GamePanel.HEIGHT){
            y = 0;
        }

    }

    public void draw(Canvas canvas){

        //x, y set to 0
        canvas.drawBitmap(image, x, y, null);

        //if part of the image is off the screen, we need to compensate for that by drawing a second image after
        if(y>0){
            canvas.drawBitmap(image, x, y-GamePanel.HEIGHT, null);
        }

    }

}
