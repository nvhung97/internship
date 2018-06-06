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
        if (savedInstanceState != null){
            Log.w(ACTIVITY_NAME, "onCreate begin " + savedInstanceState);
        }
        else {
            Log.w(ACTIVITY_NAME, "onCreate begin");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_a);
        if (fragment == null) {
            Log.w(ACTIVITY_NAME, "onCreate add fragment A");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_a, new FragmentA());
            fragmentTransaction.commit();
        }
        else {
            Log.w(ACTIVITY_NAME, "onCreate fragment existed");
        }
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
