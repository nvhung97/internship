package com.example.cpu11398_local.etalk.presentation.view.chat.media;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import com.example.cpu11398_local.etalk.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class MediaVideoActivity extends AppCompatActivity {

    private ExoPlayer   player;
    private PlayerView  playerView;
    private ProgressBar progressBar;
    private ImageButton fullScreen;
    private ImageButton exitFullScreen;
    private ImageButton exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_media_video);

        playerView      = findViewById(R.id.exo_player);
        fullScreen      = findViewById(R.id.exo_full_screen);
        exitFullScreen  = findViewById(R.id.exo_exit_full_screen);
        progressBar     = findViewById(R.id.exo_loading);
        exit            = findViewById(R.id.exo_cancel);

        fullScreen.setOnClickListener(v -> {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            fullScreen.setVisibility(View.GONE);
            exitFullScreen.setVisibility(View.VISIBLE);
        });
        exitFullScreen.setOnClickListener(v -> {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            fullScreen.setVisibility(View.VISIBLE);
            exitFullScreen.setVisibility(View.GONE);
        });
        exit.setOnClickListener(v -> finish());

        initializePlayer();
    }

    private void initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(
                            new AdaptiveTrackSelection.Factory(
                                    new DefaultBandwidthMeter()
                            )
                    ),
                    new DefaultLoadControl()
            );
            player.setPlayWhenReady(true);
            player.addListener(new Player.DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case PlaybackStateCompat.STATE_PAUSED:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case PlaybackStateCompat.STATE_PLAYING:
                            progressBar.setVisibility(View.GONE);
                            break;
                        case PlaybackStateCompat.STATE_FAST_FORWARDING:
                            finish();
                            break;
                    }
                }
            });
            playerView.setPlayer(player);
            player.seekTo(
                    player.getCurrentWindowIndex(),
                    60000/*getIntent().getExtras().getLong("position")*/
            );
        }
        player.prepare(
                buildMediaSource("http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4"/*getIntent().getExtras().getString("url")*/),
                false,
                true
        );
    }

    private MediaSource buildMediaSource(String link) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(
                        getPackageName(),
                        null,
                        DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                        DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                        true
                )
        ).createMediaSource(Uri.parse(link));
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }
}