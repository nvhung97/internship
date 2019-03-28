package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class PlaybackContainer extends FrameLayout {

    private SimpleExoPlayer player;
    private Handler         progressHandler;

    private ImageButton play;
    private ImageButton pause;
    private SeekBar     seekBar;
    private TimeView    time;
    private ImageButton mute;
    private ImageButton unmute;
    private ImageButton fullScreen;
    private ImageButton exitFullScreen;

    private int mHeight;
    private int mWidth;

    private float   currentVolume   = 0.0f;
    private boolean isTouching      = false;

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
        mHeight = getResources().getDimensionPixelSize(R.dimen.playback_height);
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
            if (player != null) {
                player.setPlayWhenReady(true);
            }
        });

        pause.setOnClickListener(v -> {
            play.setVisibility(VISIBLE);
            pause.setVisibility(GONE);
            if (player != null) {
                player.setPlayWhenReady(false);
            }
        });

        mute.setOnClickListener(v -> {
            mute.setVisibility(GONE);
            unmute.setVisibility(VISIBLE);
            if (player != null) {
                currentVolume = player.getVolume();
                player.setVolume(0.0f);
            }
        });

        unmute.setOnClickListener(v -> {
            mute.setVisibility(VISIBLE);
            unmute.setVisibility(GONE);
            if (player != null) {
                player.setVolume(currentVolume);
            }
        });

        fullScreen.setOnClickListener(v -> {
            fullScreen.setVisibility(GONE);
            exitFullScreen.setVisibility(VISIBLE);
        });

        exitFullScreen.setOnClickListener(v -> {
            fullScreen.setVisibility(VISIBLE);
            exitFullScreen.setVisibility(GONE);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && player != null) {
                    player.seekTo(progress * player.getDuration() / 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouching = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouching = false;
            }
        });
    }

    public void live() {
        play.setVisibility(GONE);
        pause.setVisibility(GONE);
        seekBar.setVisibility(GONE);
        time.setVisibility(GONE);
        mute.setVisibility(GONE);
        unmute.setVisibility(GONE);
    }

    public void setPlayer(SimpleExoPlayer player) {
        this.player = player;
        if (seekBar.getVisibility() != GONE) {
            progressHandler = new Handler();
            progressHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (player.getPlaybackState() != 0) {
                        if (player.getPlayWhenReady() &&
                                (player.getPlaybackState() == Player.STATE_READY || player.getPlaybackState() == Player.STATE_BUFFERING)) {
                            if (!isTouching) {
                                seekBar.setProgress((int) (player.getCurrentPosition() * 100 / player.getDuration()));
                            }
                            seekBar.setSecondaryProgress((int) (player.getBufferedPosition() * 100 / player.getDuration()));
                            time.setRemainingTime(player.getDuration() - player.getCurrentPosition());
                            progressHandler.postDelayed(this, 1000 - player.getCurrentPosition() % 1000);
                        } else {
                            progressHandler.postDelayed(this, 1000);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mWidth = MeasureSpec.getSize(widthMeasureSpec);

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

        if (fullScreen.getVisibility() != GONE) {
            fullScreen.measure(
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
