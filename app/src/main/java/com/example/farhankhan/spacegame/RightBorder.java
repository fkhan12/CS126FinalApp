package com.example.farhankhan.spacegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * LeftBorder and RightBorder are very similar and could technically be left in the same class but
 * it is easier to understand if they are separated out. Both of these classes are based on the
 * same video tutorials as the many of the other Game related classes:
 * https://www.youtube.com/watch?v=e2UfGK0XHaI&index=7&list=PLWweaDaGRHjvQlpLV0yZDmRKVBdy6rSlg
 *
 * Created by FarhanKhan on 4/11/2017.
 */

public class RightBorder extends GameObject{

    private Bitmap image;

    public RightBorder(Bitmap res, int x, int y){
        height = 200;
        width = 20;

        this.x = x;
        this.y = y;
        dx = GamePanel.MOVE_SPEED;

        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    public void update(){
        x += dx;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }

}
