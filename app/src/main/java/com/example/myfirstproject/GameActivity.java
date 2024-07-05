package com.example.myfirstproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an instance of MyCanvas and set it as the content view
        MyCanvas gameCanvas = new MyCanvas(this);
        setContentView(gameCanvas);
    }
}
