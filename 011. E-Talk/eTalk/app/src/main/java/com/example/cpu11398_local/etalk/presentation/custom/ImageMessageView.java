package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class ImageMessageView extends android.support.v7.widget.AppCompatImageView{

    private int w = 0;
    private int h = 0;

    public ImageMessageView(Context context) {
        super(context);
    }

    public ImageMessageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageMessageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(Drawable drawable){
        w = drawable.getIntrinsicWidth();
        h = drawable.getIntrinsicHeight();
        setImageDrawable(drawable);
    }

    public void setSize(int w, int h) {
        this.w = w;
        this.h = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(
                (w != h && w < h)
                        ? (MeasureSpec.getSize(widthMeasureSpec) * w / h) | MeasureSpec.EXACTLY
                        : widthMeasureSpec,
                MeasureSpec.UNSPECIFIED
        );
    }
}
