package astro.sastikjothidam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class Generate_horoscope_Open extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"Cloud_Horoscope_Open.php";
    String json_string="";

    List<String> Lname = new ArrayList<>();
    List<String> Ldob = new ArrayList<>();
    List<String> Ltob = new ArrayList<>();
    List<String> Lpob = new ArrayList<>();
    List<String> Lcountry = new ArrayList<>();
    List<String> Llat = new ArrayList<>();
    List<String> Llon = new ArrayList<>();
    List<String> Ltz = new ArrayList<>();
    List<String> Lid = new ArrayList<>();

    CustomAdapter customAdapter;
    ListView listView;

    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;

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
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generate_horoscope_open);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intialise();
        progressDialog.show();
        new backgroundworker().execute();
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
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mail","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8");
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

                    Lname.add(jsonObject1.getString("name"));
                    Ldob.add(jsonObject1.getString("dob"));
                    Ltob.add(jsonObject1.getString("tob"));
                    Lpob.add(jsonObject1.getString("pob"));
                    Lcountry.add(jsonObject1.getString("country"));
                    Llat.add(jsonObject1.getString("lat"));
                    Llon.add(jsonObject1.getString("lon"));
                    Ltz.add(jsonObject1.getString("tz"));
                    Lid.add(jsonObject1.getString("id"));

                    count++;
                }
                if(Lname.size()>0){
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e){

            }
        }
    }

    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Lname.size();
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

            if (view != null){
                return view;
            }

            view = getLayoutInflater().inflate(R.layout.custom_layout_open_edit_horoscope, null);

            TextView txt10_name = view.findViewById(R.id.textView10);
            TextView txt_other_details = view.findViewById(R.id.textView102);
            ImageView edit = view.findViewById(R.id.imageView45);
            ImageView open = view.findViewById(R.id.imageView46);

            txt10_name.setText(Lname.get(i));
            String other_details = Ldob.get(i)+" : "+Ltob.get(i)+" "+Lpob.get(i);
            txt_other_details.setText(other_details);

            open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Generate_horoscope_Open.this,Generate_horoscope_main_menu.class);
                    intent.putExtra("time",Ltob.get(i));
                    intent.putExtra("date",Ldob.get(i).toString());
                    intent.putExtra("name",Lname.get(i).toString().trim());
                    intent.putExtra("city",Lpob.get(i).toString().trim());
                    intent.putExtra("country",Lcountry.get(i).toString().trim());
                    intent.putExtra("lat",Llat.get(i).toString().trim());
                    intent.putExtra("lon",Llon.get(i).toString().trim());
                    intent.putExtra("tz",Ltz.get(i).toString().trim());
                    intent.putExtra("id",Lid.get(i).toString().trim());
                    startActivity(intent);
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Generate_horoscope_Open.this,Generate_horoscope_birth_details.class);
                    Intent intent1 = getIntent();
                    intent.putExtra("time",Ltob.get(i));
                    intent.putExtra("date",Ldob.get(i).toString());
                    intent.putExtra("name",Lname.get(i).toString().trim());
                    intent.putExtra("city",Lpob.get(i).toString().trim());
                    intent.putExtra("country",Lcountry.get(i).toString().trim());
                    intent.putExtra("lat",Llat.get(i).toString().trim());
                    intent.putExtra("lon",Llon.get(i).toString().trim());
                    intent.putExtra("tz",Ltz.get(i).toString().trim());
                    intent.putExtra("id",Lid.get(i).toString().trim());
                    startActivity(intent);
                }
            });

            return view;
        }


    }

    public void intialise(){

        progressDialog = new ProgressDialog(Generate_horoscope_Open.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(Generate_horoscope_Open.this);

        customAdapter = new CustomAdapter();

        listView = findViewById(R.id.listView);
        listView.setDivider(null);

    }
}