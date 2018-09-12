package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class TimeTextView extends android.support.v7.widget.AppCompatTextView{

    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTime(Long time) {
        setText(String.valueOf(time));
    }
}
