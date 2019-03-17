package com.example.keyboarddemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

public class CustomPopupView extends KeyboardView {

    private Key currentKey;

    public CustomPopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPopupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean onLongPress(Key popupKey) {
        currentKey = popupKey;
        invalidate();
        return super.onLongPress(popupKey);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (currentKey != null) {
            Drawable drawable = getContext().getDrawable(R.drawable.bg_preview_hit);
            drawable.setState(currentKey.getCurrentDrawableState());
            drawable.setBounds(
                    currentKey.x,
                    currentKey.y,
                    currentKey.x + currentKey.width,
                    currentKey.y + currentKey.height
            );
            drawable.draw(canvas);
        }
        super.onDraw(canvas);
    }
}
