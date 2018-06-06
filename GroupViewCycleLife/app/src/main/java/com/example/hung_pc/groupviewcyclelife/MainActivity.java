package com.example.hung_pc.groupviewcyclelife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout relativeLayout = findViewById(R.id.root);
                relativeLayout.getLayoutParams().height = 100;
                relativeLayout.getLayoutParams().width = 100;
                relativeLayout.requestLayout();
            }
        });

    }
}
