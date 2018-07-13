package com.example.cpu11398_local.toucheventcyclelife;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MyLayout extends FrameLayout {

    final String TAG = "TouchEvent";

    public MyLayout(@NonNull Context context) {
        super(context);
    }

    public MyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "MyLayout dispatchTouchEvent DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MyLayout dispatchTouchEvent MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "MyLayout dispatchTouchEvent UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "MyLayout dispatchTouchEvent CANCEL");
                break;
        }
        boolean b = super.dispatchTouchEvent(ev);
        Log.d(TAG, "MyLayout dispatchTouchEvent RETURNS " + b);
        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "MyLayout onInterceptTouchEvent DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MyLayout onInterceptTouchEvent MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "MyLayout onInterceptTouchEvent UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "MyLayout onInterceptTouchEvent CANCEL");
                break;
        }
        boolean b = super.onInterceptTouchEvent(ev);
        Log.d(TAG, "MyLayout onInterceptTouchEvent RETURNS " + b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "MyLayout onTouchEvent DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MyLayout onTouchEvent MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "MyLayout onTouchEvent UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "MyLayout onTouchEvent CANCEL");
                break;
        }
        boolean b = super.onTouchEvent(event);
        Log.d(TAG, "MyLayout onTouchEvent RETURNS " + b);
        return b;
    }
}
