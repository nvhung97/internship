package com.example.cpu11398_local.swipebackdemo;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MyLayout extends FrameLayout{

    private final long      MAX_ANIMATION_DURATION      = 500;
    private final long      MAX_FLING_SCREEN_DURATION   = 250;
    private final long      MAX_FLING_TOM_DURATION      = 500;

    private float           screenWidth, screenHeight;
    private float           tomMaxTranslate;
    private float           downX, downY;

    private float           touchSlop;
    private float           minimumFlingVelocity, maximumFlingVelocity;

    private boolean         isMoving, isHorizontalMoving, isRunningAnimation;

    private VelocityTracker velocityTracker;

    private FrameLayout     lyt_frame;
    private ImageView       img_background;
    private ImageView       img_tom;

    public MyLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        ViewConfiguration configuration = ViewConfiguration.get(context);
        touchSlop                       = configuration.getScaledTouchSlop();
        minimumFlingVelocity            = configuration.getScaledMinimumFlingVelocity();
        maximumFlingVelocity            = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        lyt_frame       = findViewById(R.id.lyt_frame);
        img_background  = findViewById(R.id.img_background);
        img_tom         = findViewById(R.id.img_tom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth     = MeasureSpec.getSize(widthMeasureSpec);
        screenHeight    = MeasureSpec.getSize(heightMeasureSpec);
        tomMaxTranslate = (screenHeight - img_tom.getMeasuredWidth()) / 2;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isRunningAnimation) {
            return true;
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                considerActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                considerActionMove(event);
                break;
        }
        return isMoving;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isRunningAnimation) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:
                    considerActionMove(event);
                    break;
                case MotionEvent.ACTION_UP:
                    considerActionUp(event);
                    break;
            }
        }
        return true;
    }

    private void considerActionDown(MotionEvent event) {
        downX = event.getRawX();
        downY = event.getRawY();
    }

    private void considerActionMove(MotionEvent event) {
        float deltaX = event.getRawX() - downX;
        float deltaY = event.getRawY() - downY;
        if (!isMoving) {
            if (deltaX > touchSlop || Math.abs(deltaY) > touchSlop) {
                isMoving = true;
                isHorizontalMoving = deltaX > Math.abs(deltaY);
                velocityTracker = VelocityTracker.obtain();
            }
        }
        if (isMoving) {
            if (isHorizontalMoving) {
                translateScreenManual(deltaX);
            } else {
                translateTomManual(deltaY);
            }
            velocityTracker.addMovement(event);
        }
    }

    private void considerActionUp(MotionEvent event) {
        if (isMoving) {
            velocityTracker.addMovement(event);
            velocityTracker.computeCurrentVelocity(1000, maximumFlingVelocity);
            if (isHorizontalMoving) {
                if (Math.abs(velocityTracker.getXVelocity()) > minimumFlingVelocity) {
                    flingScreen(velocityTracker.getXVelocity(), event.getRawX() - downX);
                } else {
                    translateScreenAutomatic(event.getRawX() - downX);
                }
            } else {
                if (Math.abs(velocityTracker.getYVelocity()) > minimumFlingVelocity) {
                    flingTom(velocityTracker.getYVelocity(), event.getRawY() - downY);
                } else {
                    translateTomAutomatic(event.getRawY() - downY);
                }
            }
            isMoving = false;
            velocityTracker.clear();
        }
    }

    private void translateScreenManual(float translate) {
        setScreenAlphaWithTranslate(translate);
        if (translate < 0) {
            setTomWaitTouch();
            downX += translate;
            lyt_frame.setTranslationX(0);
        } else {
            setTomFraid();
            lyt_frame.setTranslationX(translate);
        }
    }

    private void translateTomManual(float translate) {
        if (Math.abs(translate) <= tomMaxTranslate) {
            img_tom.setTranslationY(translate);
        } else {
            if (translate > 0) {
                img_tom.setTranslationY(tomMaxTranslate);
                downY += translate - tomMaxTranslate;
            } else {
                img_tom.setTranslationY(-tomMaxTranslate);
                downY += translate + tomMaxTranslate;
            }
        }
    }

    private void translateScreenAutomatic(float translate) {
        if (translate < screenWidth / 2) {
            long duration = (long)(translate / screenWidth * MAX_ANIMATION_DURATION);
            img_background.animate().alpha(1.0f).setDuration(duration);
            lyt_frame
                    .animate()
                    .translationX(0)
                    .setDuration(duration)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isRunningAnimation = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            setTomWaitTouch();
                            isRunningAnimation = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }
        else {
            long duration = (long)((screenWidth - translate) / screenWidth * MAX_ANIMATION_DURATION);
            img_background.animate().alpha(0.0f).setDuration(duration);
            lyt_frame
                    .animate()
                    .translationX(screenWidth)
                    .setDuration(duration)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            ((Activity)getContext()).finish();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }

    }

    private void translateTomAutomatic(float translate) {
        setTomWaitTouch();
        img_tom
                .animate()
                .translationY(0)
                .setDuration((long)(Math.abs(translate) / tomMaxTranslate / 2 * MAX_ANIMATION_DURATION))
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunningAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunningAnimation = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void flingScreen(float velocity, float translate) {
        if (velocity > 0) {
            long duration = (long)((screenWidth - translate) / screenWidth * MAX_FLING_SCREEN_DURATION);
            img_background.animate().alpha(0.0f).setDuration(duration);
            lyt_frame
                    .animate()
                    .translationX(screenWidth)
                    .setDuration(duration)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            ((Activity)getContext()).finish();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        } else {
            long duration = (long)(translate / screenWidth * MAX_FLING_SCREEN_DURATION);
            img_background.animate().alpha(1.0f).setDuration(duration);
            lyt_frame
                    .animate()
                    .translationX(0)
                    .setDuration(duration)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isRunningAnimation = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            setTomWaitTouch();
                            isRunningAnimation = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }
    }

    private void flingTom(float velocity, float translate) {
        setTomLaugh();
        if (velocity > 0) {
            img_tom
                    .animate()
                    .translationY(screenHeight / 2)
                    .setDuration((long)((screenHeight / 2 - translate) / screenHeight * MAX_FLING_TOM_DURATION))
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isRunningAnimation = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            img_tom
                                    .animate()
                                    .translationY(0)
                                    .setDuration(MAX_FLING_TOM_DURATION / 2)
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            isRunningAnimation = false;
                                            setTomWaitTouch();
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }
        else {
            img_tom
                    .animate()
                    .translationY(-screenHeight / 2)
                    .setDuration((long)((screenHeight / 2 + translate) / screenHeight * MAX_FLING_TOM_DURATION))
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isRunningAnimation = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            img_tom
                                    .animate()
                                    .translationY(0)
                                    .setDuration(MAX_FLING_TOM_DURATION / 2)
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            isRunningAnimation = false;
                                            setTomWaitTouch();
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }
    }

    private void setTomWaitTouch() {
        img_tom.setImageResource(R.drawable.img_tom_wait_touch);
    }

    private void setTomLaugh() {
        img_tom.setImageResource(R.drawable.img_tom_laugh);
    }

    private void setTomFraid() {
        img_tom.setImageResource(R.drawable.img_tom_afraid);
    }

    private void setScreenAlphaWithTranslate(float translate) {
        img_background.setAlpha((screenWidth - translate) / screenWidth);
    }
}
