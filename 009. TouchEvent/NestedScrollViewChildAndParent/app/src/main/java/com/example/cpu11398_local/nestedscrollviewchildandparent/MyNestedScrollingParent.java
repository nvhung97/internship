package com.example.cpu11398_local.nestedscrollviewchildandparent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyNestedScrollingParent extends NestedScrollView {

    private final String TAG = MyNestedScrollingParent.class.getSimpleName();


    public MyNestedScrollingParent(@NonNull Context context) {
        super(context);
    }

    public MyNestedScrollingParent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollingParent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes) {
        boolean ret = super.onStartNestedScroll(child, target, axes);
        Log.d(TAG, "onStartNestedScroll(" + child + ", " + target + ", " + axes + "): " + ret);
        return ret;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        Log.d(TAG, "onNestedScrollAccepted(" + child + ", " + target + ", " + axes + ")");
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        Log.d(TAG, "onStopNestedScroll(" + target + ")");
        super.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d(TAG, "onNestedScroll(" + target + ", " + dxConsumed + ", " + dyConsumed + ", " + dxUnconsumed + ", " + dyUnconsumed + ")");
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        Log.d(TAG, "onNestedPreScroll(" + target + ", " + dx + ", " + dy + ", " + consumed + ")");
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        boolean ret = super.onNestedFling(target, velocityX, velocityY, consumed);
        Log.d(TAG, "onNestedFling(" + target + ", " + velocityX + ", " + velocityY + ", " + consumed + "): " + ret);
        return ret;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        boolean ret = super.onNestedPreFling(target, velocityX, velocityY);
        Log.d(TAG, "onNestedPreFling(" + target + ", " + velocityX + ", " + velocityY + "): " + ret);
        return ret;
    }

    @Override
    public int getNestedScrollAxes() {
        int ret = super.getNestedScrollAxes();
        Log.d(TAG, "getNestedScrollAxes(): " + ret);
        return ret;
    }
}
