package com.example.cpu11398_local.languagechange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Activity_A extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__a);

        findViewById(R.id.activity_a_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_A.this, Activity_B.class));
            }
        });
    }
}
