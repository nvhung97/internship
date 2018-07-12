package com.example.cpu11398_local.swipebackdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViewById(R.id.btn_start).setOnClickListener(
                v -> startActivity(new Intent(WelcomeActivity.this, TomActivity.class))
        );
    }
}
