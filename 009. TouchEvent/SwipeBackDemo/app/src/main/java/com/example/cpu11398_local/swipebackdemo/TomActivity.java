package com.example.cpu11398_local.swipebackdemo;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

public class TomActivity extends AppCompatActivity {

    ImageView img_tom;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tom);

        img_tom = findViewById(R.id.img_tom);

        img_tom.setOnTouchListener((v, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    img_tom.setImageResource(R.drawable.img_tom_tickled);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    img_tom.setImageResource(R.drawable.img_tom_wait_touch);
                    break;
            }
            return true;
        });

        greetingTom();
    }

    void greetingTom() {
        img_tom.setBackground(getDrawable(R.drawable.img_tom_greeting));
        AnimationDrawable animation = (AnimationDrawable)img_tom.getBackground();
        animation.start();
    }
}
