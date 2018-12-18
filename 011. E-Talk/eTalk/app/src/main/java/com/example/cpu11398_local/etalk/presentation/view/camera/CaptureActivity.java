package com.example.cpu11398_local.etalk.presentation.view.camera;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.custom.AutoFitTextureView;

public class CaptureActivity extends AppCompatActivity {

    private AutoFitTextureView textureView;
    private Button btnCapture;
    private Button btnTick;
    private Button btnSwith;
    private Button btnCancel;
    private Button btnResolution1;
    private Button btnResolution2;
    private Button btnResolution3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textureView     = findViewById(R.id.capture_activity_texture);
        btnCapture      = findViewById(R.id.capture_activity_capture);
        btnTick         = findViewById(R.id.capture_activity_tick);
        btnSwith        = findViewById(R.id.capture_activity_switch);
        btnCancel       = findViewById(R.id.capture_activity_cancel);
        btnResolution1  = findViewById(R.id.capture_activity_resolution_1);
        btnResolution2  = findViewById(R.id.capture_activity_resolution_2);
        btnResolution3  = findViewById(R.id.capture_activity_resolution_3);

    }


    public void onChooseResolution1(View view) {
        //TODO: Switch to resolution 1
        btnResolution1.setTextColor(ContextCompat.getColor(this, R.color.colorETalkClickLight));
        btnResolution2.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        btnResolution3.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
    }

    public void onChooseResolution2(View view) {
        //TODO: Switch to resolution 2
        btnResolution1.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        btnResolution2.setTextColor(ContextCompat.getColor(this, R.color.colorETalkClickLight));
        btnResolution3.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
    }

    public void onChooseResolution3(View view) {
        //TODO: Switch to resolution 3
        btnResolution1.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        btnResolution2.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        btnResolution3.setTextColor(ContextCompat.getColor(this, R.color.colorETalkClickLight));
    }

    public void onCaptureCancel(View view) {
        //TODO: Check state of camera, finish if no image captured, otherwise reset state
        finish();
    }

    public void onCaptureExecute(View view) {
        //TODO: Capture image
        btnCapture.setVisibility(View.INVISIBLE);
        btnSwith.setVisibility(View.GONE);
        btnTick.setVisibility(View.VISIBLE);
    }

    public void onCaptureSwitch(View view) {
        //TODO: Switch between front and rear camera
    }

    public void onCaptureTick(View view) {
        //TODO: Do something with captured image
        finish();
    }
}
