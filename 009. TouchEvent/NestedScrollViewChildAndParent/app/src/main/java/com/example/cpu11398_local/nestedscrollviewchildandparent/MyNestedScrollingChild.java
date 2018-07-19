package com.example.cpu11398_local.nestedscrollviewchildandparent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Arrays;

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
        Log.d(TAG, "startNestedScroll(" + axes2String(axes) + ", " + type2String(type) + ")");
        return super.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        Log.d(TAG, "stopNestedScroll(" + type2String(type) + ")");
        super.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        Log.d(TAG, "hasNestedScrollingParent(" + type2String(type) + ")");
        return super.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        Log.d(TAG, "dispatchNestedScroll(" + dxConsumed + ", " + dyConsumed + ", " + dxUnconsumed + ", " + dyUnconsumed + ", " + Arrays.toString(offsetInWindow) + ", " + type2String(type) + ")");
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        Log.d(TAG, "dispatchNestedPreScroll(" + dx + ", " + dy + ", " + Arrays.toString(consumed) + ", " + Arrays.toString(offsetInWindow) + ", " + type2String(type) + ")");
        return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        Log.d(TAG, "setNestedScrollingEnabled(" + enabled + ")");
        super.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        Log.d(TAG, "isNestedScrollingEnabled()");
        return super.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        Log.d(TAG, "startNestedScroll(" + axes2String(axes) + ")");
        return super.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        Log.d(TAG, "stopNestedScroll()");
        super.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        Log.d(TAG, "hasNestedScrollingParent()");
        return super.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        Log.d(TAG, "dispatchNestedScroll(" + dxConsumed + ", " + dyConsumed + ", " + dxUnconsumed + ", " + dyUnconsumed + ", " + Arrays.toString(offsetInWindow) + ")");
        return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        Log.d(TAG, "dispatchNestedPreScroll(" + dx + ", " + dy + ", " + Arrays.toString(consumed) + ", " + Arrays.toString(offsetInWindow) + ")");
        return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        Log.d(TAG, "dispatchNestedFling(" + velocityX + ", " + velocityY + ", " + consumed + ")");
        return super.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        Log.d(TAG, "dispatchNestedPreFling(" + velocityX + ", " + velocityY + ")");
        return super.dispatchNestedPreFling(velocityX, velocityY);
    }

    private String type2String(int type) {
        switch (type) {
            case 0:
                return "TYPE_TOUCH";
            case 1:
                return "TYPE_NON_TOUCH";
        }
        return null;
    }

    private String axes2String(int axes) {
        switch (axes) {
            case 0:
                return "SCROLL_AXIS_NONE";
            case 1:
                return "SCROLL_AXIS_HORIZONTAL";
            case 2:
                return "SCROLL_AXIS_VERTICAL";
        }
        return null;
    }
}
