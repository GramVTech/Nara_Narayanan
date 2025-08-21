package astro.sastikjothidam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class Single_Video_Youtube_player extends AppCompatActivity {

    String videoId = "";
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer youTubePlayer1;
    float current_sec=0;
    ImageView imageView12;
    CustomPlayerUiController customPlayerUiController1;
    View customPlayerUi;
    int play_pause_count=0;

    @Override
    protected void attachBaseContext(Context newBase) {
        Configuration overrideConfiguration = new Configuration(newBase.getResources().getConfiguration());
        overrideConfiguration.fontScale = 1.0f; // Set to 1.0 for normal, or use 0.85f for smaller fonts
        Context context = newBase.createConfigurationContext(overrideConfiguration);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_video_youtube_player);
        intialise();

        YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer1 = youTubePlayer;
                CustomPlayerUiController customPlayerUiController = new CustomPlayerUiController(Single_Video_Youtube_player.this, customPlayerUi, youTubePlayer, youTubePlayerView);
                youTubePlayer.addListener(customPlayerUiController);
                customPlayerUiController1 = customPlayerUiController;
                youTubePlayer.loadVideo(videoId,0);
                try {

                    youTubePlayer1.addListener(new YouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                        }

                        @Override
                        public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState playerState) {

                        }

                        @Override
                        public void onPlaybackQualityChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackQuality playbackQuality) {

                        }

                        @Override
                        public void onPlaybackRateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackRate playbackRate) {

                        }

                        @Override
                        public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError playerError) {

                        }

                        @Override
                        public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float v) {
                            current_sec = v;
                        }

                        @Override
                        public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float v) {

                        }

                        @Override
                        public void onVideoLoadedFraction(@NonNull YouTubePlayer youTubePlayer, float v) {

                        }

                        @Override
                        public void onVideoId(@NonNull YouTubePlayer youTubePlayer, @NonNull String s) {

                        }

                        @Override
                        public void onApiChange(@NonNull YouTubePlayer youTubePlayer) {

                        }
                    });

                }catch (Exception e){}
            }
        };

        // disable web ui
        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).build();
        youTubePlayerView.initialize(listener, options);


        imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_pause_count++;
                if(play_pause_count%2==0){
                    youTubePlayer1.play();
                    imageView12.setImageDrawable(getResources().getDrawable(R.mipmap.play, getApplicationContext().getTheme()));
                }else{
                    youTubePlayer1.pause();
                    imageView12.setImageDrawable(getResources().getDrawable(R.mipmap.pause, getApplicationContext().getTheme()));
                }
            }
        });

    }

    public void intialise(){
        Intent intent = getIntent();
        videoId = intent.getStringExtra("videoId");

        imageView12 = findViewById(R.id.imageView12);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        customPlayerUi = youTubePlayerView.inflateCustomPlayerUi(R.layout.custom_player_ui);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayerView.matchParent();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            youTubePlayerView.wrapContent();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR|View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }
}