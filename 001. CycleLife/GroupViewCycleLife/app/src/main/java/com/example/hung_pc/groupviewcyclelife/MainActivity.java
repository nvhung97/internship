package com.example.hung_pc.groupviewcyclelife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * resize LinearLayout
                 */
                LinearLayout linearLayout = findViewById(R.id.root);
                linearLayout.getLayoutParams().height = 500;
                linearLayout.getLayoutParams().width = 100;
                linearLayout.requestLayout();

                /**
                 * resize button
                 */
                /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        2.0f
                );
                button.setLayoutParams(params);*/
            }
        });

    }
}
