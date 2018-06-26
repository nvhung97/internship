package com.example.cpu11398_local.paperdemo;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    final String TAG = "Paper";

    ImageView   img;
    long        logTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);

        Paper.init(this);

        /**
         * Save
         * Recommend: Group all objects belong to the same type by List (for example) before save.
         */
       /* logTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; ++i) {
            Paper.book().write(
                    String.valueOf(i),
                    new Student(String.valueOf(i), "Nguyễn Văn A", i)
            );
            if ((i + 1) % 1000 == 0) {
                Log.e(TAG, "1000-record " + ((i + 1) / 1000) + "th");
            }
        }
        Log.e(TAG, "Save cost: " + (System.currentTimeMillis() - logTime));*/

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Load
                 */
                logTime = System.currentTimeMillis();
                for (int i = 0; i < 10000; ++i) {
                    Paper.book().read(String.valueOf(i));
                }
                Log.e(TAG, "Load cost: " + (System.currentTimeMillis() - logTime));
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
