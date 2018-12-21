package com.example.cpu11398_local.etalk.presentation.view.camera;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.custom.AutoFitTextureView;
import com.example.cpu11398_local.etalk.presentation.custom.ClockView;

public class RecordActivity extends AppCompatActivity {

    private AutoFitTextureView textureView;
    private ClockView clockView;
    private Button btnRecord;
    private Button btnStopRecord;
    private Button btnTick;
    private Button btnSwith;
    private Button btnCancel;
    private Button btnResolution1;
    private Button btnResolution2;
    private Button btnResolution3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textureView     = findViewById(R.id.record_activity_texture);
        clockView       = findViewById(R.id.record_activity_time);
        btnRecord       = findViewById(R.id.record_activity_record);
        btnStopRecord   = findViewById(R.id.record_activity_stop_record);
        btnTick         = findViewById(R.id.record_activity_tick);
        btnSwith        = findViewById(R.id.record_activity_switch);
        btnCancel       = findViewById(R.id.record_activity_cancel);
        btnResolution1  = findViewById(R.id.record_activity_resolution_1);
        btnResolution2  = findViewById(R.id.record_activity_resolution_2);
        btnResolution3  = findViewById(R.id.record_activity_resolution_3);

        setRotationAnimation();

        btnResolution1.setText("1x2");
        btnResolution2.setText("1x2");
        btnResolution3.setText("1x2");
    }

    private void setRotationAnimation() {
        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        winParams.rotationAnimation = WindowManager.LayoutParams.ROTATION_ANIMATION_CROSSFADE;;
        window.setAttributes(winParams);
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

    public void onRecordCancel(View view) {
        //TODO: Check state of camera, finish if no video recorded, otherwise reset state
        finish();
    }

    public void onRecordExecute(View view) {
        //TODO: Start record video
        btnRecord.setVisibility(View.GONE);
        btnStopRecord.setVisibility(View.VISIBLE);
        clockView.setVisibility(View.VISIBLE);
        btnSwith.setVisibility(View.GONE);
        clockView.setCountTime(70L);
    }

    public void onRecordStop(View view) {
        //TODO: Stop record video
        btnStopRecord.setVisibility(View.GONE);
        btnTick.setVisibility(View.VISIBLE);
    }

    public void onRecordSwitch(View view) {
        //TODO: Switch between front and rear camera
    }

    public void onRecordTick(View view) {
        //TODO: Do something with recorded video
        finish();
    }
}
