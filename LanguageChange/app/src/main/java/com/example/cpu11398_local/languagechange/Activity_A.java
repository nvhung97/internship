package com.example.cpu11398_local.languagechange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Locale;

public class Activity_A extends AppCompatActivity {

    Button activity_a_setting;

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String strLang = sharedPreferences.getString("language","en");
        Locale locale = new Locale(strLang);
        Locale.setDefault(locale);
        Resources resources = newBase.getResources();
        Configuration configuration = resources.getConfiguration();
        //configuration.locale = locale;
        //resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        configuration.setLocale(locale);
        newBase.createConfigurationContext(configuration);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__a);

        activity_a_setting = findViewById(R.id.activity_a_setting);

        activity_a_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Activity_A.this, Activity_B.class));
                RelativeLayout frame1 = (RelativeLayout) findViewById(R.id.smiley);
                frame1.getLayoutParams().height =460;
                frame1.getLayoutParams().width = 350;
                frame1.requestLayout();
            }
        });
    }
}
