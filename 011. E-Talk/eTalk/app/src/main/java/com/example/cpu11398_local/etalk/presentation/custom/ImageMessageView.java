package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class ImageMessageView extends android.support.v7.widget.AppCompatImageView{

    private int w = 0;
    private int h = 0;

    public ImageMessageView(Context context) {
        super(context);
        init();
    }

    public ImageMessageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageMessageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClipToOutline(true);
    }

    public void setSize(int w, int h) {
        this.w = w;
        this.h = h;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (w > h) {
            super.onMeasure(
                    MeasureSpec.getSize(widthMeasureSpec) | MeasureSpec.EXACTLY,
                    MeasureSpec.getSize(widthMeasureSpec) * h / w | MeasureSpec.EXACTLY
            );
        } else {
            super.onMeasure(
                    MeasureSpec.getSize(widthMeasureSpec) * w / h| MeasureSpec.EXACTLY,
                    MeasureSpec.getSize(widthMeasureSpec) | MeasureSpec.EXACTLY
            );
        }
    }
}
