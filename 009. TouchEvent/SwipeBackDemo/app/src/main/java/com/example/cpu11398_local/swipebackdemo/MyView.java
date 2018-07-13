package com.example.cpu11398_local.swipebackdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class MyView extends ImageView {

    private final long TICKLED_DELAY = 1000;
    private final long TOUCH_TIMEOUT = 100;

    private Handler handler;

    private boolean isWaitInTickled, isWaitForTickled;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isWaitInTickled) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    handler = new Handler();
                    isWaitForTickled = true;
                    handler.postDelayed(
                            () -> {
                                if (System.currentTimeMillis() % 2 == 0) {
                                    setTomTickled();
                                }
                                else {
                                    setTomLaugh();
                                }
                                isWaitForTickled = false;
                            },
                            TOUCH_TIMEOUT
                    );
                    break;
                case MotionEvent.ACTION_UP:
                    handler = new Handler();
                    isWaitInTickled = true;
                    handler.postDelayed(
                            () -> {
                                setTomWaitTouch();
                                isWaitInTickled = false;
                            },
                            TICKLED_DELAY
                    );
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if (isWaitForTickled) {
                        handler.removeCallbacksAndMessages(null);
                    }
                    break;
            }
        }
        return true;
    }

    private void setTomWaitTouch() {
        this.setImageResource(R.drawable.img_tom_wait_touch);
    }

    private void setTomTickled() {
        this.setImageResource(R.drawable.img_tom_tickled);
    }

    private void setTomLaugh() {
        this.setImageResource(R.drawable.img_tom_laugh);
    }
}
