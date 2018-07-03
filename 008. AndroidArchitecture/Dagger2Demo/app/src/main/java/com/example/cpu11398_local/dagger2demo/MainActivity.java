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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    private static AppComponent appComponent;

    @BindView(R.id.btn_option_login)
    Button btn_option_login;
    @BindView(R.id.btn_option_change)
    Button btn_option_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Utils.showLog(TAG, "onCreate");

        btn_option_login.setOnClickListener(this);
        btn_option_change.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        Utils.showLog(TAG, "onClick");
        switch (v.getId()) {
            case R.id.btn_option_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.btn_option_change:
                startActivity(new Intent(MainActivity.this, ChangeActivity.class));
                break;
        }
    }
}
