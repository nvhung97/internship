package com.example.cpu11398_local.myapplication;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String TAG = "Room";

    ImageView       img;
    StudentDatabase db;
    long            logTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);

        db = StudentDatabase.getInstance(this);

        /**
         * FindById
         */
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                Student student = db.studentDao().findById("1511360");
                    Log.e(
                            TAG,
                            "id: " + student.getStudentId()
                                    + ", name: " + student.getStudentName()
                                    + ", age: " +student.getStudentAge()
                    );
            }
        }).start();*/

        /**
         * Insert
         */
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                logTime = System.currentTimeMillis();
                for (int i = 0; i < 10000; ++i) {
                    db.studentDao().insert(
                            new Student(
                                    String.valueOf(i),
                                    "Nguyễn Văn A",
                                    i)
                    );
                    if ((i + 1) % 1000 == 0) {
                        Log.e(TAG, "1000-record " + ((i + 1) / 1000) + "th");
                    }
                }
                Log.e(TAG, "Save cost: " + (System.currentTimeMillis() - logTime));
            }
        }).start();*/

        /**
         * Delete
         */
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                db.studentDao().delete(
                        new Student(
                                "1511360",
                                "Nguyễn Văn Hùng",
                                21)
                );
            }
        }).start();*/

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * getAll
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        logTime = System.currentTimeMillis();
                        List<Student> studentList = db.studentDao().getAll();
                        Log.e(TAG, "Number of records: "+ studentList.size());
                        Log.e(TAG, "Load cost: " + (System.currentTimeMillis() - logTime));
                        /*for (int i = 0; i < studentList.size(); ++i) {
                            Log.e(
                                    TAG,
                                    "id: " + studentList.get(i).getStudentId()
                                            + ", name: " + studentList.get(i).getStudentName()
                                            + ", age: " +studentList.get(i).getStudentAge()
                             );
                        }*/
                    }
                }).start();
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
