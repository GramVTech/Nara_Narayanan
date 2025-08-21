package astro.sastikjothidam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

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

public class Astrology_Class_List_View extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"Astrology_class_base_on_playlist.php";
    String json_string="";

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"Astrology_class_base_on_playlist_with_filter.php";
    String json_string2="";

    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    List<String> Lclass_id = new ArrayList<>();
    List<String> Lclass_name = new ArrayList<>();
    List<String> Lclass_description = new ArrayList<>();
    List<String> Lclass_image_link = new ArrayList<>();
    List<String> Lclass_video_link = new ArrayList<>();
    CustomAdapter customAdapter;
    ListView listView;
    AutoCompleteTextView autocompletetextView;
    String selected_text="";
    Intent intent;

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
        setContentView(R.layout.activity_astrology_class_list_view);

        intialise();

        progressDialog.show();
        new backgroundworker().execute();

        autocompletetextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                //Toast.makeText(Astrology_Class_List_View.this, ""+selected, Toast.LENGTH_SHORT).show();
                selected_text = selected;
                progressDialog.show();
                new backgroundworker2().execute();
            }
        });

        findViewById(R.id.imageView6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autocompletetextView.setText("");
                progressDialog.show();
                new backgroundworker().execute();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(Astrology_Class_List_View.this, Multi_Video_Youtube_Player_With_Comments.class);
                intent1.putExtra("which_area_msg","Astrology_class");
                intent1.putExtra("VideoId",Lclass_video_link.get(position));
                intent1.putExtra("VideoTitle",Lclass_name.get(position));
                intent1.putExtra("class_category_id",intent.getStringExtra("class_category_id"));
                startActivity(intent1);
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
                Lclass_description.clear();
                Lclass_image_link.clear();
                Lclass_video_link.clear();

                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("class_category_id","UTF-8")+"="+URLEncoder.encode(intent.getStringExtra("class_category_id"),"UTF-8");
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
                    Lclass_description.add(jsonObject1.getString("video_description"));
                    Lclass_image_link.add(jsonObject1.getString("video_image_link"));
                    Lclass_video_link.add(jsonObject1.getString("video_link"));
                    count++;
                }
                if(Lclass_id.size()>0){
                    listView.setAdapter(customAdapter);
                    ArrayAdapter<String> Astrology_class_adapter=new ArrayAdapter<String>(Astrology_Class_List_View.this,android.R.layout.simple_dropdown_item_1line,Lclass_name);
                    autocompletetextView.setThreshold(1);
                    autocompletetextView.setAdapter(Astrology_class_adapter);
                }
            }catch (Exception e){

            }


        }
    }

    public class backgroundworker2 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url2);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {

                Lclass_id.clear();
                Lclass_name.clear();
                Lclass_description.clear();
                Lclass_image_link.clear();
                Lclass_video_link.clear();

                sb2=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("class_category_id","UTF-8")+"="+URLEncoder.encode(intent.getStringExtra("class_category_id"),"UTF-8")+"&"
                        +URLEncoder.encode("selected_class_name","UTF-8")+"="+ URLEncoder.encode(selected_text,"UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string2=bufferedReader.readLine())!=null)
                {
                    sb2.append(json_string2+"\n");
                    Log.d("json_string",""+json_string2);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb2.toString());
                return sb2.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string2 = result;
            progressDialog.dismiss();
            int count=0;
            Log.d("RESULT", "" + result);
            try{
                JSONObject jsonObject = new JSONObject(json_string2);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                while(count < jsonArray.length()){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);

                    Lclass_id.add(jsonObject1.getString("id"));
                    Lclass_name.add(jsonObject1.getString("video_name"));
                    Lclass_description.add(jsonObject1.getString("video_description"));
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

            view = getLayoutInflater().inflate(R.layout.custom_layout_premium_videos, null);

            TextView class_info = view.findViewById(R.id.textView13);

            ImageView class_image = view.findViewById(R.id.imageView7);

            ImageView iclass_image = view.findViewById(R.id.imageView5);

            try {
                Glide
                        .with(Astrology_Class_List_View.this)
                        .load(Lclass_image_link.get(i))
                        .into(class_image);

            }catch (Exception e){
                Log.e("AQWERRTT",""+e);
            }

            class_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetDialog_For_Premium_video_details bottomSheetDialogForPremiumVideoDetails = new BottomSheetDialog_For_Premium_video_details(Astrology_Class_List_View.this,
                            Lclass_name.get(i),Lclass_description.get(i));
                    bottomSheetDialogForPremiumVideoDetails.setCancelable(true);
                    bottomSheetDialogForPremiumVideoDetails.show(getSupportFragmentManager(), "Dialog");
                }
            });

            iclass_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetDialog_For_Premium_video_details bottomSheetDialogForPremiumVideoDetails = new BottomSheetDialog_For_Premium_video_details(Astrology_Class_List_View.this,
                            Lclass_name.get(i),Lclass_description.get(i));
                    bottomSheetDialogForPremiumVideoDetails.setCancelable(true);
                    bottomSheetDialogForPremiumVideoDetails.show(getSupportFragmentManager(), "Dialog");
                }
            });

            return view;

        }


    }

    public void intialise(){

        progressDialog = new ProgressDialog(Astrology_Class_List_View.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(Astrology_Class_List_View.this);

        customAdapter = new CustomAdapter();

        listView = findViewById(R.id.listView);
        listView.setDivider(null);

        autocompletetextView=(AutoCompleteTextView)findViewById(R.id.editTextText);

        intent = getIntent();
    }
}