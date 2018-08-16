package com.example.cpu11398_local.etalk.presentation.view.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.di.AppComponent;
import com.example.cpu11398_local.etalk.presentation.di.AppModule;
import com.example.cpu11398_local.etalk.presentation.di.DaggerAppComponent;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.content.ContentActivity;
import com.example.cpu11398_local.etalk.presentation.view.login.LoginActivity;
import com.example.cpu11398_local.etalk.presentation.view.register.RegisterActivity;

public class MainActivity extends BaseActivity {

    private final int REQUEST_LOGIN     = 0;
    private final int REQUEST_REGISTER  = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Start Login Activity when user click Login button.
     * @param v View is clicked.
     */
    @SuppressLint("CheckResult")
    public void showLoginView(View v) {
        //startActivity(new Intent(MainActivity.this, ContentActivity.class));
        startActivityForResult(
                new Intent(MainActivity.this, LoginActivity.class),
                REQUEST_LOGIN
        );
    }

    /**
     * Start Register Activity when user click Register button.
     * @param v View is clicked.
     */
    public void showRegisterView(View v) {
        //startActivity(new Intent(MainActivity.this, ChatActivity.class));
        startActivityForResult(
                new Intent(MainActivity.this, RegisterActivity.class),
                REQUEST_REGISTER
        );
    }

    /**
     * Get result after {@link #showLoginView(View)} for login or {@link #showRegisterView(View)}
     * for register. If login successfully, start {@code ContentActivity}. If register successfully,
     * start {@code LoginActivity}. In other cases, do nothing.
     * @param requestCode code of action.
     * @param resultCode result of action.
     * @param data data after perform action.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == RESULT_OK) {
                    startActivity(new Intent(this, ContentActivity.class));
                }
                break;
            case REQUEST_REGISTER:
                if (resultCode == RESULT_OK) {
                    startActivityForResult(
                            new Intent(this, LoginActivity.class),
                            REQUEST_LOGIN
                    );
                }
        }
    }

    /**
     * A Singleton {@code AppComponent} is used to inject object to other activity.
     */
    private static AppComponent appComponent;

    /**
     * create a new {@code AppComponent} if it does not exist.
     * @param context parameter is used create a new {@code AppComponent}.
     * @return {@code AppComponent}.
     */
    public static AppComponent getAppComponent(Context context) {
        if (appComponent == null) {
            appComponent = DaggerAppComponent
                    .builder()
                    .appModule(new AppModule(context))
                    .build();
        }
        return appComponent;
    }

    @Override
    public void onDataBinding() {

    }

    @Override
    public void onSubscribeViewModel() {

    }

    @Override
    public void onUnSubscribeViewModel() {

    }

    @Override
    public Object onSaveViewModel() {
        return null;
    }

    @Override
    public void onEndTaskViewModel() {

    }
}
