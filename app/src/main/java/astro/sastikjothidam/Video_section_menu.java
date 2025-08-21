package astro.sastikjothidam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class Video_section_menu extends AppCompatActivity{


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
        setContentView(R.layout.activity_video_section_menu);
        intialise();

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat insetsController =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());

        // Hide both status bar and navigation bar
        insetsController.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());

        // Optional: Make them re-appear with swipe
        insetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        findViewById(R.id.premium_videos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Video_section_menu.this, Premium_video_playlist.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.astrology_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Video_section_menu.this, Astrology_class_playlist.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.live_classes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Video_section_menu.this, Live_classes.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.free_vid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String channelUrl = "https://www.youtube.com/watch?v=oG1Jt8BrDQg&list=PLu9PXRE9TXaoggmZz3k66Kxfugtb8CpXh";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(channelUrl));
                intent.setPackage("com.google.android.youtube");

                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(channelUrl));
                    startActivity(webIntent);
                }
            }
        });
    }
    public void intialise(){
    }
}