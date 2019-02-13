package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

public class AutoFitTextureView extends TextureView {

    private int w, h;

    public AutoFitTextureView(Context context) {
        super(context);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAspectRatio(int w, int h) {
        this.w = w;
        this.h = h;
        requestLayout();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == w || 0 == h) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * w / h) {
                setMeasuredDimension(width, width * w / h);
            } else {
                setMeasuredDimension(height * w / h, height);
            }
        }
    }
}
