package astro.sastikjothidam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Multi_Video_Youtube_Player_With_Comments extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = "";
    String json_string="";
    ProgressDialog progressDialog;
    int play_pause_count=0;
    Intent intent;
    String videoId = "";
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer youTubePlayer1;
    float current_sec=0;
    ImageView imageView12;
    CustomPlayerUiController customPlayerUiController1;
    View customPlayerUi;

    CustomAdapter customAdapter;
    GridView listView;
    List<String> Lclass_id = new ArrayList<>();
    List<String> Lclass_name = new ArrayList<>();
    List<String> Lclass_image_link = new ArrayList<>();
    List<String> Lclass_video_link = new ArrayList<>();
    TextView tvideo_name;
    String class_category_id="";

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
        setContentView(R.layout.activity_multi_video_youtube_player_with_comments);

        intialise();

        progressDialog.show();
        new backgroundworker().execute();


        if(intent.getStringExtra("which_area_msg").equals("Astrology_class")) {
            json_url1 = Url_interface.url+"Astrology_class_base_on_playlist.php";
        }else if(intent.getStringExtra("which_area_msg").equals("Premium_videos")) {
            json_url1 = Url_interface.url+"Premium_video_base_on_playlist.php";
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(intent.getStringExtra("which_area_msg").equals("Astrology_class")) {
                    Intent intent1 = new Intent(Multi_Video_Youtube_Player_With_Comments.this, Multi_Video_Youtube_Player_With_Comments.class);
                    intent1.putExtra("which_area_msg", "Astrology_class");
                    intent1.putExtra("VideoId", Lclass_video_link.get(position));
                    intent1.putExtra("VideoTitle", Lclass_name.get(position));
                    intent1.putExtra("class_category_id", class_category_id);
                    startActivity(intent1);
                }else if(intent.getStringExtra("which_area_msg").equals("Premium_videos")) {
                    Intent intent1 = new Intent(Multi_Video_Youtube_Player_With_Comments.this, Multi_Video_Youtube_Player_With_Comments.class);
                    intent1.putExtra("which_area_msg", "Premium_videos");
                    intent1.putExtra("VideoId", Lclass_video_link.get(position));
                    intent1.putExtra("VideoTitle", Lclass_name.get(position));
                    intent1.putExtra("class_category_id", class_category_id);
                    startActivity(intent1);
                }
            }
        });

        YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer1 = youTubePlayer;
                CustomPlayerUiController customPlayerUiController = new CustomPlayerUiController(Multi_Video_Youtube_Player_With_Comments.this, customPlayerUi, youTubePlayer, youTubePlayerView);
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




        findViewById(R.id.comments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog_Youtube_Message bottomSheetDialog_attendance = new BottomSheetDialog_Youtube_Message(Multi_Video_Youtube_Player_With_Comments.this,intent.getStringExtra("which_area_msg"),videoId);
                bottomSheetDialog_attendance.setCancelable(true);
                bottomSheetDialog_attendance.show(getSupportFragmentManager(), "Dialog");
            }
        });
    }

    public class backgroundworker extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url1);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {

                Lclass_id.clear();
                Lclass_name.clear();
                Lclass_image_link.clear();
                Lclass_video_link.clear();

                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("class_category_id","UTF-8")+"="+URLEncoder.encode(class_category_id,"UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string=bufferedReader.readLine())!=null)
                {
                    sb.append(json_string+"\n");
                    Log.d("json_string",""+json_string);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb.toString());
                return sb.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string = result;
            progressDialog.dismiss();
            int count=0;
            Log.d("RESULT", "" + result);
            try{
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                while(count < jsonArray.length()){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);

                    Lclass_id.add(jsonObject1.getString("id"));
                    Lclass_name.add(jsonObject1.getString("video_name"));
                    Lclass_image_link.add(jsonObject1.getString("video_image_link"));
                    Lclass_video_link.add(jsonObject1.getString("video_link"));
                    count++;
                }
                if(Lclass_id.size()>0){
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e){

            }


        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lclass_id.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.custom_layout_astrology_classes_while_video, null);

            TextView class_name = view.findViewById(R.id.textView16);

            ImageView class_image = view.findViewById(R.id.imageViewc7);

            class_name.setText(" Class - "+String.valueOf(i+1)+" "+Lclass_name.get(i).substring(0,3)+"...");

            Glide
                    .with(Multi_Video_Youtube_Player_With_Comments.this)
                    .load(Lclass_image_link.get(i))
                    .placeholder(R.drawable.progress_animation)
                    .into(class_image);


            return view;

        }


    }

    public void intialise(){

        intent = getIntent();

        progressDialog = new ProgressDialog(Multi_Video_Youtube_Player_With_Comments.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        customAdapter = new CustomAdapter();

        listView = findViewById(R.id.listView);

        videoId = intent.getStringExtra("VideoId");


        imageView12 = findViewById(R.id.imageView12);

        tvideo_name = findViewById(R.id.textView10);

        tvideo_name.setText(intent.getStringExtra("VideoTitle"));

        class_category_id = intent.getStringExtra("class_category_id");

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