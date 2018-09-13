package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.cpu11398_local.etalk.R;

public class StatusCircleView extends View{

    private Handler handler = new Handler();

    public StatusCircleView(Context context) {
        super(context);
        init();
    }

    public StatusCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatusCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(Float.MAX_VALUE);
        shape.setStroke(
                (int)getContext().getResources().getDimension(R.dimen.status_view_stroke_width),
                getContext().getResources().getColor(R.color.colorWhite)
        );
        shape.setColor(getContext().getResources().getColor(R.color.colorGreen));
        setBackground(shape);
        setVisibility(GONE);
    }

    public void setStatus(Long time) {
        handler.removeCallbacksAndMessages(null);
        if (System.currentTimeMillis() - time < 10000) {
            setVisibility(VISIBLE);
            handler.postDelayed(
                    () -> setVisibility(GONE),
                    10000
            );
        } else {
            setVisibility(GONE);
        }
    }
}
