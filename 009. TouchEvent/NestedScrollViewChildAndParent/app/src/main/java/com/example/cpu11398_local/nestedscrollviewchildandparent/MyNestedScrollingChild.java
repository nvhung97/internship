package com.example.cpu11398_local.nestedscrollviewchildandparent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;

public class MyNestedScrollingChild extends NestedScrollView{

    private final String TAG = MyNestedScrollingChild.class.getSimpleName();

    public MyNestedScrollingChild(@NonNull Context context) {
        super(context);
    }

    public MyNestedScrollingChild(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollingChild(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        boolean ret = super.startNestedScroll(axes, type);
        Log.d(TAG, "startNestedScroll(" + axes + ", " + type + "): " + ret);
        return ret;
    }

    @Override
    public void stopNestedScroll(int type) {
        Log.d(TAG, "stopNestedScroll(" + type + ")");
        super.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        boolean ret = super.hasNestedScrollingParent(type);
        Log.d(TAG, "hasNestedScrollingParent(" + type + "): " + ret);
        return ret;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        boolean ret = super. dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
        Log.d(TAG, "dispatchNestedScroll(" + dxConsumed + ", " + dyConsumed + ", " + dxUnconsumed + ", " + dyUnconsumed + ", " + offsetInWindow + ", " + type + "): " + ret);
        return ret;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        boolean ret = super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
        Log.d(TAG, "dispatchNestedPreScroll(" + dx + ", " + dy + ", " + consumed + ", " + offsetInWindow + ", " + type + "): " + ret);
        return ret;
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        Log.d(TAG, "setNestedScrollingEnabled(" + enabled + ")");
        super.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        boolean ret = super.isNestedScrollingEnabled();
        Log.d(TAG, "isNestedScrollingEnabled(): " + ret);
        return ret;
    }

    @Override
    public boolean startNestedScroll(int axes) {
        boolean ret = super.startNestedScroll(axes);
        Log.d(TAG, "startNestedScroll(" + axes + "): " + ret);
        return ret;
    }

    @Override
    public void stopNestedScroll() {
        Log.d(TAG, "stopNestedScroll()");
        super.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        boolean ret = super.hasNestedScrollingParent();
        Log.d(TAG, "hasNestedScrollingParent(): " + ret);
        return ret;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        boolean ret = super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        Log.d(TAG, "dispatchNestedScroll(" + dxConsumed + ", " + dyConsumed + ", " + dxUnconsumed + ", " + dyUnconsumed + ", " + offsetInWindow + "): " + ret);
        return ret;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        boolean ret = super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
        Log.d(TAG, "dispatchNestedPreScroll(" + dx + ", " + dy + ", " + consumed + ", " + offsetInWindow + "): " + ret);
        return ret;
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        boolean ret = super.dispatchNestedFling(velocityX, velocityY, consumed);
        Log.d(TAG, "dispatchNestedFling(" + velocityX + ", " + velocityY + ", " + consumed + "): " + ret);
        return ret;
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        boolean ret = super.dispatchNestedPreFling(velocityX, velocityY);
        Log.d(TAG, "dispatchNestedPreFling(" + velocityX + ", " + velocityY + "): " + ret);
        return ret;
    }
}
