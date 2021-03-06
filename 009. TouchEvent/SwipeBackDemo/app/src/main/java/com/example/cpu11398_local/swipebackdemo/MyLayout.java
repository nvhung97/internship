package com.example.cpu11398_local.swipebackdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MyLayout extends FrameLayout{

    private final String    TAG                         = MyLayout.class.getSimpleName();
    private final float     DISTANCE_RATIO_HORIZONTAL   = 0.4f;
    private final float     DISTANCE_RATIO_VERTICAL     = 0.333f;
    private final long      MAX_ANIMATION_DURATION      = 500;
    private final long      MAX_FLING_DURATION          = 250;

    private float           screenWidth, screenHeight;
    private float           tomPositionY, tomTempPositionY, tomMaxTranslateY;
    private float           downX, downY;
    private float           translateX, translateY;
    private float           velocityX, velocityY;

    private float           touchSlop;
    private float           minimumFlingVelocity, maximumFlingVelocity;

    private boolean         isMoving, isHorizontalMoving, isRunningAnimation;

    private Context         context;
    private VelocityTracker velocityTracker;

    private FrameLayout     lyt_frame;
    private ImageView       img_tom;
    private View            background;

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

    private void init(Context ctx) {
        ViewConfiguration config = ViewConfiguration.get(ctx);
        touchSlop                = config.getScaledTouchSlop();
        minimumFlingVelocity     = 10 * config.getScaledMinimumFlingVelocity();
        maximumFlingVelocity     = config.getScaledMaximumFlingVelocity();
        context                  = ctx;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        lyt_frame       = findViewById(R.id.lyt_frame);
        img_tom         = findViewById(R.id.img_tom);
        background      = new View(context);
        background.setBackgroundColor(getResources().getColor(R.color.colorLightBlack));
        addView(
                background,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
        lyt_frame.bringToFront();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth      = MeasureSpec.getSize(widthMeasureSpec);
        screenHeight     = MeasureSpec.getSize(heightMeasureSpec);
        tomMaxTranslateY = (screenHeight - img_tom.getMeasuredHeight()) / 2;
        tomPositionY     = 0;
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
        translateX = event.getRawX() - downX;
        translateY = event.getRawY() - downY;
        if (!isMoving) {
            if (Math.abs(translateX) > touchSlop || Math.abs(translateY) > touchSlop) {
                isMoving = true;
                isHorizontalMoving = Math.abs(translateX) > Math.abs(translateY);
                velocityTracker = VelocityTracker.obtain();
            }
        }
        if (isMoving) {
            if (isHorizontalMoving) {
                if (translateX > 0) {
                    translateScreenManualHorizontal(translateX);
                    setTomFraid();
                } else {
                    translateScreenManualHorizontal(0.0f);
                    setTomWaitTouch();
                    downX += translateX;
                }
            } else {
                tomTempPositionY = tomPositionY + translateY;
                if (tomTempPositionY >= -tomMaxTranslateY && tomTempPositionY <= tomMaxTranslateY) {
                    translateTomManualVertical(tomTempPositionY);
                    translateScreenManualVertical(0.0f);
                } else if (tomTempPositionY > tomMaxTranslateY) {
                    translateTomManualVertical(tomMaxTranslateY);
                    translateScreenManualVertical(tomTempPositionY - tomMaxTranslateY);
                } else {
                    translateTomManualVertical(-tomMaxTranslateY);
                    downY += tomTempPositionY + tomMaxTranslateY;
                }
            }
            velocityTracker.addMovement(event);
        }
    }

    private void considerActionUp(MotionEvent event) {
        if (isMoving) {
            velocityTracker.computeCurrentVelocity(1000, maximumFlingVelocity);
            if (isHorizontalMoving) {
                translateX = event.getRawX() - downX;
                velocityX  = velocityTracker.getXVelocity();
                if (velocityX > minimumFlingVelocity) {
                    flingHideScreenHorizontal(translateX).withEndAction(this::finishActivity);
                } else {
                    translateScreenAutomaticHorizontal(translateX).withEndAction(() -> {
                        if (translateX < DISTANCE_RATIO_HORIZONTAL * screenWidth) {
                            setTomWaitTouch();
                        } else {
                            finishActivity();
                        }
                    });
                }
            } else {
                translateY       = event.getRawY() - downY;
                tomTempPositionY = tomPositionY + translateY;
                velocityY        = velocityTracker.getYVelocity();
                if (Math.abs(velocityY) > minimumFlingVelocity) {
                    if (tomTempPositionY <= tomMaxTranslateY) {
                        flingTomVertical(velocityY, tomTempPositionY);
                    } else if (velocityY > 0 && tomPositionY == tomMaxTranslateY) {
                        flingHideScreenVertical(
                                tomTempPositionY - tomMaxTranslateY
                        ).withEndAction(this::finishActivity);
                    } else {
                        tomPositionY = tomMaxTranslateY;
                        translateScreenAutomaticVertical(
                                tomTempPositionY - tomMaxTranslateY
                        ).withEndAction(() -> {
                            if (tomTempPositionY - tomMaxTranslateY < DISTANCE_RATIO_VERTICAL * screenHeight) {
                                setTomWaitTouch();
                            } else {
                                finishActivity();
                            }
                        });
                    }
                } else {
                    if (tomTempPositionY <= tomMaxTranslateY) {
                        tomPositionY = tomTempPositionY;
                        setTomWaitTouch();
                    } else {
                        tomPositionY = tomMaxTranslateY;
                        translateScreenAutomaticVertical(
                                tomTempPositionY - tomMaxTranslateY
                        ).withEndAction(() -> {
                            if (tomTempPositionY - tomMaxTranslateY < DISTANCE_RATIO_VERTICAL * screenHeight) {
                                setTomWaitTouch();
                            } else {
                                finishActivity();
                            }
                        });
                    }
                }
            }
            isMoving = false;
            velocityTracker.clear();
        }
    }

    private void translateScreenManualHorizontal(float translate) {
        if (translate < 0 || translate > screenWidth) {
            Log.d(TAG, "Cannot translate screen to left or over right edge.");
        }
        lyt_frame.setTranslationX(translate);
        setScreenOpacityWithTranslateHorizontal(translate);
    }

    private void translateScreenManualVertical(float translate) {
        if (translate < 0 || translate > screenHeight) {
            Log.d(TAG, "Cannot translate screen to top or over bottom edge.");
        }
        lyt_frame.setTranslationY(translate);
        setScreenOpacityWithTranslateVertical(translate);
    }

    private void translateTomManualVertical(float translate) {
        if (translate < -tomMaxTranslateY || translate > tomMaxTranslateY) {
            Log.d(TAG, "Tom cannot go out of the screen.");
        }
        img_tom.setTranslationY(translate);
    }

    private ViewPropertyAnimator translateScreenAutomaticHorizontal(float startPosition) {
        if (startPosition < 0 || startPosition > screenWidth) {
            Log.d(TAG, "Cannot automatic translate screen with start position out-side screen.");
        }
        float endPosition;
        long  duration;
        if (startPosition < DISTANCE_RATIO_HORIZONTAL * screenWidth) {
            endPosition = 0.0f;
            duration    = (long)(startPosition / screenWidth * MAX_ANIMATION_DURATION);
        } else {
            endPosition = screenWidth;
            duration    = (long)((screenWidth - startPosition) / screenWidth * MAX_ANIMATION_DURATION);
        }
        return lyt_frame
                .animate()
                .translationX(endPosition)
                .setDuration(duration)
                .setUpdateListener(value -> setScreenOpacityWithTranslateHorizontal(lyt_frame.getTranslationX()))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunningAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunningAnimation = false;
                    }
                });

    }

    private ViewPropertyAnimator translateScreenAutomaticVertical(float startPosition) {
        if (startPosition < 0 || startPosition > screenHeight) {
            Log.d(TAG, "Cannot automatic translate screen with start position out-side screen.");
        }
        float endPosition;
        long  duration;
        if (startPosition < DISTANCE_RATIO_VERTICAL * screenHeight) {
            endPosition = 0.0f;
            duration    = (long)(startPosition / screenHeight * MAX_ANIMATION_DURATION);
        } else {
            endPosition = screenHeight;
            duration    = (long)((screenHeight - startPosition) / screenHeight * MAX_ANIMATION_DURATION);
        }
        return lyt_frame
                .animate()
                .translationY(endPosition)
                .setDuration(duration)
                .setUpdateListener(value -> setScreenOpacityWithTranslateVertical(lyt_frame.getTranslationY()))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunningAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunningAnimation = false;
                    }
                });
    }

    private ViewPropertyAnimator flingHideScreenHorizontal(float startPosition) {
        if (startPosition < 0 || startPosition > screenWidth) {
            Log.d(TAG, "Cannot fling from out-side screen.");
        }
        return lyt_frame
                .animate()
                .translationX(screenWidth)
                .setDuration((long)((screenWidth - startPosition) / screenWidth * MAX_FLING_DURATION))
                .setUpdateListener(value -> setScreenOpacityWithTranslateHorizontal(lyt_frame.getTranslationX()))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunningAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunningAnimation = false;
                    }
                });
    }

    private ViewPropertyAnimator flingHideScreenVertical(float startPosition) {
        if (startPosition < 0 || startPosition > screenHeight) {
            Log.d(TAG, "Cannot fling from out-side screen.");
        }
        return lyt_frame
                .animate()
                .translationY(screenHeight)
                .setDuration((long)((screenHeight - startPosition) / screenHeight * MAX_FLING_DURATION))
                .setUpdateListener(value -> setScreenOpacityWithTranslateVertical(lyt_frame.getTranslationY()))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunningAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunningAnimation = false;
                    }
                });
    }

    private ViewPropertyAnimator flingTomVertical(float velocity, float startPosition) {
        if (Math.abs(velocity) < minimumFlingVelocity || Math.abs(velocity) > maximumFlingVelocity) {
            Log.d(TAG, "Illegal fling velocity.");
        }
        if (startPosition < -tomMaxTranslateY || startPosition > tomMaxTranslateY) {
            Log.d(TAG, "Tom cannot fling from out-side screen.");
        }
        long duration;
        if (velocity > 0) {
            tomPositionY = tomMaxTranslateY;
            duration     = (long)((tomMaxTranslateY - startPosition) / tomMaxTranslateY / 2 * MAX_FLING_DURATION);
        } else {
            tomPositionY = -tomMaxTranslateY;
            duration     = (long)((tomMaxTranslateY + startPosition) / tomMaxTranslateY / 2 * MAX_FLING_DURATION);
        }
        return img_tom
                .animate()
                .translationY(tomPositionY)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
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
                });
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

    private void setScreenOpacityWithTranslateHorizontal(float position) {
        if (position < 0 || position > screenWidth) {
            Log.d(TAG, "Cannot set opacity with position out-side screen.");
        }
        background.setAlpha((screenWidth - position) / screenWidth);
    }

    private void setScreenOpacityWithTranslateVertical(float position) {
        if (position < 0 || position > screenHeight) {
            Log.d(TAG, "Cannot set opacity with position out-side screen.");
        }
        background.setAlpha((screenHeight - position) / screenHeight);
    }

    private void finishActivity() {
        ((Activity)context).finish();
        ((Activity)context).overridePendingTransition(0, 0);
    }
}