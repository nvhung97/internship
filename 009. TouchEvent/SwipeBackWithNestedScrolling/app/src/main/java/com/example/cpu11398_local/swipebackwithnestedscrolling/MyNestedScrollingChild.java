package com.example.cpu11398_local.swipebackwithnestedscrolling;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;

/**
 * Created by Hung-pc on 7/31/2018.
 */

public class MyNestedScrollingChild extends FrameLayout{

    private final long      MAX_FLING_DURATION = 500;

    private float           listItemPositionY, listItemHeight, screenWidth, screenHeight;
    private float           lastMotionY, yDiff, velocityY;

    private boolean         isVerticalMoving;

    private float           touchSlop;
    private float           minimumFlingVelocity, maximumFlingVelocity;

    private View                        listItem;
    private ViewPropertyAnimator        animator;
    private NestedScrollingChildHelper  nestedScrollingChildHelper;
    private VelocityTracker             velocityTracker;

    public MyNestedScrollingChild(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MyNestedScrollingChild(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyNestedScrollingChild(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        ViewConfiguration config        = ViewConfiguration.get(context);
        this.touchSlop                  = config.getScaledTouchSlop();
        this.minimumFlingVelocity       = 10 * config.getScaledMinimumFlingVelocity();
        this.maximumFlingVelocity       = config.getScaledMaximumFlingVelocity();
        this.nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        listItem = findViewById(R.id.list_item);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        screenWidth   = MeasureSpec.getSize(widthMeasureSpec);
        screenHeight  = MeasureSpec.getSize(heightMeasureSpec);
        listItem.measure(
                (int)screenWidth | MeasureSpec.EXACTLY,
                10000 | MeasureSpec.AT_MOST
        );
        setMeasuredDimension((int)screenWidth, (int)screenHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        listItemPositionY = 0.0f;
        listItemHeight = listItem.getMeasuredHeight();
        listItem.layout(left, top, right, (int)listItemHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                considerActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                considerActionMove(event);
                break;
        }
        return isVerticalMoving;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                considerActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                considerActionUp(event);
                break;
        }
        return true;
    }

    private void considerActionDown(MotionEvent event) {
        if (animator != null) {
            animator.cancel();
            listItemPositionY = listItem.getTranslationY();
        }
        lastMotionY = event.getRawY();
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    private void considerActionMove(MotionEvent event) {
        yDiff = event.getRawY() - lastMotionY;
        if (!isVerticalMoving) {
            if (Math.abs(yDiff) > touchSlop) {
                isVerticalMoving = true;
                velocityTracker = VelocityTracker.obtain();
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                ViewParent viewParent = getParent();
                if (viewParent != null) {
                    viewParent.requestDisallowInterceptTouchEvent(true);
                }
            }
        }
        if (isVerticalMoving) {
            int[] consumed = {0, 0};
            dispatchNestedPreScroll(0, (int)yDiff, consumed, null);
            yDiff -= consumed[1];
            if (yDiff != 0) {
                int dyConsumed, dyUnconsumed;
                if ((yDiff > 0 && listItemPositionY + yDiff <= 0)
                        || (yDiff < 0 && listItemPositionY + yDiff >= -listItemHeight + screenHeight)) {
                    dyConsumed = (int)yDiff;
                    dyUnconsumed = 0;
                    listItemPositionY += yDiff;
                    translateListItemManualVertical(listItemPositionY);
                    dispatchNestedScroll(0, dyConsumed, 0, dyUnconsumed, null);
                } else if (yDiff > 0) {
                    dyConsumed = -(int)listItemPositionY;
                    dyUnconsumed = (int)yDiff - dyConsumed;
                    listItemPositionY = 0.0f;
                    translateListItemManualVertical(listItemPositionY);
                    dispatchNestedScroll(0, dyConsumed, 0, dyUnconsumed, null);
                } else {
                    dyConsumed = -(int)(listItemHeight -screenHeight + listItemPositionY);
                    dyUnconsumed = (int)yDiff - dyConsumed;
                    listItemPositionY = -listItemHeight + screenHeight;
                    translateListItemManualVertical(listItemPositionY);
                    dispatchNestedScroll(0, dyConsumed, 0, dyUnconsumed, null);
                }
            }
            lastMotionY = event.getRawY();
            velocityTracker.addMovement(event);
        }
    }

    private void considerActionUp(MotionEvent event) {
        if (isVerticalMoving) {
            velocityTracker.computeCurrentVelocity(1000, maximumFlingVelocity);
            velocityY = velocityTracker.getYVelocity();
            Log.e("Test", "check"+velocityY);
            if (Math.abs(velocityY) > minimumFlingVelocity && Math.abs(velocityY) < maximumFlingVelocity) {
                if (!dispatchNestedPreFling(0.0f, velocityY)) {
                    if (listItemPositionY < 0 && listItemPositionY > -listItemHeight + screenHeight) {
                        flingLisItemVertical(listItemPositionY, velocityY).withEndAction(() -> {
                            listItemPositionY = listItem.getTranslationY();
                        });
                        dispatchNestedFling(0.0f, velocityY, true);
                    } else {
                        dispatchNestedFling(0.0f, velocityY, false);
                    }
                }
            }
            isVerticalMoving = false;
            velocityTracker.recycle();
        }
        stopNestedScroll();
    }

    private void translateListItemManualVertical(float translate) {
        listItem.setTranslationY(translate);
    }

    private ViewPropertyAnimator flingLisItemVertical(float startPosition, float velocity) {
        float endPosition;
        long  duration;
        if (velocity > 0) {
            endPosition = 0.0f;
            duration    = (long)(Math.abs(startPosition) / (listItemHeight - screenHeight) * MAX_FLING_DURATION);

        } else {
            endPosition = -listItemHeight + screenHeight;
            duration    = (long)((listItemHeight - screenHeight - Math.abs(startPosition)) / (listItemHeight - screenHeight) * MAX_FLING_DURATION);
        }
        return animator = listItem
                .animate()
                .translationY(endPosition)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animator = null;
                    }
                });
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        nestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return nestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return nestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        nestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return nestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
