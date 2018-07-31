package com.example.cpu11398_local.swipebackwithnestedscrolling;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;

public class MyNestedScrollingParent extends FrameLayout {

    private final float     DISTANCE_RATIO_HORIZONTAL   = 0.4f;
    private final float     DISTANCE_RATIO_VERTICAL     = 0.3333f;
    private final long      MAX_ANIMATION_DURATION      = 500;
    private final long      MAX_FLING_DURATION          = 250;

    private float           screenPositionX, screenPositionY, screenWidth, screenHeight;
    private float           lastMotionX, xDiff, velocityX;

    private boolean         isHorizontalMoving;

    private float           touchSlop;
    private float           minimumFlingVelocity, maximumFlingVelocity;

    private NestedScrollingParentHelper nestedScrollingParentHelper;
    private VelocityTracker             velocityTracker;
    private Context                     context;
    private ViewPropertyAnimator        animator;
    private NestedScrollView            nestedScrollingChild;

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
        ViewConfiguration config         = ViewConfiguration.get(context);
        this.touchSlop                   = config.getScaledTouchSlop();
        this.minimumFlingVelocity        = 10 * config.getScaledMinimumFlingVelocity();
        this.maximumFlingVelocity        = config.getScaledMaximumFlingVelocity();
        this.nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.context                     = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nestedScrollingChild = findViewById(R.id.nested_scrolling_child);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth     = MeasureSpec.getSize(widthMeasureSpec);
        screenHeight    = MeasureSpec.getSize(heightMeasureSpec);
        screenPositionX = 0.0f;
        screenPositionY = 0.0f;
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
        return isHorizontalMoving;
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
        lastMotionX = event.getRawX();
    }

    private void considerActionMove(MotionEvent event) {
        xDiff = event.getRawX() - lastMotionX;
        if (!isHorizontalMoving) {
            if (Math.abs(xDiff) > touchSlop) {
                isHorizontalMoving = true;
                velocityTracker = VelocityTracker.obtain();
            }
        }
        if (isHorizontalMoving) {
            lastMotionX = event.getRawX();
            screenPositionX += xDiff;
            translateScreenManualHorizontal(screenPositionX);
            velocityTracker.addMovement(event);
        }
    }

    private void considerActionUp(MotionEvent event) {
        if (isHorizontalMoving) {
            velocityTracker.computeCurrentVelocity(1000, maximumFlingVelocity);
            velocityX = velocityTracker.getXVelocity();
            if (Math.abs(velocityX) > minimumFlingVelocity && Math.abs(velocityX) < maximumFlingVelocity) {
                flingScreenHorizontal(screenPositionX, velocityX).withEndAction(() -> {
                    if (nestedScrollingChild.getTranslationX() != 0) {
                        finishActivity();
                    } else {
                        screenPositionX = 0.0f;
                    }
                });
            } else {
                translateScreenAutomaticHorizontal(screenPositionX).withEndAction(() -> {
                    if (nestedScrollingChild.getTranslationX() != 0) {
                        finishActivity();
                    } else {
                        screenPositionX = 0.0f;
                    }
                });
            }
            isHorizontalMoving = false;
            velocityTracker.clear();
        }
    }

    private void translateScreenManualHorizontal(float translate) {
        nestedScrollingChild.setTranslationX(translate);
        setScreenOpacityWithTranslateHorizontal(translate);
    }

    private void translateScreenManualVertical(float translate) {
        nestedScrollingChild.setTranslationY(translate);
        setScreenOpacityWithTranslateVertical(translate);
    }

    private ViewPropertyAnimator translateScreenAutomaticHorizontal(float startPosition) {
        float endPosition;
        long  duration;
        if (Math.abs(startPosition) < DISTANCE_RATIO_HORIZONTAL * screenWidth) {
            endPosition = 0.0f;
            duration    = (long)(Math.abs(startPosition) / screenWidth * MAX_ANIMATION_DURATION);
        } else if (startPosition > 0){
            endPosition = screenWidth;
            duration    = (long)((screenWidth - startPosition) / screenWidth * MAX_ANIMATION_DURATION);
        } else {
            endPosition = -screenWidth;
            duration    = (long)((screenWidth + startPosition) / screenWidth * MAX_ANIMATION_DURATION);
        }
        return animator = nestedScrollingChild
                .animate()
                .translationX(endPosition)
                .setDuration(duration)
                .setUpdateListener(value -> setScreenOpacityWithTranslateHorizontal(nestedScrollingChild.getTranslationX()))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animator = null;
                    }
                });
    }

    private ViewPropertyAnimator translateScreenAutomaticVertical(float startPosition) {
        float endPosition;
        long  duration;
        if (Math.abs(startPosition) < DISTANCE_RATIO_VERTICAL * screenHeight) {
            endPosition = 0.0f;
            duration    = (long)(Math.abs(startPosition) / screenHeight * MAX_ANIMATION_DURATION);
        } else if (startPosition > 0){
            endPosition = screenHeight;
            duration    = (long)((screenHeight - startPosition) / screenHeight * MAX_ANIMATION_DURATION);
        } else {
            endPosition = -screenHeight;
            duration    = (long)((screenHeight + startPosition) / screenHeight * MAX_ANIMATION_DURATION);
        }
        return animator = nestedScrollingChild
                .animate()
                .translationY(endPosition)
                .setDuration(duration)
                .setUpdateListener(value -> setScreenOpacityWithTranslateVertical(nestedScrollingChild.getTranslationY()))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animator = null;
                    }
                });
    }

    private ViewPropertyAnimator flingScreenHorizontal(float startPosition, float velocity) {
        float endPosition;
        long  duration;
        if ((velocity > 0 && startPosition < 0) || (velocity < 0 && startPosition > 0)){
            endPosition = 0.0f;
            duration    = (long)((Math.abs(startPosition)) / screenWidth * MAX_FLING_DURATION);
        } else if (velocity > 0) {
            endPosition = screenWidth;
            duration    = (long)((screenWidth - startPosition) / screenWidth * MAX_FLING_DURATION);
        } else {
            endPosition = -screenWidth;
            duration    = (long)((screenWidth + startPosition) / screenWidth * MAX_FLING_DURATION);
        }
        return animator = nestedScrollingChild
                .animate()
                .translationX(endPosition)
                .setDuration(duration)
                .setUpdateListener(value -> setScreenOpacityWithTranslateHorizontal(nestedScrollingChild.getTranslationX()))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animator = null;
                    }
                });
    }

    private ViewPropertyAnimator flingScreenVertical(float startPosition, float velocity) {
        float endPosition;
        long  duration;
        if ((velocity > 0 && startPosition < 0) || (velocity < 0 && startPosition > 0)){
            endPosition = 0.0f;
            duration    = (long)((Math.abs(startPosition)) / screenHeight * MAX_FLING_DURATION);
        } else if (velocity > 0) {
            endPosition = screenHeight;
            duration    = (long)((screenHeight - startPosition) / screenHeight * MAX_FLING_DURATION);
        } else {
            endPosition = -screenHeight;
            duration    = (long)((screenHeight + startPosition) / screenHeight * MAX_FLING_DURATION);
        }
        return animator = nestedScrollingChild
                .animate()
                .translationY(endPosition)
                .setDuration(duration)
                .setUpdateListener(value -> setScreenOpacityWithTranslateVertical(nestedScrollingChild.getTranslationY()))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animator = null;
                    }
                });
    }

    private void setScreenOpacityWithTranslateHorizontal(float position) {
        this.setAlpha((screenWidth - Math.abs(position)) / screenWidth);
    }

    private void setScreenOpacityWithTranslateVertical(float position) {
        this.setAlpha((screenHeight - Math.abs(position)) / screenHeight);
    }

    private void finishActivity() {
        Activity activity = (Activity)context;
        activity.finish();
        activity.overridePendingTransition(0, 0);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        if (screenPositionY != 0 && animator == null) {
            translateScreenAutomaticVertical(screenPositionY).withEndAction(() -> {
                        if (nestedScrollingChild.getTranslationY() != 0) {
                            finishActivity();
                        } else {
                            screenPositionY = 0.0f;
                        }
            });
        }
        nestedScrollingParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed != 0) {
            screenPositionY -= dyUnconsumed;
            translateScreenManualVertical(screenPositionY);
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        if ((screenPositionY > 0 && dy > 0) || (screenPositionY < 0 && dy < 0)) {
            if (Math.abs(screenPositionY) >= Math.abs(dy)) {
                consumed[1] = dy;
                screenPositionY -= dy;
                translateScreenManualVertical(screenPositionY);
            } else {
                consumed[1] = (int)screenPositionY;
                screenPositionY = 0.0f;
                translateScreenManualVertical(screenPositionY);
            }
        }
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            if (screenPositionY != 0) {
                if (Math.abs(velocityY) > minimumFlingVelocity && Math.abs(velocityY) < maximumFlingVelocity) {
                    flingScreenVertical(screenPositionY, -velocityY).withEndAction(() -> {
                        if (nestedScrollingChild.getTranslationY() != 0) {
                            finishActivity();
                        } else {
                            screenPositionY = 0.0f;
                        }
                    });
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        if (screenPositionY != 0) {
            if (Math.abs(velocityY) > minimumFlingVelocity && Math.abs(velocityY) < maximumFlingVelocity) {
                flingScreenVertical(screenPositionY, -velocityY).withEndAction(() -> {
                    if (nestedScrollingChild.getTranslationY() != 0) {
                        finishActivity();
                    } else {
                        screenPositionY = 0.0f;
                    }
                });
                return true;
            }
        }
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return nestedScrollingParentHelper.getNestedScrollAxes();
    }
}
