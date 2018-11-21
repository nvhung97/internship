package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.example.cpu11398_local.etalk.R;
import com.google.android.exoplayer2.ui.PlayerView;

public class RoundedPlayerView extends PlayerView {

    private RectF rectF = new RectF();
    private int cornerRadius = getContext().getResources().getDimensionPixelSize(R.dimen.map_view_corner);
    private int w = 0;
    private int h = 0;

    public RoundedPlayerView(Context context) {
        super(context);
    }

    public RoundedPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSize(int w, int h) {
        this.w = w;
        this.h = h;
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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        rectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Path path = new Path();
        int count = canvas.save();

        path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW);

        canvas.clipPath(path);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(count);
    }
}
