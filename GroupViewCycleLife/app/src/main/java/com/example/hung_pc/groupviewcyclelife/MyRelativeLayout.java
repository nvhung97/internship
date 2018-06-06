package com.example.hung_pc.groupviewcyclelife;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by Hung-pc on 6/4/2018.
 */

public class MyRelativeLayout extends RelativeLayout{

    private final String TAG = "GROUP_VIEW_CYCLE_LIFE";

    public MyRelativeLayout(@NonNull Context context) {
        super(context);
        Log.w(TAG, "MyGroupView(@NonNull Context context)");
    }

    public MyRelativeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.w(TAG, "MyGroupView(@NonNull Context context, @Nullable AttributeSet attrs)");
    }

    public MyRelativeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.w(TAG, "MyGroupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)");
    }

    public MyRelativeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.w(TAG, "MyGroupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.w(TAG, "onMeasure begin with widthMeasureSpec: " + widthMeasureSpec + ", heightMeasureSpec: " + heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.w(TAG, "onMeasure finish");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.w(TAG, "onLayout begin with changed: " + changed + ", left: " + left + ", top: " + top + ", right: " + right + ", bottom: " + bottom);
        super.onLayout(changed, left, top, right, bottom);
        Log.w(TAG, "onLayout finish");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.w(TAG, "onDraw begin");
        super.onDraw(canvas);
        Log.w(TAG, "onDraw finish");
    }
}
