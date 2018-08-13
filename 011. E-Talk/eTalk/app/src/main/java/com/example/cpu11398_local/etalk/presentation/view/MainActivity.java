package com.example.cpu11398_local.etalk.presentation.view;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupMenu;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.utils.Tool;

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
        /*PopupMenu popupMenu = new PopupMenu(this, v, Gravity.RIGHT);
        popupMenu.inflate(R.menu.aaaaa);
        Tool.forcePopupMenuShowIcon(popupMenu);
        popupMenu.show();*/
        startActivityForResult(
                new Intent(MainActivity.this, ContentActivity.class),
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
