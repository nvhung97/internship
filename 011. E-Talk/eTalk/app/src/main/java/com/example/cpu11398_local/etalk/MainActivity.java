package com.example.cpu11398_local.etalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_LOGIN     = 0;
    private final int REQUEST_REGISTER  = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Start Login Activity when user click Login button
     * @param v View is clicked
     */
    public void showLoginView(View v) {
        startActivityForResult(
                new Intent(MainActivity.this, LoginActivity.class),
                REQUEST_LOGIN
        );
    }

    /**
     * Start Register Activity when user click Register button
     * @param v View is clicked
     */
    public void showRegisterView(View v) {
        startActivityForResult(
                new Intent(MainActivity.this, RegisterActivity.class),
                REQUEST_REGISTER
        );
    }
}
