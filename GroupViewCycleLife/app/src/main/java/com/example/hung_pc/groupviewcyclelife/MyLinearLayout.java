package com.example.hung_pc.groupviewcyclelife;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by Hung-pc on 6/4/2018.
 */

public class MyLinearLayout extends LinearLayout{

    private final String TAG = "GROUP_VIEW_CYCLE_LIFE";

    public MyLinearLayout(@NonNull Context context) {
        super(context);
        Log.w(TAG, "MyGroupView(@NonNull Context context)");
    }

    public MyLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.w(TAG, "MyGroupView(@NonNull Context context, @Nullable AttributeSet attrs)");
    }

    public MyLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.w(TAG, "MyGroupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)");
    }

    public MyLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.w(TAG, "MyGroupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)");
    }

    @Override
    protected void onAttachedToWindow() {
        Log.w(TAG, "onAttachedToWindow begin");
        super.onAttachedToWindow();
        Log.w(TAG, "onAttachedToWindow finish");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.w(TAG, "onMeasure begin");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.w(TAG, "onMeasure finish");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.w(TAG, "onLayout begin");
        super.onLayout(changed, left, top, right, bottom);
        Log.w(TAG, "onLayout finish");
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.w(TAG, "dispatchDraw begin");
        super.dispatchDraw(canvas);
        Log.w(TAG, "dispatchDraw finish");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.w(TAG, "onDraw begin");
        super.onDraw(canvas);
        Log.w(TAG, "onDraw finish");
    }

    @Override
    public void requestLayout() {
        Log.w(TAG, "requestLayout begin");
        super.requestLayout();
        Log.w(TAG, "requestLayout finish");
    }
}
