package com.example.myfirstproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyCanvas extends View {

    Paint paint, paint2;
    private static final int RECT_WIDTH = 100;
    private static final int RECT_HEIGHT = 100;
    private static int RECT_MOVE_INCREMENT = 5; // Smaller increments for smoother motion
    private static final int FRAME_DELAY = 16; // Approximately 60 frames per second
    public int powerupcollision=0, coll1 = 0,a=0;
    MyRect obj;
    public  List<Rect> powerup = new ArrayList<>();
    Paint obstpaint, paint3, scorePaint;
    private final List<Rect> rectangles = new ArrayList<>();
    public MyRect obst;
    public int score = 0,b,framecount=0, collisioncounter = 0;
    Random random;
    public float xpos1, xpos2, dWidth, dHeight;
    boolean canAddRectangle = true;
    Runnable runnable;
    Handler handler;
    boolean collisionDetected = false;
    private float TomY = dHeight + 2600;

    public MyCanvas(Context context) {
        super(context);
        init(context);
    }

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
    }

    private void init(Context context) {
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        scorePaint = new Paint();
        scorePaint.setTextAlign(Paint.Align.RIGHT);
        scorePaint.setTextSize(60);
        scorePaint.setColor(Color.BLACK);

        paint3 = new Paint();
        paint3.setColor(Color.RED);

        random = new Random(); // Initialize random

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                moveRectangles();
                movepowerup();
                framecount++;
                if (canAddRectangle) {
                    addRectangle();
                    canAddRectangle = false;
                    addpower();
                }


                invalidate();
                handler.postDelayed(this, FRAME_DELAY);
            }
        };

        handler.post(runnable);
    }

    private void addPowerUp(){


    }

    private void addRectangle() {
        int n = random.nextInt(3);
        int left = (int) ((dWidth * ((n * 6) + 1)) / 18); // Ensure the rectangle is within the view bounds
        int top = 10;
        int right = (int) ((dWidth * ((n * 6) + 5)) / 18); // Rectangle width
        int bottom = (int) (dHeight-2250); // Rectangle height
        rectangles.add(new Rect(left, top, right, bottom));
    }

    private void addpower(){
        int n = random.nextInt(3);
        int left = (int) ((dWidth * ((n * 5)+2) ) / 15); // Ensure the rectangle is within the view bounds
        int top = (int)(dHeight/16);
        int right = (int) ((dWidth * (n * (5) + 3)) / 15); // Rectangle width
        int bottom = (int) (dHeight/10); // Rectangle height
        powerup.add(new Rect(left, top, right, bottom)); // power up is the list name and Rect is the objects from the class Rect and tis present in the array powerup
    }

    private void movepowerup(){
        List<Rect> powerupstoremove = new ArrayList<>();
        for(Rect a : powerup){
            a.offset(0,RECT_MOVE_INCREMENT);
            if(a.top>dHeight){
                powerupstoremove.add(a);
            }
        }
        powerup.removeAll(powerupstoremove);
    }

    private void moveRectangles() {
        List<Rect> rectanglesToRemove = new ArrayList<>();
        for (Rect rect : rectangles) {
            rect.offset(0, RECT_MOVE_INCREMENT);
            if (rect.top > dHeight) {
                rectanglesToRemove.add(rect);
                score += 1;
            }
        }
        rectangles.removeAll(rectanglesToRemove);

        // Check if the last rectangle has crossed one-third of the screen height
        if (!rectangles.isEmpty() && rectangles.get(rectangles.size() - 1).top > dHeight / 3) {
            canAddRectangle = true;
        }

        // If all rectangles are removed, allow new rectangle to be added
        if (rectangles.isEmpty()) {
            canAddRectangle = true;
        }

        if(framecount%250==0){
            RECT_MOVE_INCREMENT+=5;
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        paint = new Paint();
        paint2 = new Paint();
        obstpaint = new Paint();
        Paint poweruppaint = new Paint();
        poweruppaint.setColor(Color.YELLOW);


        obstpaint.setColor(Color.BLACK);
        paint2.setColor(Color.GREEN);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20);

        canvas.drawCircle(xpos2, TomY, 100, paint); // Tom
        canvas.drawCircle(xpos2, dHeight - 400, 70, paint2); // Jerry
        canvas.drawLine(dWidth / 3, 0, dWidth / 3, dHeight, paint);
        canvas.drawLine(dWidth * 2 / 3, 0, dWidth * 2 / 3, dHeight, paint);

        collisionDetected = false; // Reset collision detection

        for (Rect rect : rectangles) {
            canvas.drawRect(rect, paint3);

            // Check for collision with green circle (Jerry)
            if (Rect.intersects(rect, new Rect((int) (xpos2 - 70), (int) (dHeight - 470), (int) (xpos2 + 70), (int) (dHeight - 330)))) {
                collisionDetected = true;
                collisioncounter += 1;
            }
        }

        for(Rect o : powerup){
            canvas.drawRect(o,poweruppaint);
            if(Rect.intersects(o,new Rect((int)(xpos2-70),(int)(dHeight-400),(int)(xpos2+70),(int)(dHeight-330)))){
                powerupcollision++;

            }
        }

        if (collisionDetected) {

            if (collisioncounter < 1) { // no collisions
                TomY = dHeight - 100;

            }

            if (collisioncounter == 1) { //first collision, note that here a gets incremented only one time since collision counter gets updated only once.
                TomY = dHeight - 220;
                a++;
                b=framecount;

            }

            if(collisioncounter>1 && framecount>b+59){ // we add 59 so that it does not stop immeditely after the first collision!!! Try removing that 59 and check. also 59 represents the amount of frames taken to pass through the rectangle when the speed is 10 ie lowest.
                stop();
            }

        }

        canvas.drawText("CollisionCounter: " + collisioncounter, dWidth - 100, 100, scorePaint);
        canvas.drawText("Speed: " + RECT_MOVE_INCREMENT, dWidth - 100, 200, scorePaint);
        canvas.drawText("FrameCount " + framecount, dWidth - 100, 300, scorePaint);
        canvas.drawText("a: " + a, dWidth - 100, 400, scorePaint);
        canvas.drawText("Frame of first collision: " + b, dWidth - 100, 500, scorePaint);
        canvas.drawText("Score: " + score, dWidth - 100, 600, scorePaint);
        canvas.drawText("power coll: " + powerupcollision, dWidth - 100, 700, scorePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        xpos1 = event.getX();

        if (xpos1 < dWidth / 3) {
            xpos2 = dWidth / 6;
        } else if (xpos1 > dWidth / 3 && xpos1 < (dWidth * 2) / 3) {
            xpos2 = dWidth / 2;
        } else if (xpos1 > (dWidth * 2) / 3) {
            xpos2 = (dWidth * 5) / 6;
        }

        invalidate();
        return super.onTouchEvent(event);
    }
}
