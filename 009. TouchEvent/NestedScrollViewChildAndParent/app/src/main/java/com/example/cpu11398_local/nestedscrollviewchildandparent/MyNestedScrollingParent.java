package com.example.cpu11398_local.nestedscrollviewchildandparent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

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
        Log.d(TAG, "onStartNestedScroll(" + child.getClass().getSimpleName() + ", " + target.getClass().getSimpleName() + ", " + axes2String(axes) + ")");
        return super.onStartNestedScroll(child, target, axes);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        Log.d(TAG, "onNestedScrollAccepted(" + child.getClass().getSimpleName()+ ", " + target.getClass().getSimpleName() + ", " + axes2String(axes) + ")");
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        Log.d(TAG, "onStopNestedScroll(" + target.getClass().getSimpleName() + ")");
        super.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d(TAG, "onNestedScroll(" + target.getClass().getSimpleName() + ", " + dxConsumed + ", " + dyConsumed + ", " + dxUnconsumed + ", " + dyUnconsumed + ")");
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        Log.d(TAG, "onNestedPreScroll(" + target.getClass().getSimpleName() + ", " + dx + ", " + dy + ", " + Arrays.toString(consumed) + ")");
        super.onNestedPreScroll(target, dx, dy, consumed);
        Log.e(TAG, "onNestedPreScroll(" + target.getClass().getSimpleName() + ", " + dx + ", " + dy + ", " + Arrays.toString(consumed) + ")");
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        Log.d(TAG, "onNestedFling(" + target.getClass().getSimpleName() + ", " + velocityX + ", " + velocityY + ", " + consumed + ")");
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        Log.d(TAG, "onNestedPreFling(" + target.getClass().getSimpleName() + ", " + velocityX + ", " + velocityY + ")");
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        Log.d(TAG, "getNestedScrollAxes()");
        return super.getNestedScrollAxes();
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
