package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class VerticalButton extends android.support.v7.widget.AppCompatButton {

    public VerticalButton(Context context) {
        super(context);
    }

    public VerticalButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setTranslationX((getMeasuredHeight() - getMeasuredWidth()) / 2);
        setRotation(-90);
    }
}
