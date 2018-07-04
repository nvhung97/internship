package com.example.cpu11398_local.dagger2demo;

import android.content.Intent;
import com.example.cpu11398_local.dagger2demo.DI.AppModule;
import com.example.cpu11398_local.dagger2demo.DI.DaggerAppComponent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.cpu11398_local.dagger2demo.DI.AppComponent;
import com.example.cpu11398_local.dagger2demo.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    private static AppComponent appComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Utils.showLog(TAG, "onCreate");
    }

    public static AppComponent getAppComponent() {
        Utils.showLog(TAG, "getAppComponent");
        if (appComponent == null) {
            appComponent = DaggerAppComponent
                    .builder()
                    .appModule(new AppModule())
                    .build();
        }
        return appComponent;
    }

    @OnClick(R.id.btn_option_login)
    public void login() {
        Utils.showLog(TAG, "login");
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    @OnClick(R.id.btn_option_change)
    public void change() {
        Utils.showLog(TAG, "change");
        startActivity(new Intent(MainActivity.this, ChangeActivity.class));
    }
}
