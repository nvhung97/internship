package com.example.cpu11398_local.swipebackdemo;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tom);

        MyView img_tom              = findViewById(R.id.img_tom);
        AnimationDrawable animation = (AnimationDrawable)getDrawable(R.drawable.img_tom_greeting);
        img_tom.setImageDrawable(animation);
        animation.start();
    }
}
