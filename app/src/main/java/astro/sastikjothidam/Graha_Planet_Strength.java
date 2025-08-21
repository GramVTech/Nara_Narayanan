package astro.sastikjothidam;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Graha_Planet_Strength extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = "https://astrochinnaraj.net/1_astro_software/Finding_Graha_Strength.php";
    String json_string="";
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    CustomAdapter customAdapter;
    ListView listView;

    List<String> Lclass_sign = new ArrayList<>();
    List<String> Lclass_res = new ArrayList<>();
    Map<String,String> Lsign_map = new HashMap<>();

    Map<String, String> zodiac_signs_map = new HashMap<>();

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
        setContentView(R.layout.activity_graha_planet_strength);
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

                Lclass_sign.clear();
                Lclass_res.clear();
                Lsign_map.clear();

                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(intent.getStringExtra("date"),"UTF-8")+"&"
                        +URLEncoder.encode("time","UTF-8")+"="+ URLEncoder.encode(intent.getStringExtra("time"),"UTF-8")+"&"
                        +URLEncoder.encode("lat","UTF-8")+"="+ URLEncoder.encode(intent.getStringExtra("lat"),"UTF-8")+"&"
                        +URLEncoder.encode("lon","UTF-8")+"="+ URLEncoder.encode(intent.getStringExtra("lon"),"UTF-8")+"&"
                        +URLEncoder.encode("tz","UTF-8")+"="+ URLEncoder.encode(intent.getStringExtra("tz"),"UTF-8");
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
                JSONArray jsonArray = new JSONArray(json_string);
                while(count<jsonArray.length()){
                    JSONObject jsonObject = jsonArray.getJSONObject(count);
                    for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                        String key = it.next();
                        JSONArray values = jsonObject.getJSONArray(key);
                        Lclass_sign.add(key);
                        Lclass_res.add(values.toString());
                        Lsign_map.put(key,values.toString());
                        Log.d("TAG", "Key: " + key);
                        Log.d("TAG", "Value: " + values.toString());
                    }
                    count++;
                }
                if(count>0){
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e){
                Toast.makeText(Graha_Planet_Strength.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            }


        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lclass_sign.size();
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

            view = getLayoutInflater().inflate(R.layout.custom_layout_planets_strength, null);

            TextView rasi = view.findViewById(R.id.textView90);

            List<ImageView> img_list = new ArrayList<>();

            ImageView rule1 = view.findViewById(R.id.imageView35);
            ImageView rule2 = view.findViewById(R.id.imageView36);
            ImageView rule3 = view.findViewById(R.id.imageView37);
            ImageView rule4 = view.findViewById(R.id.imageView38);
            ImageView rule5 = view.findViewById(R.id.imageView39);
            ImageView rule6 = view.findViewById(R.id.imageView40);
            ImageView rule7 = view.findViewById(R.id.imageView41);
            ImageView rule8 = view.findViewById(R.id.imageView42);
            ImageView rule9 = view.findViewById(R.id.imageView43);
            ImageView rule10 = view.findViewById(R.id.imageView44);

            TextView mark = view.findViewById(R.id.textView100);

            img_list.add(rule1);
            img_list.add(rule2);
            img_list.add(rule3);
            img_list.add(rule4);
            img_list.add(rule5);
            img_list.add(rule6);
            img_list.add(rule7);
            img_list.add(rule8);
            img_list.add(rule9);
            img_list.add(rule10);


            rasi.setText(zodiac_signs_map.get(Lclass_sign.get(i)));
            int counter = 0;
            try {
                JSONArray jsonArray = new JSONArray(Lclass_res.get(i));
                for(int j=0;j<jsonArray.length();j++){
                    ImageView img = img_list.get(j);
                    Log.d("ASASQWERTY",""+jsonArray.get(j));
                    if(jsonArray.get(j).equals("NOT PASSED")){
                        img.setImageResource(R.mipmap.close);
                    }else{
                        img.setImageResource(R.mipmap.checkmark);
                        counter++;
                    }
                }
                mark.setText(String.valueOf(counter)+"/10");

                img_list.clear();
            }catch (Exception e){

            }

            return view;

        }


    }

    public void intialise(){

        progressDialog = new ProgressDialog(Graha_Planet_Strength.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(Graha_Planet_Strength.this);

        customAdapter = new CustomAdapter();

        zodiac_signs_map.put("Aries", "மேஷம்");
        zodiac_signs_map.put("Taurus", "ரிஷபம்");
        zodiac_signs_map.put("Gemini", "மிதுனம்");
        zodiac_signs_map.put("Cancer", "கடகம்");
        zodiac_signs_map.put("Leo", "சிம்மம்");
        zodiac_signs_map.put("Virgo", "கன்னி");
        zodiac_signs_map.put("Libra", "துலாம்");
        zodiac_signs_map.put("Scorpio", "விருச்சிகம்");
        zodiac_signs_map.put("Sagittarius", "தனுசு");
        zodiac_signs_map.put("Capricorn", "மகரம்");
        zodiac_signs_map.put("Aquarius", "கும்பம்");
        zodiac_signs_map.put("Pisces", "மீனம்");

        intent = getIntent();

        listView = findViewById(R.id.listView);
        listView.setDivider(null);

    }
}