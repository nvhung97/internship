package com.example.hung_pc.sqlitedemo;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    MySQLiteOpenHelper  mySQLiteOpenHelper;
    ImageView           img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);

        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);

        mySQLiteOpenHelper.insertStudent();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySQLiteOpenHelper.readStudent();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnimationDrawable animation = (AnimationDrawable) img.getBackground();
        animation.start();
    }
}
