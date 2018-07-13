package com.example.cpu11398_local.swipebackdemo;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MyLayout extends FrameLayout{

    private final String    TAG                         = MyLayout.class.getSimpleName();
    private final float     DISTANCE_RATIO_HORIZONTAL   = 0.4f;
    private final float     DISTANCE_RATIO_VERTICAL     = 0.333f;
    private final long      MAX_ANIMATION_DURATION      = 500;
    private final long      MAX_FLING_DURATION          = 250;

    private float           screenWidth, screenHeight;
    private float           tomMaxTranslate;
    private float           downX, downY;
    private float           tomPositionY, screenPositionY;

    private float           touchSlop;
    private float           minimumFlingVelocity, maximumFlingVelocity;

    private boolean         isMoving, isHorizontalMoving, isRunningAnimation;

    private Activity        activity;
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
        activity                        = (Activity)context;
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
        tomMaxTranslate = (screenHeight - img_tom.getMeasuredHeight()) / 2;
        tomPositionY    = 0;
        screenPositionY = 0;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isRunningAnimation) {
            Log.i(TAG, "Ignore event " + MotionEvent.actionToString(event.getAction()));
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
    }//

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
    }//

    private void considerActionDown(MotionEvent event) {
        downX = event.getRawX();
        downY = event.getRawY();
    }//

    private void considerActionMove(MotionEvent event) {
        float translateX = event.getRawX() - downX;
        float translateY = event.getRawY() - downY;
        if (!isMoving) {
            if (translateX > touchSlop || Math.abs(translateY) > touchSlop) {
                isMoving = true;
                isHorizontalMoving = translateX > Math.abs(translateY);
                velocityTracker = VelocityTracker.obtain();
            }
        }
        if (isMoving) {
            if (isHorizontalMoving) {
                translateScreenManualHorizontal(translateX);
            } else {
                float tomNewPosition = tomPositionY + translateY;
                if (tomNewPosition >= -tomMaxTranslate && tomNewPosition <= tomMaxTranslate) {
                    translateTomManualVertical(tomNewPosition);
                } else if (tomNewPosition < -tomMaxTranslate) {
                    translateTomManualVertical(-tomMaxTranslate);
                    translateScreenManualVertical(tomNewPosition + tomMaxTranslate);
                } else {
                    translateTomManualVertical(tomMaxTranslate);
                    translateScreenManualVertical(tomNewPosition - tomMaxTranslate);
                }
            }
            velocityTracker.addMovement(event);
        }
    }//

    private void considerActionUp(MotionEvent event) {
        if (isMoving) {
            velocityTracker.addMovement(event);
            velocityTracker.computeCurrentVelocity(1000, maximumFlingVelocity);
            if (isHorizontalMoving) {
                if (Math.abs(velocityTracker.getXVelocity()) > minimumFlingVelocity) {
                    flingScreenHorizontal(velocityTracker.getXVelocity(), event.getRawX() - downX);
                } else {
                    translateScreenAutomaticHorizontal(event.getRawX() - downX);
                }
            } else {
                if (Math.abs(velocityTracker.getYVelocity()) > minimumFlingVelocity) {
                    flingTomVertical(velocityTracker.getYVelocity(), event.getRawY() - downY);
                } else {
                    tomPositionY += event.getRawY() - downY;
                }
            }
            isMoving = false;
            velocityTracker.clear();
        }
    }

    private void translateScreenManualHorizontal(float translate) {
        if (translate <= 0) {
            setTomWaitTouch();
            downX += translate;
            lyt_frame.setTranslationX(0);
        } else {
            setTomFraid();
            lyt_frame.setTranslationX(translate);
            setScreenOpacityWithTranslateHorizontal(translate);
        }
    }//

    private void translateScreenManualVertical(float translate) {
        if (translate <= 0) {
            downY += translate;
            lyt_frame.setTranslationY(0);
        } else {
            lyt_frame.setTranslationY(translate);
            setScreenOpacityWithTranslateVertical(translate);
        }
    }//

    private void translateTomManualVertical(float translate) {
        if (translate < -tomMaxTranslate || translate > tomMaxTranslate) {
            Log.e(TAG, "Tom cannot go out of the screen.");
            return;
        }
        img_tom.setTranslationY(translate);
    }//

    private void translateScreenAutomaticHorizontal(float startPosition) {
        if (startPosition < 0 || startPosition > screenWidth) {
            Log.e(TAG, "Cannot automatic translate screen with start position out-side screen.");
            return;
        }
        float endOpacity;
        float endPosition;
        long  duration;
        if (startPosition < DISTANCE_RATIO_HORIZONTAL * screenWidth) {
            endOpacity  = 1.0f;
            endPosition = 0.0f;
            duration    = (long)(startPosition / screenWidth * MAX_ANIMATION_DURATION);
        } else {
            endOpacity  = 0.0f;
            endPosition = screenWidth;
            duration    = (long)((screenWidth - startPosition) / screenWidth * MAX_ANIMATION_DURATION);
        }
        img_background.animate().alpha(endOpacity).setDuration(duration);
        lyt_frame
                .animate()
                .translationX(endPosition)
                .setDuration(duration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunningAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunningAnimation = false;
                        if (endPosition == 0.0f) {
                            setTomWaitTouch();
                        } else {
                            finishActivity();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

    }//

    private void translateScreenAutomaticVertical(float startPosition) {
        if (startPosition < 0 || startPosition > screenHeight) {
            Log.e(TAG, "Cannot automatic translate screen with start position out-side screen.");
            return;
        }
        float endOpacity;
        float endPosition;
        long  duration;
        if (startPosition < DISTANCE_RATIO_VERTICAL * screenHeight) {
            endOpacity  = 1.0f;
            endPosition = 0.0f;
            duration    = (long)(startPosition / screenHeight * MAX_ANIMATION_DURATION);
        } else {
            endOpacity  = 0.0f;
            endPosition = screenHeight;
            duration    = (long)((screenHeight - startPosition) / screenHeight * MAX_ANIMATION_DURATION);
        }
        img_background.animate().alpha(endOpacity).setDuration(duration);
        lyt_frame
                .animate()
                .translationX(endPosition)
                .setDuration(duration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunningAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunningAnimation = false;
                        if (endPosition == screenHeight) {
                            finishActivity();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }//

    private void flingScreenHorizontal(float velocity, float startPosition) {
        if (startPosition < 0 || startPosition > screenWidth) {
            Log.e(TAG, "Cannot fling from out-side screen.");
            return;
        }
        if (Math.abs(velocity) < minimumFlingVelocity || Math.abs(velocity) > maximumFlingVelocity) {
            Log.e(TAG, "Illegal fling velocity.");
            return;
        }
        float endOpacity;
        float endPosition;
        long  duration;
        if (velocity < 0) {
            endOpacity  = 1.0f;
            endPosition = 0.0f;
            duration    = (long)(startPosition / screenWidth * MAX_FLING_DURATION);
        } else {
            endOpacity  = 0.0f;
            endPosition = screenWidth;
            duration    = (long)((screenWidth - startPosition) / screenWidth * MAX_FLING_DURATION);
        }
        img_background.animate().alpha(endOpacity).setDuration(duration);
        lyt_frame
                .animate()
                .translationX(endPosition)
                .setDuration(duration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunningAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunningAnimation = false;
                        if (endPosition == 0.0f) {
                            setTomWaitTouch();
                        } else {
                            finishActivity();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }//

    private void flingScreenVertical(float velocity, float startPosition) {
        if (startPosition < 0 || startPosition > screenHeight) {
            Log.e(TAG, "Cannot fling from out-side screen.");
            return;
        }
        if (Math.abs(velocity) < minimumFlingVelocity || Math.abs(velocity) > maximumFlingVelocity) {
            Log.e(TAG, "Illegal fling velocity.");
            return;
        }
        float endOpacity;
        float endPosition;
        long  duration;
        if (velocity < 0) {
            endOpacity  = 1.0f;
            endPosition = 0.0f;
            duration    = (long)(startPosition / screenHeight * MAX_FLING_DURATION);
        } else {
            endOpacity  = 0.0f;
            endPosition = screenHeight;
            duration    = (long)((screenHeight - startPosition) / screenHeight * MAX_FLING_DURATION);
        }
        img_background.animate().alpha(endOpacity).setDuration(duration);
        lyt_frame
                .animate()
                .translationX(endPosition)
                .setDuration(duration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunningAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunningAnimation = false;
                        if (endPosition == screenHeight) {
                            finishActivity();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }//

    private void flingTomVertical(float velocity, float startPosition) {
        if (startPosition < -tomMaxTranslate || startPosition > tomMaxTranslate) {
            Log.e(TAG, "Tom cannot fling from out-side screen.");
            return;
        }
        if (Math.abs(velocity) < minimumFlingVelocity || Math.abs(velocity) > maximumFlingVelocity) {
            Log.e(TAG, "Illegal fling velocity.");
            return;
        }
        float tomNewPositon;
        long  duration;
        if (velocity > 0) {
            tomNewPositon = tomMaxTranslate;
            duration      = (long)((tomMaxTranslate - startPosition) / tomMaxTranslate / 2 * MAX_FLING_DURATION);
        } else {
            tomNewPositon = -tomMaxTranslate;
            duration      = (long)((tomMaxTranslate + startPosition) / tomMaxTranslate / 2 * MAX_FLING_DURATION);
        }
        img_tom
                .animate()
                .translationY(tomNewPositon)
                .setDuration(duration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunningAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setTomLaugh();
                        new Handler().postDelayed(
                                () -> {
                                    setTomWaitTouch();
                                    isRunningAnimation = false;
                                },
                                MAX_ANIMATION_DURATION
                        );
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }//

    private void setTomWaitTouch() {
        img_tom.setImageResource(R.drawable.img_tom_wait_touch);
    }//

    private void setTomLaugh() {
        img_tom.setImageResource(R.drawable.img_tom_laugh);
    }//

    private void setTomFraid() {
        img_tom.setImageResource(R.drawable.img_tom_afraid);
    }//

    private void setScreenOpacityWithTranslateHorizontal(float position) {
        if (position < 0) {
            Log.e(TAG, "Cannot set opacity with negative position.");
            return;
        }
        img_background.setAlpha((screenWidth - position) / screenWidth);
    }//

    private void setScreenOpacityWithTranslateVertical(float position) {
        if (position < 0) {
            Log.e(TAG, "Cannot set opacity with negative position.");
            return;
        }
        img_background.setAlpha((screenHeight - position) / screenHeight);
    }//

    private void finishActivity() {
        activity.finish();
        activity.overridePendingTransition(0, 0);
    }//
}