package astro.sastikjothidam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

class CustomPlayerUiController extends AbstractYouTubePlayerListener {
    private final YouTubePlayerTracker playerTracker;
    private final Context context;
    private final YouTubePlayer youTubePlayer;
    private final YouTubePlayerView youTubePlayerView;
    // panel is used to intercept clicks on the WebView, I don't want the user to be able to click the WebView directly.
    private View panel;
    float vid_dur;
    SeekBar seekBar;
    String ab1;
    TextView textView18;
    Float current_time_sec=0f;
    private boolean fullscreen = false;

    CustomPlayerUiController(Context context, View customPlayerUi, YouTubePlayer youTubePlayer, YouTubePlayerView youTubePlayerView) {
        this.context = context;
        this.youTubePlayer = youTubePlayer;
        this.youTubePlayerView = youTubePlayerView;

        playerTracker = new YouTubePlayerTracker();
        youTubePlayer.addListener(playerTracker);

        initViews(customPlayerUi);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser)
                    {
                        youTubePlayer.seekTo(progress);

                    }
                }
        );


    }

    private void initViews(View playerUi) {
        panel = playerUi.findViewById(R.id.panel);
        seekBar=playerUi.findViewById(R.id.seekBar2);
        textView18 = playerUi.findViewById(R.id.textView18);
        textView18 = playerUi.findViewById(R.id.textView18);
    }

    @Override
    public void onReady(@NonNull YouTubePlayer youTubePlayer) {

    }

    @Override
    public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
        if (state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED)
            panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        else if (state == PlayerConstants.PlayerState.BUFFERING)
            panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
        current_time_sec = second*1000;
        int seconds = (int) (current_time_sec / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        String ab = (hours == 0 ? "--:" : hours + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60);
        textView18.setText(String.valueOf(ab)+"/"+ab1);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float duration) {
        seekBar.setMax(Math.round(duration));
        vid_dur = duration;
        vid_dur = vid_dur*1000;
        int seconds1 = (int) (vid_dur / 1000);
        int minutes1 = seconds1 / 60;
        int hours1 = minutes1 / 60;
        ab1 = (hours1 == 0 ? "--:" : hours1 + ":") + String.format("%02d:%02d", minutes1 % 60, seconds1 % 60);
    }
}
