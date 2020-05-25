package com.example.bubbleapper;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.Random;

public class BubbleView extends AppCompatImageView implements View.OnTouchListener {

    private ArrayList<Bubble> bubbleList;
    private final int DELAY = 16; //miliseconds
    private Paint myPaint = new Paint();
    private Handler h;

    public BubbleView (Context context, AttributeSet attrs){
        super(context, attrs);
        bubbleList = new ArrayList<Bubble>();
        myPaint.setColor(Color.WHITE);
        h = new Handler(); //just as timer in eclipse

        this.setOnTouchListener(this);

    }
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            //update all the bubbles
            for (Bubble bubble:bubbleList){
                bubble.update();
            }
            //redraw the screen
            invalidate();

        }
    };
    protected void onDraw(Canvas c){
       /* //Test the ability to draw bubbles
        for (int x = 0; x<100;x++){
            bubbleList.add(new Bubble((int)(Math.random()*800),(int)(Math.random()*1000),100));} */
        for (Bubble bubble:bubbleList){
            myPaint.setColor(bubble.color);
            c.drawOval(bubble.x-bubble.size/2,bubble.y-bubble.size/2,bubble.x + bubble.size/2, bubble.y + bubble.size/2,
                    myPaint);
        }
        myPaint.setColor(Color.WHITE);
        c.drawText("Count: " + bubbleList.size(),5,15,myPaint);
        h.postDelayed(r, DELAY); //similar to timer
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //handle multi-touch events
        if (event.getPointerCount()>1)
            for (int n =0; n< event.getPointerCount();n++){
            bubbleList.add(new Bubble((int)event.getX(n),(int)event.getY(n),(int)(Math.random()+50)));
            }
        else { //Grouping bubbles with single touch
            bubbleList.add(new Bubble((int)event.getX(),(int)event.getY(),(int)(Math.random()+50)));
            if (bubbleList.size() > 1 ){ //if there is existing bubble it will be grouped with it
                bubbleList.get(bubbleList.size() - 1).xspeed = bubbleList.get(bubbleList.size() -2).xspeed;
                bubbleList.get(bubbleList.size() - 1).yspeed = bubbleList.get(bubbleList.size() -2).yspeed;
            }
        }
        return true;
    }


    private class Bubble {
            // A bubble need an x, y location and a size
            public int x;
            public int y;
            public int size;
            public int color;
            public int xspeed;
            public int yspeed;
            private final int MAX_SPEED = 15;

            public Bubble(int newX, int newY, int newSize) {
                x= newX;
                y= newY;
                size = newSize;

                xspeed = (int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED);
                yspeed = (int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED);

                color = getRandomColor();

            }
            public void update() {
                x += xspeed;
                y += yspeed;

                // collision detection with edges of the panel
                if (x < 0 || x>getWidth() ) {
                    xspeed= -1 * xspeed;
                }
                if (y <0 || y>getHeight() ) {
                    yspeed = -1 * yspeed;
                } if (xspeed == 0 && yspeed== 0) {
                    xspeed = (int) (Math.random()+1);
                    yspeed = (int) (Math.random()+1);
                }

            }
        }
    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

}
