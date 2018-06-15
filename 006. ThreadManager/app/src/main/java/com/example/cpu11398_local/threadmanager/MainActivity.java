package com.example.cpu11398_local.threadmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.cpu11398_local.threadmanager.ThreadTypes.ActivityAsyncTask;
import com.example.cpu11398_local.threadmanager.ThreadTypes.ActivityIntentService;
import com.example.cpu11398_local.threadmanager.ThreadTypes.ActivityRxJava;
import com.example.cpu11398_local.threadmanager.ThreadTypes.ActivityService;
import com.example.cpu11398_local.threadmanager.ThreadTypes.ActivityThread;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainThread";

    Button btn_set_loop;
    Button btn_thread;
    Button btn_async_task;
    Button btn_service;
    Button btn_intent_service;
    Button btn_rx_java;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_set_loop = findViewById(R.id.btn_set_loop);
        btn_thread = findViewById(R.id.btn_thread);
        btn_async_task = findViewById(R.id.btn_async_task);
        btn_service = findViewById(R.id.btn_service);
        btn_intent_service = findViewById(R.id.btn_intent_service);
        btn_rx_java = findViewById(R.id.btn_rx_java);

        btn_set_loop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isAllowLoop) {
                    Utils.isAllowLoop = false;
                    btn_set_loop.setText("Enable Loop");
                }
                else {
                    Utils.isAllowLoop = true;
                    btn_set_loop.setText("Disable Loop");
                }
            }
        });
        btn_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityThread.class));
            }
        });
        btn_async_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityAsyncTask.class));
            }
        });
        btn_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityService.class));
            }
        });
        btn_intent_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityIntentService.class));
            }
        });
        btn_rx_java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityRxJava.class));
            }
        });

        Utils.showLog(TAG, Thread.currentThread().getId(), TAG);
    }
}
