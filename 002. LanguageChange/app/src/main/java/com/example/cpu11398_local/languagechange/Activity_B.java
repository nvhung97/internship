package com.example.cpu11398_local.languagechange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Activity_B extends ActivityBase {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__b);

        findViewById(R.id.activity_b_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (LocateManager.getLocale(Activity_B.this).getLanguage()){
                    case LocateManager.LANGUAGE_ENGLISH:
                        LocateManager.setNewLocale(
                                Activity_B.this,
                                LocateManager.LANGUAGE_VIETNAMESE
                        );
                        break;
                    case LocateManager.LANGUAGE_VIETNAMESE:
                        LocateManager.setNewLocale(
                                Activity_B.this,
                                LocateManager.LANGUAGE_ENGLISH
                        );
                        break;
                }
                recreate();
            }
        });

        findViewById(R.id.activity_b_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_B.this, Activity_C.class));
            }
        });
    }
}
