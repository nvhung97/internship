package com.example.myapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class TimeView extends android.support.v7.widget.AppCompatTextView {

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRemainingTime(long remainingTime) {
        // get number of hours
        long h = remainingTime / (1000 * 60 * 60);
        remainingTime %= (1000 * 60 * 60);

        // get number of minutes
        long m = remainingTime / (1000 * 60);
        remainingTime %= (1000 * 60);

        // get number of seconds. If there are still several milliseconds, add one to {@code s}
        long s = remainingTime / 1000 + (remainingTime % 1000 > 0 ? 1 : 0);

        String text = "-";

        // add hour
        if (h > 0) {
            text += h + ":";
        }

        // add minute
        if (m == 0) {
            text += "00:";
        } else if (m < 10) {
            text += "0" + m + ":";
        } else {
            text += m + ":";
        }

        // add second
        if (s == 0) {
            text += "00";
        } else if (s < 10) {
            text += "0" + s;
        } else {
            text += s;
        }

        setText(text);
    }
}
