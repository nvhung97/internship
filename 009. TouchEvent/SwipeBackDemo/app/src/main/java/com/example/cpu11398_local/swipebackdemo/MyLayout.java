package com.example.cpu11398_local.swipebackdemo;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MyLayout extends FrameLayout{

    private final float     MIN_MOVE                = 10.0f;
    private final long      MAX_ANIMATION_DURATION  = 1000;

    private float           screenWidth, screenHeight;
    private float           tomSize;
    private float           tomMaxTranslate;
    private float           downX, downY;
    private boolean         isMoving, isHorizontalMoving;

    private VelocityTracker velocityTracker;

    private ImageView       img_tom;
    private View            background;

    public MyLayout(@NonNull Context context) {
        super(context);
    }

    public MyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        img_tom = findViewById(R.id.img_tom);
        background = new View(getContext());
        background.setBackgroundColor(getResources().getColor(R.color.colorLightBlack));
        background.setVisibility(GONE);
        addView(
                background,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth     = MeasureSpec.getSize(widthMeasureSpec);
        screenHeight    = MeasureSpec.getSize(heightMeasureSpec);
        tomSize         = img_tom.getMeasuredWidth();
        tomMaxTranslate = (screenHeight - tomSize) / 2;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                return false;
            case MotionEvent.ACTION_MOVE:
                return ev.getRawX() - downX > MIN_MOVE
                        || Math.abs(ev.getRawY() - downY) > MIN_MOVE;
            default:
                return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getRawX() - downX;
                float deltaY = event.getRawY() - downY;
                if (!isMoving) {
                    isMoving = deltaX > MIN_MOVE || Math.abs(deltaY) > MIN_MOVE;
                    isHorizontalMoving = deltaX > Math.abs(deltaY);
                }
                if (isMoving) {
                    if (isHorizontalMoving) {
                        translateScreenManual(deltaX);
                    } else {
                        translateTomManual(deltaY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isMoving) {
                    if (isHorizontalMoving) {
                        translateScreenAutomatic(event.getRawX() - downX);
                    } else {
                        translateTomAutomatic(event.getRawY() - downY);
                    }
                    isMoving = false;
                }
                break;
        }
        return true;
    }

    private void translateScreenManual(float translate) {
        if (translate < 0) {
            img_tom.setImageResource(R.drawable.img_tom_wait_touch);
            downX += translate;
            this.setTranslationX(0);
        } else {
            img_tom.setImageResource(R.drawable.img_tom_afraid);
            this.setTranslationX(translate);
        }
    }

    private void translateTomManual(float translate) {
        if (Math.abs(translate) <= tomMaxTranslate) {
            img_tom.setImageResource(R.drawable.img_tom_wait_touch);
            img_tom.setTranslationY(translate);
        } else {
            img_tom.setImageResource(R.drawable.img_tom_laugh);
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
            this
                    .animate()
                    .translationX(0)
                    .setDuration((long)(translate / screenWidth * MAX_ANIMATION_DURATION))
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            img_tom.setImageResource(R.drawable.img_tom_wait_touch);
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
            this
                    .animate()
                    .translationX(screenWidth)
                    .setDuration((long)((screenWidth - translate) / screenWidth * MAX_ANIMATION_DURATION))
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
        img_tom.setImageResource(R.drawable.img_tom_wait_touch);
        img_tom
                .animate()
                .translationY(0)
                .setDuration((long)(Math.abs(translate) / tomMaxTranslate * MAX_ANIMATION_DURATION));
    }
}
