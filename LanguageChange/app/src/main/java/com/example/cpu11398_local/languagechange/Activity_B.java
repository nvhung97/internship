package com.example.cpu11398_local.languagechange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class Activity_B extends AppCompatActivity {

    final String LANGUAGE_KEY = "language";
    final String LANGUAGE_ENGLISH = "en";
    final String LANGUAGE_VIETNAMESE = "vi";
    static String strLang = "en";

    Button  activity_b_change;
    Button  activity_b_next;

    protected void attachBaseContext(Context newBase) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String strLang = sharedPreferences.getString("language","en");
        Locale locale = new Locale(strLang);
        Locale.setDefault(locale);
        Resources resources = newBase.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        //configuration.setLocale(locale);
        //newBase.createConfigurationContext(configuration);
        super.attachBaseContext(newBase);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__b);

        activity_b_change = findViewById(R.id.activity_b_change);
        activity_b_next = findViewById(R.id.activity_b_next);

        activity_b_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Activity_B.this);
                if (strLang.equalsIgnoreCase(LANGUAGE_ENGLISH)) {
                    strLang = LANGUAGE_VIETNAMESE;
                    sharedPreferences.edit().putString(LANGUAGE_KEY,LANGUAGE_VIETNAMESE).commit();
                }
                else {
                    strLang = LANGUAGE_ENGLISH;
                    sharedPreferences.edit().putString(LANGUAGE_KEY,LANGUAGE_ENGLISH).commit();
                }
                Locale locale = new Locale(strLang);
                Locale.setDefault(locale);
                Resources resources = getResources();
                Configuration configuration = resources.getConfiguration();
                configuration.locale = locale;
                resources.updateConfiguration(configuration, resources.getDisplayMetrics());
                //configuration.setLocale(locale);
                //Activity_B.this.createConfigurationContext(configuration);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    recreate();
                } else {
                    final Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }
        });

        activity_b_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_B.this, Activity_C.class));
            }
        });
    }
}
