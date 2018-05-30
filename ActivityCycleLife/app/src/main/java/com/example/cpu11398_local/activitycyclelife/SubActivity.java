package com.example.cpu11398_local.activitycyclelife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SubActivity extends AppCompatActivity {

    static final String ACTIVITY_NAME = "Activity_B";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Log.w(ACTIVITY_NAME, "onCreate");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(ACTIVITY_NAME, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(ACTIVITY_NAME, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(ACTIVITY_NAME, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(ACTIVITY_NAME, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(ACTIVITY_NAME, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w(ACTIVITY_NAME, "onRestart");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.w(ACTIVITY_NAME, "BackPressed");
    }

    @Override
    protected void onUserLeaveHint()
    {
        Log.d(ACTIVITY_NAME,"HomePressed");
        super.onUserLeaveHint();
    }
}
