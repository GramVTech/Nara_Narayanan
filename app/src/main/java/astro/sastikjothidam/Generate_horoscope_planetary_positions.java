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
import android.widget.TextView;

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
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generate_horoscope_planetary_positions extends AppCompatActivity {

    Map<String, String> naksathara_map = new HashMap<String, String>();
    Map<String, String> zodiac_signs_map = new HashMap<>();
    ProgressDialog progressDialog;
    Intent intent;
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = "http://astrochinnaraj.net/1_astro_software/rasi_chart_api.php";
    String json_string2 = "";
    List<String> planet_norm_degree = new ArrayList<>();
    List<String> planet_rasis = new ArrayList<>();
    List<String> planet_naks = new ArrayList<>();
    List<String> planet_house = new ArrayList<>();
    TextView textView72,textViews72,textViewmo72,textViewma72,textViewme72,textViewju72,textViewve72,textViewsa72,textViewra72,textViewke72,
            textView73,textViews73,textViewmo73,textViewma73,textViewme73,textViewju73,textViewve73,textViewsa73,textViewra73,textViewke73,
            textView74,textViews74,textViewmo74,textViewma74,textViewme74,textViewju74,textViewve74,textViewsa74,textViewra74,textViewke74,
            textView75,textViews75,textView76,textView77,textView78,textView788,textView7888,textView78888,textView788888,textView7558888;

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
        setContentView(R.layout.activity_generate_horoscope_planetary_positions);

        intialise();

        progressDialog.show();
        new backgroundworker2().execute();

        findViewById(R.id.cardView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate_horoscope_planetary_positions.this,Generate_horoscope_birth_details.class);
                Intent intent1 = getIntent();
                intent.putExtra("date",intent1.getStringExtra("date"));
                intent.putExtra("time",intent1.getStringExtra("time"));
                intent.putExtra("name",intent1.getStringExtra("name"));
                intent.putExtra("city",intent1.getStringExtra("city"));
                intent.putExtra("country",intent1.getStringExtra("country"));
                intent.putExtra("lat",intent1.getStringExtra("lat"));
                intent.putExtra("lon",intent1.getStringExtra("lon"));
                intent.putExtra("tz",intent1.getStringExtra("tz"));
                intent.putExtra("id",intent1.getStringExtra("id"));
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        findViewById(R.id.cardView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate_horoscope_planetary_positions.this,Generate_horoscope_panchangam.class);
                Intent intent1 = getIntent();
                intent.putExtra("date",intent1.getStringExtra("date"));
                intent.putExtra("time",intent1.getStringExtra("time"));
                intent.putExtra("name",intent1.getStringExtra("name"));
                intent.putExtra("city",intent1.getStringExtra("city"));
                intent.putExtra("country",intent1.getStringExtra("country"));
                intent.putExtra("lat",intent1.getStringExtra("lat"));
                intent.putExtra("lon",intent1.getStringExtra("lon"));
                intent.putExtra("tz",intent1.getStringExtra("tz"));
                intent.putExtra("id",intent1.getStringExtra("id"));
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        findViewById(R.id.cardView6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate_horoscope_planetary_positions.this,Generate_charts.class);
                Intent intent1 = getIntent();
                intent.putExtra("date",intent1.getStringExtra("date"));
                intent.putExtra("time",intent1.getStringExtra("time"));
                intent.putExtra("name",intent1.getStringExtra("name"));
                intent.putExtra("city",intent1.getStringExtra("city"));
                intent.putExtra("country",intent1.getStringExtra("country"));
                intent.putExtra("lat",intent1.getStringExtra("lat"));
                intent.putExtra("lon",intent1.getStringExtra("lon"));
                intent.putExtra("tz",intent1.getStringExtra("tz"));
                intent.putExtra("id",intent1.getStringExtra("id"));
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        findViewById(R.id.cardView7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate_horoscope_planetary_positions.this,Generate_horoscope_planetary_positions.class);
                Intent intent1 = getIntent();
                intent.putExtra("date",intent1.getStringExtra("date"));
                intent.putExtra("time",intent1.getStringExtra("time"));
                intent.putExtra("name",intent1.getStringExtra("name"));
                intent.putExtra("city",intent1.getStringExtra("city"));
                intent.putExtra("country",intent1.getStringExtra("country"));
                intent.putExtra("lat",intent1.getStringExtra("lat"));
                intent.putExtra("lon",intent1.getStringExtra("lon"));
                intent.putExtra("tz",intent1.getStringExtra("tz"));
                intent.putExtra("id",intent1.getStringExtra("id"));
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        findViewById(R.id.cardView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate_horoscope_planetary_positions.this,Generate_horoscope_Dasa_Bukti.class);
                Intent intent1 = getIntent();
                intent.putExtra("date",intent1.getStringExtra("date"));
                intent.putExtra("time",intent1.getStringExtra("time"));
                intent.putExtra("name",intent1.getStringExtra("name"));
                intent.putExtra("city",intent1.getStringExtra("city"));
                intent.putExtra("country",intent1.getStringExtra("country"));
                intent.putExtra("lat",intent1.getStringExtra("lat"));
                intent.putExtra("lon",intent1.getStringExtra("lon"));
                intent.putExtra("tz",intent1.getStringExtra("tz"));
                intent.putExtra("id",intent1.getStringExtra("id"));
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        findViewById(R.id.cardView9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate_horoscope_planetary_positions.this,Generate_special_report_Main_Menu.class);
                Intent intent1 = getIntent();
                intent.putExtra("date",intent1.getStringExtra("date"));
                intent.putExtra("time",intent1.getStringExtra("time"));
                intent.putExtra("name",intent1.getStringExtra("name"));
                intent.putExtra("city",intent1.getStringExtra("city"));
                intent.putExtra("country",intent1.getStringExtra("country"));
                intent.putExtra("lat",intent1.getStringExtra("lat"));
                intent.putExtra("lon",intent1.getStringExtra("lon"));
                intent.putExtra("tz",intent1.getStringExtra("tz"));
                intent.putExtra("id",intent1.getStringExtra("id"));
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public class backgroundworker2 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                sb2=new StringBuffer();
                Log.d("ASASASASS",""+json_url2);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
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
            Log.d("RESULT", "" + result);
            int count=0;
            try {
                JSONArray jsonArray = new JSONArray(json_string2);
                while(count < jsonArray.length()){
                    JSONObject jsonObject = jsonArray.getJSONObject(count);
                    planet_norm_degree.add(jsonObject.getString("normDegree").substring(0,5));
                    planet_rasis.add(zodiac_signs_map.get(jsonObject.getString("sign")));
                    planet_naks.add(naksathara_map.get(jsonObject.getString("nakshatra"))+"-"+jsonObject.getString("nakshatra_pad"));
                    planet_house.add(jsonObject.getString("house"));
                    count++;
                    if(count>9){
                        break;
                    }
                }

                assign();


            }catch (Exception e){

            }
        }
    }

    public void assign(){
        textView72.setText(planet_rasis.get(9));
        textViews72.setText(planet_rasis.get(0));
        textViewmo72.setText(planet_rasis.get(1));
        textViewma72.setText(planet_rasis.get(2));
        textViewme72.setText(planet_rasis.get(3));
        textViewju72.setText(planet_rasis.get(4));
        textViewve72.setText(planet_rasis.get(5));
        textViewsa72.setText(planet_rasis.get(6));
        textViewra72.setText(planet_rasis.get(7));
        textViewke72.setText(planet_rasis.get(8));

        textView73.setText(planet_norm_degree.get(9));
        textViews73.setText(planet_norm_degree.get(0));
        textViewmo73.setText(planet_norm_degree.get(1));
        textViewma73.setText(planet_norm_degree.get(2));
        textViewme73.setText(planet_norm_degree.get(3));
        textViewju73.setText(planet_norm_degree.get(4));
        textViewve73.setText(planet_norm_degree.get(5));
        textViewsa73.setText(planet_norm_degree.get(6));
        textViewra73.setText(planet_norm_degree.get(7));
        textViewke73.setText(planet_norm_degree.get(8));

        textView74.setText(planet_naks.get(9));
        textViews74.setText(planet_naks.get(0));
        textViewmo74.setText(planet_naks.get(1));
        textViewma74.setText(planet_naks.get(2));
        textViewme74.setText(planet_naks.get(3));
        textViewju74.setText(planet_naks.get(4));
        textViewve74.setText(planet_naks.get(5));
        textViewsa74.setText(planet_naks.get(6));
        textViewra74.setText(planet_naks.get(7));
        textViewke74.setText(planet_naks.get(8));

        textView75.setText(planet_house.get(9));
        textViews75.setText(planet_house.get(0));
        textView76.setText(planet_house.get(1));
        textView77.setText(planet_house.get(2));
        textView78.setText(planet_house.get(3));
        textView788.setText(planet_house.get(4));
        textView7888.setText(planet_house.get(5));
        textView78888.setText(planet_house.get(6));
        textView788888.setText(planet_house.get(7));
        textView7558888.setText(planet_house.get(8));
    }

    public void intialise(){

        progressDialog = new ProgressDialog(Generate_horoscope_planetary_positions.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        intent = getIntent();

        naksathara_map.put("Ashwini", "அஸ்வினி");
        naksathara_map.put("Bharni", "பரணி");
        naksathara_map.put("Krittika", "கிருத்திகை");
        naksathara_map.put("Rohini", "ரோகிணி");
        naksathara_map.put("Mrigshira", "மிருகசீரிஷம்");
        naksathara_map.put("Ardra", "திருவாதிரை");
        naksathara_map.put("Punarvasu", "புனர்பூசம்");
        naksathara_map.put("Pushya", "பூசம்");
        naksathara_map.put("Ashlesha", "ஆயில்யம்");
        naksathara_map.put("Magha", "மகம்");
        naksathara_map.put("Purva Phalguni", "பூரம்");
        naksathara_map.put("Uttra Phalguni", "உத்திரம்");
        naksathara_map.put("Hast", "அஸ்தம்");
        naksathara_map.put("Chitra", "சித்திரை");
        naksathara_map.put("Swati", "சுவாதி");
        naksathara_map.put("Vishakha", "விசாகம்");
        naksathara_map.put("Anuradha", "அனுஷம்");
        naksathara_map.put("Jyeshtha", "கேட்டை");
        naksathara_map.put("Mool", "மூலம்");
        naksathara_map.put("Purva Shadha", "பூராடம்");
        naksathara_map.put("Uttra Shadha", "உத்திராடம்");
        naksathara_map.put("Shravan", "திருவோணம்");
        naksathara_map.put("Dhanishtha", "அவிட்டம்");
        naksathara_map.put("Shatbhisha", "சதயம்");
        naksathara_map.put("Purva Bhadrapad", "பூரட்டாதி");
        naksathara_map.put("Uttra Bhadrapad", "உத்திரட்டாதி");
        naksathara_map.put("Revati", "ரேவதி");

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

        textView72 = findViewById(R.id.textView72);
        textViews72 = findViewById(R.id.textViews72);
        textViewmo72 = findViewById(R.id.textViewmo72);
        textViewma72 = findViewById(R.id.textViewma72);
        textViewme72 = findViewById(R.id.textViewme72);
        textViewju72 = findViewById(R.id.textViewju72);
        textViewve72 = findViewById(R.id.textViewve72);
        textViewsa72 = findViewById(R.id.textViewsa72);
        textViewra72 = findViewById(R.id.textViewra72);
        textViewke72 = findViewById(R.id.textViewke72);

        textView73 = findViewById(R.id.textView73);
        textViews73 = findViewById(R.id.textViews73);
        textViewmo73 = findViewById(R.id.textViewmo73);
        textViewma73 = findViewById(R.id.textViewma73);
        textViewme73 = findViewById(R.id.textViewme73);
        textViewju73 = findViewById(R.id.textViewju73);
        textViewve73 = findViewById(R.id.textViewve73);
        textViewsa73 = findViewById(R.id.textViewsa73);
        textViewra73 = findViewById(R.id.textViewra73);
        textViewke73 = findViewById(R.id.textViewke73);

        textView74 = findViewById(R.id.textView74);
        textViews74 = findViewById(R.id.textViews74);
        textViewmo74 = findViewById(R.id.textViewmo74);
        textViewma74 = findViewById(R.id.textViewma74);
        textViewme74 = findViewById(R.id.textViewme74);
        textViewju74 = findViewById(R.id.textViewju74);
        textViewve74 = findViewById(R.id.textViewve74);
        textViewsa74 = findViewById(R.id.textViewsa74);
        textViewra74 = findViewById(R.id.textViewra74);
        textViewke74 = findViewById(R.id.textViewke74);

        textView75 = findViewById(R.id.textView75);
        textViews75 = findViewById(R.id.textViews75);
        textView76 = findViewById(R.id.textView76);
        textView77 = findViewById(R.id.textView77);
        textView78 = findViewById(R.id.textView78);
        textView788 = findViewById(R.id.textView788);
        textView7888 = findViewById(R.id.textView7888);
        textView78888 = findViewById(R.id.textView78888);
        textView788888 = findViewById(R.id.textView788888);
        textView7558888 = findViewById(R.id.textView7558888);
    }
}