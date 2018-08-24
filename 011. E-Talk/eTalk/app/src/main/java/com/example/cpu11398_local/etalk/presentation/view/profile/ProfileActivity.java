package com.example.cpu11398_local.etalk.presentation.view.profile;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.cpu11398_local.etalk.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ((CollapsingToolbarLayout)findViewById(R.id.toolbar0)).setContentScrim(getDrawable(R.drawable.tool_bar_backgound));
    }
}
