package com.example.cpu11398_local.etalk.presentation.view.chat.media;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.ActivityMediaPhotoBinding;
import com.example.cpu11398_local.etalk.utils.GlideApp;

public class MediaPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityMediaPhotoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_media_photo);
        binding.mediaPhotoActivityBack.setOnClickListener(v -> onBackPressed());
        binding.mediaPhotoActivityName.setText(getIntent().getExtras().getString("name"));
        GlideApp
                .with(this)
                .load(getIntent().getExtras().getString("link"))
                .into(binding.mediaPhotoActivityContent);
    }
}
