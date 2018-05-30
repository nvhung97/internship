package com.example.cpu11398_local.activitycyclelife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static final String ACTIVITY_NAME = "Activity_A";
    TextView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w(ACTIVITY_NAME, "request Activity B");
                startActivity(new Intent(MainActivity.this, SubActivity.class));
            }
        });
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
}
