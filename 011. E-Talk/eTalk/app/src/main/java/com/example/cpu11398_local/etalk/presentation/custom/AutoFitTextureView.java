package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.util.AttributeSet;
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
        if (w == 0 || h == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            if (width < height * w / h) {
                super.onMeasure(
                        width | MeasureSpec.EXACTLY,
                        (width * h / w) | MeasureSpec.EXACTLY
                );
            } else {
                super.onMeasure(
                        (height * w / h) | MeasureSpec.EXACTLY,
                        height | MeasureSpec.EXACTLY
                );
            }
        }
    }
}
