package com.example.cpu11398_local.swipebackwithnestedscrolling;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Arrays;

public class MyNestedScrollingParent extends FrameLayout {

    private final String TAG = MyNestedScrollingParent.class.getSimpleName();

    private NestedScrollingParentHelper nestedScrollingParentHelper;

    private NestedScrollView nestedScrollingChild;

    public MyNestedScrollingParent(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MyNestedScrollingParent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyNestedScrollingParent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nestedScrollingChild = findViewById(R.id.nested_scrolling_child);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes) {
        Log.e(TAG, "onStartNestedScroll(" + child.getClass().getSimpleName() + ", " + target.getClass().getSimpleName() + ", " + axes2String(axes) + ")");
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        Log.e(TAG, "onNestedScrollAccepted(" + child.getClass().getSimpleName()+ ", " + target.getClass().getSimpleName() + ", " + axes2String(axes) + ")");
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        Log.e(TAG, "onStopNestedScroll(" + target.getClass().getSimpleName() + ")");
        nestedScrollingParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, "onNestedScroll(" + target.getClass().getSimpleName() + ", " + dxConsumed + ", " + dyConsumed + ", " + dxUnconsumed + ", " + dyUnconsumed + ")");
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        Log.e(TAG, "onNestedPreScroll(" + target.getClass().getSimpleName() + ", " + dx + ", " + dy + ", " + Arrays.toString(consumed) + ")");
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, "onNestedFling(" + target.getClass().getSimpleName() + ", " + velocityX + ", " + velocityY + ", " + consumed + ")");
        return false;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling(" + target.getClass().getSimpleName() + ", " + velocityX + ", " + velocityY + ")");
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        Log.e(TAG, "getNestedScrollAxes()");
        return nestedScrollingParentHelper.getNestedScrollAxes();
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
