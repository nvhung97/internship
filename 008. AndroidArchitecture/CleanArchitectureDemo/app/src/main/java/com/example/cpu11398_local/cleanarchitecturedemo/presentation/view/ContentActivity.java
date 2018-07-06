package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.cpu11398_local.cleanarchitecturedemo.R;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.di.AppComponent;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.di.AppModule;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.di.DaggerAppComponent;

public class ContentActivity extends AppCompatActivity {

    private static AppComponent appComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
    }

    public static AppComponent getAppComponent(Context context) {
        if (appComponent == null) {
            appComponent = DaggerAppComponent
                    .builder()
                    .appModule(new AppModule(context))
                    .build();
        }
        return appComponent;
    }
}
