package com.example.cpu11398_local.etalk.presentation.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.cpu11398_local.etalk.R;

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

    /**
     * Get result after {@link #showLoginView(View)} for login or {@link #showRegisterView(View)}
     * for register. If login successfully, start {@code ContentActivity}. If register successfully,
     * start {@code LoginActivity}. In other cases, do nothing.
     * @param requestCode code of action
     * @param resultCode result of action
     * @param data data after perform action
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
}
