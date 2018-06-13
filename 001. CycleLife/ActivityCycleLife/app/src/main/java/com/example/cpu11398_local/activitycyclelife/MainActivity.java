package com.example.cpu11398_local.activitycyclelife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static final String ACTIVITY_NAME = "Activity_A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            Log.w(ACTIVITY_NAME, "onCreate begin " + savedInstanceState);
        }
        else {
            Log.w(ACTIVITY_NAME, "onCreate begin");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w(ACTIVITY_NAME, "request Activity B begin");
                startActivity(new Intent(MainActivity.this, SubActivity.class));
                Log.w(ACTIVITY_NAME, "request Activity B finish");
            }
        });
        Log.w(ACTIVITY_NAME, "onCreate finish");
    }

    @Override
    protected void onStart() {
        Log.w(ACTIVITY_NAME, "onStart begin");
        super.onStart();
        Log.w(ACTIVITY_NAME, "onStart finish");
    }

    @Override
    protected void onRestart() {
        Log.w(ACTIVITY_NAME, "onRestart begin");
        super.onRestart();
        Log.w(ACTIVITY_NAME, "onRestart finish");
    }

    @Override
    protected void onResume() {
        Log.w(ACTIVITY_NAME, "onResume begin");
        super.onResume();
        Log.w(ACTIVITY_NAME, "onResume finish");
    }

    @Override
    protected void onPause() {
        Log.w(ACTIVITY_NAME, "onPause begin");
        super.onPause();
        Log.w(ACTIVITY_NAME, "onPause finish");
    }

    @Override
    protected void onStop() {
        Log.w(ACTIVITY_NAME, "onStop begin");
        super.onStop();
        Log.w(ACTIVITY_NAME, "onStop finish");
    }

    @Override
    protected void onDestroy() {
        Log.w(ACTIVITY_NAME, "onDestroy begin");
        super.onDestroy();
        Log.w(ACTIVITY_NAME, "onDestroy finish");
    }
}
