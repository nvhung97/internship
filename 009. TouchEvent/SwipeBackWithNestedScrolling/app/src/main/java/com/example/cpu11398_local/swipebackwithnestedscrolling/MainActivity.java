package com.example.cpu11398_local.swipebackwithnestedscrolling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start).setOnClickListener(
                v -> startActivity(new Intent(MainActivity.this, SubActivity.class))
        );
    }
}
