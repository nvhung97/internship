package com.example.cpu11398_local.swipebackwithnestedscrolling;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Hung-pc on 7/31/2018.
 */

@SuppressLint("AppCompatCustomView")
public class MyView extends TextView{

    private final long TOUCH_TIMEOUT = 100;

    private boolean isWaitingForAction;
    private Handler handler;

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
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                handler = new Handler();
                isWaitingForAction = true;
                handler.postDelayed(
                        () -> {
                            setBackgroundColor(getResources().getColor(R.color.colorRed));
                            isWaitingForAction = false;
                        },
                        TOUCH_TIMEOUT
                );
                break;
            case MotionEvent.ACTION_CANCEL:
                if (isWaitingForAction) {
                    handler.removeCallbacksAndMessages(null);
                }
        }
        return true;
    }
}
