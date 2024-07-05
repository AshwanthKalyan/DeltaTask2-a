package com.example.myfirstproject;

public class MyRect {
    float left,right,top,bottom;

    public MyRect(float left,float top,float right,float bottom){

        this.bottom=bottom;
        this.top=top;
        this.left=left;
        this.right=right;
    }

    public float getBottom() {
        return bottom;
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }
}
