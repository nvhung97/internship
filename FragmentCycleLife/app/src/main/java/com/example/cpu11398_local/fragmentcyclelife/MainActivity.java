package com.example.cpu11398_local.fragmentcyclelife;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    static final String ACTIVITY_NAME = "Cycle_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(ACTIVITY_NAME, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_a);
        if (fragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_a, new FragmentA());
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onStart() {
        Log.w(ACTIVITY_NAME, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.w(ACTIVITY_NAME, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.w(ACTIVITY_NAME, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.w(ACTIVITY_NAME, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.w(ACTIVITY_NAME, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.w(ACTIVITY_NAME, "onRestart");
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        Log.w(ACTIVITY_NAME, "BackPressed");
        super.onBackPressed();
    }
}
