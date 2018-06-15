package com.example.cpu11398_local.threadmanager.ThreadTypes;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.cpu11398_local.threadmanager.Utils;
import com.example.cpu11398_local.threadmanager.R;

public class ActivityThread extends AppCompatActivity {

    private final  String TAG = "Thread";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        new MyThread().start();
    }

    public class MyThread extends Thread {

        private int info = 0;

        public MyThread() {
            Toast.makeText(ActivityThread.this, "Created new Thread", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void run() {
            while (Utils.isAllowLoop) {
                Utils.showLog(TAG, Thread.currentThread().getId(), String.valueOf(++info));
                SystemClock.sleep(Utils.DELAY_TIME);
            }
        }
    }
}
