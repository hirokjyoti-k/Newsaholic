package com.adbu.newsaholic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public class LiveTVPlayer extends AppCompatActivity {

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private MediaItem mediaItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_tvplayer);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        String newsUrl = intent.getStringExtra("newsUrl");

        player = new SimpleExoPlayer.Builder(this).build();

        playerView = (PlayerView) findViewById(R.id.idExoPlayerView);
        playerView.setPlayer(player);

        mediaItem = MediaItem.fromUri(newsUrl);

        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.play();
    }
}