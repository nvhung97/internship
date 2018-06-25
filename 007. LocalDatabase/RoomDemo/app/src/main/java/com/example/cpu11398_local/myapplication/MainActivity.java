package com.example.cpu11398_local.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String TAG = "Room";

    StudentDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                db.studentDao().insert(
                        new Student(
                                "1511360",
                                "Nguyễn Văn Hùng",
                                21)
                );
            }
        }).start();

        /**
         * getAll
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Student> studentList = db.studentDao().getAll();
                for (int i = 0; i < studentList.size(); ++i) {
                    Log.e(
                            TAG,
                            "id: " + studentList.get(i).getStudentId()
                                    + ", name: " + studentList.get(i).getStudentName()
                                    + ", age: " +studentList.get(i).getStudentAge()
                     );
                }
            }
        }).start();

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
    }
}
