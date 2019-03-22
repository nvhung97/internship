package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlaybackContainer extends FrameLayout {

    private ImageButton play;
    private ImageButton pause;
    private SeekBar     seekBar;
    private TextView    time;
    private ImageButton mute;
    private ImageButton unmute;
    private ImageButton fullScreen;
    private ImageButton exitFullScreen;

    private int mHeight;
    private int mWidth;

    private boolean isLive = false;

    public PlaybackContainer(@NonNull Context context) {
        super(context);
        init();
    }

    public PlaybackContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlaybackContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHeight = getResources().getDimensionPixelSize(R.dimen.playback_controller_height);
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        play            = findViewById(R.id.play);
        pause           = findViewById(R.id.pause);
        seekBar         = findViewById(R.id.seek_bar);
        time            = findViewById(R.id.time);
        mute            = findViewById(R.id.mute);
        unmute          = findViewById(R.id.unmute);
        fullScreen      = findViewById(R.id.full_screen);
        exitFullScreen  = findViewById(R.id.exit_full_screen);

        play.setOnClickListener(v -> {
            play.setVisibility(GONE);
            pause.setVisibility(VISIBLE);
        });

        pause.setOnClickListener(v -> {
            play.setVisibility(VISIBLE);
            pause.setVisibility(GONE);
        });

        mute.setOnClickListener(v -> {
            mute.setVisibility(GONE);
            unmute.setVisibility(VISIBLE);
        });

        unmute.setOnClickListener(v -> {
            mute.setVisibility(VISIBLE);
            unmute.setVisibility(GONE);
        });

        fullScreen.setOnClickListener(v -> {
            fullScreen.setVisibility(GONE);
            exitFullScreen.setVisibility(VISIBLE);
        });

        exitFullScreen.setOnClickListener(v -> {
            fullScreen.setVisibility(VISIBLE);
            exitFullScreen.setVisibility(GONE);
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mWidth = MeasureSpec.getSize(widthMeasureSpec);

        if (fullScreen.getVisibility() != GONE) {
            fullScreen.measure(
                    mHeight | MeasureSpec.EXACTLY,
                    mHeight | MeasureSpec.EXACTLY
            );
        }

        if (play.getVisibility() != GONE) {
            play.measure(
                    mHeight | MeasureSpec.EXACTLY,
                    mHeight | MeasureSpec.EXACTLY
            );
        }

        if (pause.getVisibility() != GONE) {
            pause.measure(
                    mHeight | MeasureSpec.EXACTLY,
                    mHeight | MeasureSpec.EXACTLY
            );
        }

        if (time.getVisibility() != GONE) {
            time.measure(
                    MeasureSpec.UNSPECIFIED | MeasureSpec.UNSPECIFIED,
                    mHeight | MeasureSpec.EXACTLY
            );
        }

        if (mute.getVisibility() != GONE) {
            mute.measure(
                    mHeight | MeasureSpec.EXACTLY,
                    mHeight | MeasureSpec.EXACTLY
            );
        }

        if (unmute.getVisibility() != GONE) {
            unmute.measure(
                    mHeight | MeasureSpec.EXACTLY,
                    mHeight | MeasureSpec.EXACTLY
            );
        }

        if (exitFullScreen.getVisibility() != GONE) {
            exitFullScreen.measure(
                    mHeight | MeasureSpec.EXACTLY,
                    mHeight | MeasureSpec.EXACTLY
            );
        }

        if (seekBar.getVisibility() != GONE) {
            seekBar.measure(
                    (mWidth - 3 * mHeight - time.getMeasuredWidth()) | MeasureSpec.EXACTLY,
                    mHeight | MeasureSpec.EXACTLY
            );
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int r = mWidth;
        int t = 0;

        if (fullScreen.getVisibility() != GONE) {
            fullScreen.layout(r - fullScreen.getMeasuredWidth(), t, r, t + fullScreen.getMeasuredHeight());
            r -= fullScreen.getMeasuredWidth();
        }

        if (exitFullScreen.getVisibility() != GONE) {
            exitFullScreen.layout(r - exitFullScreen.getMeasuredWidth(), t, r, t + exitFullScreen.getMeasuredHeight());
            r -= exitFullScreen.getMeasuredWidth();
        }

        if (mute.getVisibility() != GONE) {
            mute.layout(r - mute.getMeasuredWidth(), t, r, t + mute.getMeasuredHeight());
            r -= mute.getMeasuredWidth();
        }

        if (unmute.getVisibility() != GONE) {
            unmute.layout(r - unmute.getMeasuredWidth(), t, r, t + unmute.getMeasuredHeight());
            r -= unmute.getMeasuredWidth();
        }

        if (time.getVisibility() != GONE) {
            time.layout(r - time.getMeasuredWidth(), t, r, t + time.getMeasuredHeight());
            r -= time.getMeasuredWidth();
        }

        if (seekBar.getVisibility() != GONE) {
            seekBar.layout(r - seekBar.getMeasuredWidth(), t, r, t + seekBar.getMeasuredHeight());
            r -= seekBar.getMeasuredWidth();
        }

        if (play.getVisibility() != GONE) {
            play.layout(r - play.getMeasuredWidth(), t, r, t + play.getMeasuredHeight());
        }

        if (pause.getVisibility() != GONE) {
            pause.layout(r - pause.getMeasuredWidth(), t, r, t + pause.getMeasuredHeight());
        }
    }
}
