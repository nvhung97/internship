package com.example.cpu11398_local.etalk.presentation.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.utils.Tool;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Tool.setStatusBarHeight(
                this,
                findViewById(R.id.login_activity_status_bar)
        );
    }

    /**
     * Finish activity when user click back arrow
     * @param v
     */
    public void onBackPressed(View v) {
        onBackPressed();
    }
}
