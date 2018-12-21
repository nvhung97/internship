package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class VerticalClockView extends ClockView {

    public VerticalClockView(Context context) {
        super(context);
    }

    public VerticalClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setTranslationX((getMeasuredHeight() - getMeasuredWidth()) / 2);
        setRotation(-90);
    }
}
