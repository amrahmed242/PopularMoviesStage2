package com.nanodegree.android.popularmovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.nanodegree.android.popularmovies.R;

public class TrailerActivity extends YouTubeBaseActivity {

    //region variables
    private String trailer_id;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    //endregion

    //region @Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);


        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player);

        Intent intent = getIntent();

        if (intent.hasExtra("trailer_id")) {
            trailer_id = intent.getStringExtra("trailer_id");

            onInitializedListener = new YouTubePlayer.OnInitializedListener() {

                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                    youTubePlayer.loadVideo(trailer_id);
                    youTubePlayer.play();
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            };
        }

        youTubePlayerView.initialize(getResources().getString(R.string.Youtube_Player_Config), onInitializedListener);


    }
    //endregion
}

