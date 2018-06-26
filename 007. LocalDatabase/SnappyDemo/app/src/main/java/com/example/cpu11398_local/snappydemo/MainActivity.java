package com.example.cpu11398_local.snappydemo;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.snappydb.DB;
import com.snappydb.DBFactory;

public class MainActivity extends AppCompatActivity {

    final String TAG = "SnappyDB";

    ImageView img;
    long logTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);

        /**
         * Insert
         */
        /*try {
            DB db = DBFactory.open(this);
            logTime = System.currentTimeMillis();
            for (int i = 0; i < 100000; ++i) {
                db.put(
                        String.valueOf(i),
                        new Student(String.valueOf(i), "Nguyễn Văn A", i)
                );
                if ((i + 1) % 1000 == 0) {
                    Log.e(TAG, "1000-record " + ((i + 1) / 1000) + "th");
                }
            }
            Log.e(TAG, "Save cost: " + (System.currentTimeMillis() - logTime));
            db.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }*/

        /**
         * Delete
         */
        /*try {
            DB db = DBFactory.open(this);
            logTime = System.currentTimeMillis();
            for (int i = 0; i < 100000; ++i) {
                db.del(String.valueOf(i));
            }
            Log.e(TAG, "Delete cost: " + (System.currentTimeMillis() - logTime));
            db.close();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }*/

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Load
                 */
                try {
                    DB db = DBFactory.open(MainActivity.this);
                    logTime = System.currentTimeMillis();
                    for (int i = 0; i < 100000; ++i) {
                        db.getObject(
                                String.valueOf(i),
                                Student.class
                        );
                    }
                    Log.e(TAG, "Load cost: " + (System.currentTimeMillis() - logTime));
                    db.close();
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
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
