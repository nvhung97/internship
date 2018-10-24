package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.example.cpu11398_local.etalk.R;
import java.util.Calendar;

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

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        int nowYear     = now.get(Calendar.YEAR);
        int nowMonth    = now.get(Calendar.MONTH) + 1;
        int nowDay      = now.get(Calendar.DAY_OF_MONTH);

        Calendar when = Calendar.getInstance();
        when.setTimeInMillis(time);
        int whenYear    = when.get(Calendar.YEAR);
        int whenMonth   = when.get(Calendar.MONTH) + 1;
        int whenDay     = when.get(Calendar.DAY_OF_MONTH);
        String whenMonthZero = ((whenMonth < 10) ? "0" : "");
        String whenDayZero   = ((whenDay < 10) ? "0" : "");

        String textTime = "";

        if (whenYear < nowYear) {
            textTime = whenDayZero + whenDay + "/" + whenMonthZero + whenMonth + "/" + whenYear;
        } else if ((whenMonth < nowMonth) || (nowDay - whenDay > 5)) {
            textTime = whenDayZero + whenDay + "/" + whenMonthZero + whenMonth;
        } else if ((nowDay - whenDay > 0)) {
            if (nowDay - whenDay == 1) {
                textTime = "1 " + getContext().getString(R.string.app_time_day);
            } else {
                textTime = (nowDay - whenDay)
                        + " "
                        + getContext().getString(R.string.app_time_days);
            }
        } else if (((now.getTimeInMillis() - when.getTimeInMillis()) / 3600000L) >= 1) {
            if (((now.getTimeInMillis() - when.getTimeInMillis()) / 3600000L) == 1) {
                textTime = ((now.getTimeInMillis() - when.getTimeInMillis()) / 3600000L)
                        + " "
                        + getContext().getString(R.string.app_time_hour);
            } else {
                textTime = ((now.getTimeInMillis() - when.getTimeInMillis()) / 3600000L)
                        + " "
                        + getContext().getString(R.string.app_time_hours);
            }
        } else if (((now.getTimeInMillis() - when.getTimeInMillis()) / 60000L) > 1) {
            textTime = ((now.getTimeInMillis() - when.getTimeInMillis()) / 60000L)
                    + " "
                    + getContext().getString(R.string.app_time_minutes);
        } else {
            textTime = "1 " + getContext().getString(R.string.app_time_minute);
        }

        setText(textTime);
    }
}
