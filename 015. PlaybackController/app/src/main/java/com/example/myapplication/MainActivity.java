package com.example.myapplication;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

public class MainActivity extends AppCompatActivity {

    SimpleExoPlayer     player;
    PlayerView          playerView;
    PlaybackContainer   playbackContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView          = findViewById(R.id.player);
        playbackContainer   = findViewById(R.id.playback_container);

        player = ExoPlayerFactory.newSimpleInstance(
                this,
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(),
                new DefaultLoadControl()
        );

        player.prepare(
                new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(this, "Test"))
                        .setExtractorsFactory(new DefaultExtractorsFactory())
                        .createMediaSource(
                                Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4")
                        )
        );

        player.setPlayWhenReady(true);

        playerView.setPlayer(player);
        playbackContainer.setPlayer(player);
    }
}
