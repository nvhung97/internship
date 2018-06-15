package com.example.cpu11398_local.threadmanager.ThreadTypes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.cpu11398_local.threadmanager.R;

public class ActivityIntentService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);
        startService(new Intent(this, MyIntentService.class));
    }
}
