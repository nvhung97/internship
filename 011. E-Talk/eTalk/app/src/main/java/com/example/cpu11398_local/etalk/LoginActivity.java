package com.example.cpu11398_local.etalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setStatusBarHeight();
    }

    public void setStatusBarHeight() {
        int resourceId = getResources().getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
        );
        if (resourceId > 0) {
            findViewById(R.id.login_activity_status_bar)
                    .getLayoutParams()
                    .height = getResources().getDimensionPixelSize(resourceId);
        }
    }

    /**
     * Finish activity with login successfully
     */
    public void finishSuccessfully() {
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Finish activity with login failed
     */
    public void finishFailed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
