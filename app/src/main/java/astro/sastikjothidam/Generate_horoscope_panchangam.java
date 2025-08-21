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
import android.widget.Toast;

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

public class Generate_horoscope_panchangam extends AppCompatActivity {

    HashMap<String, String> yogas = new HashMap<>();
    HashMap<String, String> daysOfWeek = new HashMap<>();
    HashMap<String, String> tithis = new HashMap<>();
    Map<String, String> naksathara_map = new HashMap<>();
    Map<String, String> naksathara_rasi_map = new HashMap<>();
    HashMap<String, String> karanams = new HashMap<>();
    HashMap<String, String> karanams_lords = new HashMap<>();
    HashMap<String, String> nakshatraLords = new HashMap<>();
    HashMap<String, String> planetaryFullName = new HashMap<>();
    HashMap<String, String> rev_house_name = new HashMap<>();
    List<String> zodiac_signs = new ArrayList<>();
    HashMap<String, String> tithisToRashis = new HashMap<>();
    HashMap<String, String> rasiLordsTamil = new HashMap<>();
    HashMap<String, String> yogas_yogi_ava_yogi = new HashMap<>();
    HashMap<String, String> valar_or_thei = new HashMap<>();
    List<String> nakshatras_list = new ArrayList<>();

    ProgressDialog progressDialog;
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = "http://astrochinnaraj.net/1_astro_software/Basic_Panchangam.php";
    String json_string2 = "";
    Intent intent;

    String sday="",sthithi="",snaksthara="",syogam="",skaranam="";
    TextView textView60,textView62,textView64,textView66,textView68,textView70,
            textView72,textView74,textView76,textView78,textView80,textView80m,textView80s,textView,
    textView80sva,textView80ind,textView80lag,textView80vag,textView80varko,textView80puk,
    textView80gp,textView80sp;

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
        setContentView(R.layout.activity_generate_horoscope_panchangam);
        intailise();
        textView.setText("PANCHANGAM - ("+intent.getStringExtra("date")+" : "+intent.getStringExtra("time")+")");
        progressDialog.show();
        new backgroundworker2().execute();

        findViewById(R.id.cardView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate_horoscope_panchangam.this,Generate_horoscope_birth_details.class);
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
                Intent intent = new Intent(Generate_horoscope_panchangam.this,Generate_horoscope_panchangam.class);
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
                Intent intent = new Intent(Generate_horoscope_panchangam.this,Generate_charts.class);
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
                Intent intent = new Intent(Generate_horoscope_panchangam.this,Generate_horoscope_planetary_positions.class);
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
                Intent intent = new Intent(Generate_horoscope_panchangam.this,Generate_horoscope_Dasa_Bukti.class);
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
                Intent intent = new Intent(Generate_horoscope_panchangam.this,Generate_special_report_Main_Menu.class);
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
                JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
                JSONObject jsonObject1234 = new JSONObject(jsonArray.get(2).toString());
                sday = jsonObject1234.getString("day");
                sthithi = jsonObject.getString("Tithi");
                snaksthara = jsonObject.getString("Naksahtra");
                syogam = jsonObject.getString("Yog");
                skaranam = jsonObject.getString("Karan");

                textView80sva.setText(jsonObject1234.getString("vainasikam"));
                textView80ind.setText(jsonObject1234.getString("indu"));
                textView80lag.setText(jsonObject1234.getString("lagna_puli"));
                textView80vag.setText(jsonObject1234.getString("vakaram"));
                textView80varko.setText(jsonObject1234.getString("vargo"));
                textView80puk.setText(jsonObject1234.getString("puskar"));
                textView80gp.setText(jsonObject1234.getString("gp"));
                textView80sp.setText(jsonObject1234.getString("sp"));


                JSONArray jsonArray2 = new JSONArray(jsonArray.get(1).toString());
                JSONObject jsonObject2 = jsonArray2.getJSONObject(0);
                JSONObject jsonObject123 = jsonArray2.getJSONObject(1);


                textView62.setText(naksathara_map.get(jsonObject123.getString("nakshatra")));
                String naks = jsonObject2.getString("nakshatra");
                //Toast.makeText(Generate_horoscope_panchangam.this, ""+naks, Toast.LENGTH_SHORT).show();
                int start_index = nakshatras_list.indexOf(naks)+1;
                int counter = 0;
                if(start_index<19){
                    counter = (19-start_index)+1;
                }else{
                    counter = (27-start_index)+1+19;
                }
                int to_pooradam_counter = 0;
                to_pooradam_counter = counter+20;
                if(to_pooradam_counter>27){
                    to_pooradam_counter = (to_pooradam_counter-27)-1;
                    if(to_pooradam_counter==0){
                        to_pooradam_counter = 26;
                    }else {
                        to_pooradam_counter = to_pooradam_counter - 1;
                    }

                }else{
                    to_pooradam_counter = to_pooradam_counter-2;
                }

                JSONObject jsonObject3 = jsonArray2.getJSONObject(9);
                String sign1 = jsonObject3.getString("sign");
                //Toast.makeText(Generate_horoscope_panchangam.this, ""+sign1, Toast.LENGTH_SHORT).show();
                int idx1 = zodiac_signs.indexOf(sign1)+1;
                textView80.setText(naksathara_map.get(String.valueOf(nakshatras_list.get(to_pooradam_counter))));
                String to_poor_naks = nakshatras_list.get(to_pooradam_counter);
                textView80s.setText(rasiLordsTamil.get(naksathara_rasi_map.get(to_poor_naks)));
                //Toast.makeText(Generate_horoscope_panchangam.this, ""+rev_house_name.get(naksathara_rasi_map.get(to_poor_naks)), Toast.LENGTH_SHORT).show();
                int idx2 = zodiac_signs.indexOf(rev_house_name.get(naksathara_rasi_map.get(to_poor_naks)))+1;
                int fin = 0;
                if(idx1>idx2){
                    fin = (zodiac_signs.size()-idx1)+idx2+1;
                }else if(idx1<idx2){
                    fin = (idx2-idx1)+1;
                }else if(idx1 == idx2){
                    fin = 1;
                }
                textView80m.setText(String.valueOf(fin));
                fun();
            }catch (Exception e){

            }
        }
    }

    public void fun(){
        textView60.setText(daysOfWeek.get(sday));
//        textView62.setText(naksathara_map.get(snaksthara));
        String stemp = "";
        try{
            int cm = sthithi.trim().split(" ").length;
            if(sthithi.trim().split(" ").length>1){
                stemp = tithis.get(sthithi.split(" ")[1].trim());
                textView64.setText(stemp+" ("+valar_or_thei.get(sthithi.split(" ")[0].trim())+")");
                String stemp1 = tithisToRashis.get(stemp);
                textView66.setText(stemp1);
                if(stemp1.split(",").length>2){
                    String stemp3 = rasiLordsTamil.get(stemp1.split(",")[0].trim());
                    String stemp4 = rasiLordsTamil.get(stemp1.split(",")[3].trim());
                    textView68.setText(stemp3+","+stemp4);
                }else{
                    String stemp3 = rasiLordsTamil.get(stemp1.split(",")[0].trim());
                    String stemp4 = rasiLordsTamil.get(stemp1.split(",")[1].trim());
                    textView68.setText(stemp3+","+stemp4);
                }
            }else if(sthithi.trim().split("-").length>1){
                stemp = tithis.get(sthithi.split("-")[1].trim());
                textView64.setText(stemp+" ("+valar_or_thei.get(sthithi.split("-")[0].trim())+")");
                String stemp1 = tithisToRashis.get(stemp);
                textView66.setText(stemp1);
                if(stemp1.split(",").length>2){
                    String stemp3 = rasiLordsTamil.get(stemp1.split(",")[0].trim());
                    String stemp4 = rasiLordsTamil.get(stemp1.split(",")[3].trim());
                    textView68.setText(stemp3+","+stemp4);
                }else{
                    String stemp3 = rasiLordsTamil.get(stemp1.split(",")[0].trim());
                    String stemp4 = rasiLordsTamil.get(stemp1.split(",")[1].trim());
                    textView68.setText(stemp3+","+stemp4);
                }
            } else{
                stemp = tithis.get(sthithi.trim());
                textView64.setText(stemp);
                textView66.setText("இல்லை");
                textView68.setText("இல்லை");
            }
        }catch (Exception e){

        }
        String stemp5 = yogas.get(syogam);
        textView70.setText(stemp5);
        String stemp7 = yogas_yogi_ava_yogi.get(syogam);
        String stemp8 = nakshatraLords.get(stemp7.split(",")[0].trim());
        String stemp9 = nakshatraLords.get(stemp7.split(",")[1].trim());
        textView72.setText(planetaryFullName.get(stemp8)+","+planetaryFullName.get(stemp9));
        String stemp10 = naksathara_map.get(stemp7.split(",")[0].trim());
        String stemp11 = naksathara_map.get(stemp7.split(",")[1].trim());
        textView74.setText(stemp10+","+stemp11);
        textView76.setText(karanams.get(skaranam));
        textView78.setText(karanams_lords.get(skaranam));

    }

    public void intailise(){


        yogas.put("Vishkumbha", "விஷ்கம்பம்");
        yogas.put("Priti", "ப்ரீத்தி");
        yogas.put("Ayushman", "ஆயிஷ்மான்");
        yogas.put("Saubhgya", "செளபாக்கியம்");
        yogas.put("Shobhan", "சோபனம்");
        yogas.put("Atigand", "அதிகண்டம்");
        yogas.put("Sukarma", "சுகர்மம்");
        yogas.put("Dhriti", "திருதி");
        yogas.put("Shool", "சூலம்");
        yogas.put("Dand", "கண்டம்");
        yogas.put("Vriddhi", "விருத்தி");
        yogas.put("Dhruv", "துருவம்");
        yogas.put("Vyaghat", "வியாகதம்");
        yogas.put("Harshan", "ஹர்சனம்");
        yogas.put("Vajra", "வஜ்ரம்");
        yogas.put("Siddhi", "சித்தி");
        yogas.put("Vyatipaat", "வியாதிபாதம்");
        yogas.put("Variyaan", "வரியான்");
        yogas.put("Parigh", "பரிகம்");
        yogas.put("Shiv", "சிவம்");
        yogas.put("Siddh", "சித்தம்");
        yogas.put("Sadhya", "சாத்யம்");
        yogas.put("Shubh", "சுபம்");
        yogas.put("Shukla", "சுப்பிரம்");
        yogas.put("Brahma", "பிராம்யம்");
        yogas.put("Endra", "ஐந்திரம்");
        yogas.put("Vaidhriti", "வைதிருதி");

        yogas_yogi_ava_yogi.put("Vishkumbha", "Pushya,Shravan");
        yogas_yogi_ava_yogi.put("Priti", "Ashlesha,Dhanishtha");
        yogas_yogi_ava_yogi.put("Ayushman", "Magha,Shatbhisha");
        yogas_yogi_ava_yogi.put("Saubhgya", "Purva Phalguni,Purva Bhadrapad");
        yogas_yogi_ava_yogi.put("Shobhan", "Uttra Phalguni,Uttra Bhadrapad");
        yogas_yogi_ava_yogi.put("Atigand", "Hast,Revati");
        yogas_yogi_ava_yogi.put("Sukarma", "Chitra,Ashwini");
        yogas_yogi_ava_yogi.put("Dhriti", "Swati,Bharni");
        yogas_yogi_ava_yogi.put("Shool", "Vishakha,Krittika");
        yogas_yogi_ava_yogi.put("Dand", "Anuradha,Rohini");
        yogas_yogi_ava_yogi.put("Vriddhi", "Jyeshtha,Mrigshira");
        yogas_yogi_ava_yogi.put("Dhruv", "Mool,Ardra");
        yogas_yogi_ava_yogi.put("Vyaghat", "Purva Shadha,Punarvasu");
        yogas_yogi_ava_yogi.put("Harshan", "Uttra Shadha,Pushya");
        yogas_yogi_ava_yogi.put("Vajra", "Shravan,Ashlesha");
        yogas_yogi_ava_yogi.put("Siddhi", "Dhanishtha,Magha");
        yogas_yogi_ava_yogi.put("Vyatipaat", "Shatbhisha,Purva Phalguni");
        yogas_yogi_ava_yogi.put("Variyaan", "Purva Bhadrapad,Uttra Phalguni");
        yogas_yogi_ava_yogi.put("Parigh", "Uttra Bhadrapad,Hast");
        yogas_yogi_ava_yogi.put("Shiv", "Revati,Chitra");
        yogas_yogi_ava_yogi.put("Siddh", "Ashwini,Swati");
        yogas_yogi_ava_yogi.put("Sadhya", "Bharni,Vishakha");
        yogas_yogi_ava_yogi.put("Shubh", "Krittika,Anuradha");
        yogas_yogi_ava_yogi.put("Shukla", "Rohini,Jyeshtha");
        yogas_yogi_ava_yogi.put("Brahma", "Mrigshira,Mool");
        yogas_yogi_ava_yogi.put("Endra", "Ardra,Purva Shadha");
        yogas_yogi_ava_yogi.put("Vaidhriti", "Punarvasu,Uttra Shadha");

        daysOfWeek.put("Sunday", "ஞாயிறு");
        daysOfWeek.put("Monday", "திங்கள்");
        daysOfWeek.put("Tuesday", "செவ்வாய்");
        daysOfWeek.put("Wednesday", "புதன்");
        daysOfWeek.put("Thursday", "வியாழன்");
        daysOfWeek.put("Friday", "வெள்ளி");
        daysOfWeek.put("Saturday", "சனி");

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

        naksathara_rasi_map.put("Ashwini", "மேஷம்");
        naksathara_rasi_map.put("Bharni", "மேஷம்");
        naksathara_rasi_map.put("Krittika", "மேஷம்");
        naksathara_rasi_map.put("Rohini", "ரிஷபம்");
        naksathara_rasi_map.put("Mrigshira", "ரிஷபம்");
        naksathara_rasi_map.put("Ardra", "மிதுனம்");
        naksathara_rasi_map.put("Punarvasu", "மிதுனம்");
        naksathara_rasi_map.put("Pushya", "கடகம்");
        naksathara_rasi_map.put("Ashlesha", "கடகம்");
        naksathara_rasi_map.put("Magha", "சிம்மம்");
        naksathara_rasi_map.put("Purva Phalguni", "சிம்மம்");
        naksathara_rasi_map.put("Uttra Phalguni", "சிம்மம்");
        naksathara_rasi_map.put("Hast", "கன்னி");
        naksathara_rasi_map.put("Chitra", "கன்னி");
        naksathara_rasi_map.put("Swati", "துலாம்");
        naksathara_rasi_map.put("Vishakha", "துலாம்");
        naksathara_rasi_map.put("Anuradha", "விருச்சிகம்");
        naksathara_rasi_map.put("Jyeshtha", "விருச்சிகம்");
        naksathara_rasi_map.put("Mool", "தனுசு");
        naksathara_rasi_map.put("Purva Shadha", "தனுசு");
        naksathara_rasi_map.put("Uttra Shadha", "தனுசு");
        naksathara_rasi_map.put("Shravan", "மகரம்");
        naksathara_rasi_map.put("Dhanishtha", "மகரம்");
        naksathara_rasi_map.put("Shatbhisha", "கும்பம்");
        naksathara_rasi_map.put("Purva Bhadrapad", "கும்பம்");
        naksathara_rasi_map.put("Uttra Bhadrapad", "மீனம்");
        naksathara_rasi_map.put("Revati", "மீனம்");

        karanams.put("Bava", "பவம்");
        karanams.put("Baalav", "பாலவம்");
        karanams.put("Kaulav", "கெளலவம்");
        karanams.put("Taitil", "தைதுலம்");
        karanams.put("Gara", "கரசை");
        karanams.put("Vanija", "வணிசை");
        karanams.put("Vishti", "பத்திரை");
        karanams.put("Shakuni", "சகுனி");
        karanams.put("Chatushpad", "சதுஸ்பாதம்");
        karanams.put("Naag", "நாகவம்");
        karanams.put("Kinstughna", "கிம்ஸ்துக்கினம்");

        karanams_lords.put("Bava", "செவ்வாய்");
        karanams_lords.put("Baalav", "ராகு");
        karanams_lords.put("Kaulav", "சனி");
        karanams_lords.put("Taitil", "சுக்கிரன்");
        karanams_lords.put("Gara", "சந்திரன்");
        karanams_lords.put("Vanija", "சூரியன்");
        karanams_lords.put("Shakuni", "சனி");
        karanams_lords.put("Vishti", "கேது");
        karanams_lords.put("Chatushpad", "குரு");
        karanams_lords.put("Naag", "ராகு");
        karanams_lords.put("Kinstughna", "புதன்");

        nakshatraLords.put("Ashwini", "Ketu");
        nakshatraLords.put("Bharni", "Venus");
        nakshatraLords.put("Krittika", "Sun");
        nakshatraLords.put("Rohini", "Moon");
        nakshatraLords.put("Mrigshira", "Mars");
        nakshatraLords.put("Ardra", "Rahu");
        nakshatraLords.put("Punarvasu", "Jupiter");
        nakshatraLords.put("Pushya", "Saturn");
        nakshatraLords.put("Ashlesha", "Mercury");
        nakshatraLords.put("Magha", "Ketu");
        nakshatraLords.put("Purva Phalguni", "Venus");
        nakshatraLords.put("Uttra Phalguni", "Sun");
        nakshatraLords.put("Hast", "Moon");
        nakshatraLords.put("Chitra", "Mars");
        nakshatraLords.put("Swati", "Rahu");
        nakshatraLords.put("Vishakha", "Jupiter");
        nakshatraLords.put("Anuradha", "Saturn");
        nakshatraLords.put("Jyeshtha", "Mercury");
        nakshatraLords.put("Mool", "Ketu");
        nakshatraLords.put("Purva Shadha", "Venus");
        nakshatraLords.put("Uttra Shadha", "Sun");
        nakshatraLords.put("Shravan", "Moon");
        nakshatraLords.put("Dhanishtha", "Mars");
        nakshatraLords.put("Shatbhisha", "Rahu");
        nakshatraLords.put("Purva Bhadrapad", "Jupiter");
        nakshatraLords.put("Uttra Bhadrapad", "Saturn");
        nakshatraLords.put("Revati", "Mercury");

        planetaryFullName.put("Sun", "சூரியன்");
        planetaryFullName.put("Moon", "சந்திரன்");
        planetaryFullName.put("Mars", "செவ்வாய்");
        planetaryFullName.put("Mercury", "புதன்");
        planetaryFullName.put("Jupiter", "குரு");
        planetaryFullName.put("Venus", "சுக்கிரன்");
        planetaryFullName.put("Saturn", "சனி");
        planetaryFullName.put("Rahu", "ராகு");
        planetaryFullName.put("Ketu", "கேது");
        planetaryFullName.put("Ascendant", "லக்கினம்");

        rev_house_name.put("மேஷம்", "Aries");
        rev_house_name.put("ரிஷபம்", "Taurus");
        rev_house_name.put("மிதுனம்", "Gemini");
        rev_house_name.put("கடகம்", "Cancer");
        rev_house_name.put("சிம்மம்", "Leo");
        rev_house_name.put("கன்னி", "Virgo");
        rev_house_name.put("துலாம்", "Libra");
        rev_house_name.put("விருச்சிகம்", "Scorpio");
        rev_house_name.put("தனுசு", "Sagittarius");
        rev_house_name.put("மகரம்", "Capricorn");
        rev_house_name.put("கும்பம்", "Aquarius");
        rev_house_name.put("மீனம்", "Pisces");

        zodiac_signs.add("Aries");
        zodiac_signs.add("Taurus");
        zodiac_signs.add("Gemini");
        zodiac_signs.add("Cancer");
        zodiac_signs.add("Leo");
        zodiac_signs.add("Virgo");
        zodiac_signs.add("Libra");
        zodiac_signs.add("Scorpio");
        zodiac_signs.add("Sagittarius");
        zodiac_signs.add("Capricorn");
        zodiac_signs.add("Aquarius");
        zodiac_signs.add("Pisces");

        tithis.put("Pratipada", "பிரதமை");
        tithis.put("Dwitiya", "துவிதியை");
        tithis.put("Tritiya", "திருதியை");
        tithis.put("Chaturthi", "சதுர்த்தி");
        tithis.put("Panchami", "பஞ்சமி");
        tithis.put("Shashthi", "ஷஷ்டி");
        tithis.put("Saptami", "சப்தமி");
        tithis.put("Ashtami", "அஷ்டமி");
        tithis.put("Navami", "நவமி");
        tithis.put("Dashami", "தசமி");
        tithis.put("Ekadashi", "ஏகாதசி");
        tithis.put("Dwadashi", "துவாதசி");
        tithis.put("Trayodashi", "திரயோதசி");
        tithis.put("Chaturdashi", "சதுர்த்தசி");
        tithis.put("Purnima", "பௌர்ணமி");
        tithis.put("Amavasya", "அமாவாசை");

        tithisToRashis.put("பிரதமை", "துலாம், மகரம்");
        tithisToRashis.put("துவிதியை", "தனுசு, மீனம்");
        tithisToRashis.put("திருதியை", "மகரம், சிம்மம்");
        tithisToRashis.put("சதுர்த்தி", "கும்பம், ரிஷபம்");
        tithisToRashis.put("பஞ்சமி", "மிதுனம், கன்னி");
        tithisToRashis.put("ஷஷ்டி", "மேஷம், சிம்மம்");
        tithisToRashis.put("சப்தமி", "தனுசு, கடகம்");
        tithisToRashis.put("அஷ்டமி", "மிதுனம், கன்னி");
        tithisToRashis.put("நவமி", "சிம்மம், விருச்சிகம்");
        tithisToRashis.put("தசமி", "சிம்மம், விருச்சிகம்");
        tithisToRashis.put("ஏகாதசி", "தனுசு, மீனம்");
        tithisToRashis.put("துவாதசி", "துலாம், மகரம்");
        tithisToRashis.put("திரயோதசி", "ரிஷபம், சிம்மம்");
        tithisToRashis.put("சதுர்த்தசி", "மிதுனம், கன்னி, தனுசு, மீனம்");


        rasiLordsTamil.put("மேஷம்", "செவ்வாய்");
        rasiLordsTamil.put("ரிஷபம்", "சுக்கிரன்");
        rasiLordsTamil.put("மிதுனம்", "புதன்");
        rasiLordsTamil.put("கடகம்", "சந்திரன்");
        rasiLordsTamil.put("சிம்மம்", "சூரியன்");
        rasiLordsTamil.put("கன்னி", "புதன்");
        rasiLordsTamil.put("துலாம்", "சுக்கிரன்");
        rasiLordsTamil.put("விருச்சிகம்", "செவ்வாய்");
        rasiLordsTamil.put("தனுசு", "குரு");
        rasiLordsTamil.put("மகரம்", "சனி");
        rasiLordsTamil.put("கும்பம்", "சனி");
        rasiLordsTamil.put("மீனம்", "குரு");

        valar_or_thei.put("Krishna","தேய் பிறை");
        valar_or_thei.put("Shukla","வளர் பிறை");

        progressDialog = new ProgressDialog(Generate_horoscope_panchangam.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        intent = getIntent();

        textView60 = findViewById(R.id.textView60);
        textView62 = findViewById(R.id.textView62);
        textView64 = findViewById(R.id.textView64);
        textView66 = findViewById(R.id.textView66);
        textView68 = findViewById(R.id.textView68);
        textView70 = findViewById(R.id.textView70);
        textView72 = findViewById(R.id.textView72);
        textView74 = findViewById(R.id.textView74);
        textView76 = findViewById(R.id.textView76);
        textView78 = findViewById(R.id.textView78);
        textView80 = findViewById(R.id.textView80);
        textView80m = findViewById(R.id.textView80m);
        textView80s = findViewById(R.id.textView80s);

        textView80sva = findViewById(R.id.textView80sva);
        textView80ind = findViewById(R.id.textView80ind);
        textView80lag = findViewById(R.id.textView80lag);
        textView80vag = findViewById(R.id.textView80vag);
        textView80varko = findViewById(R.id.textView80varko);
        textView80puk = findViewById(R.id.textView80puk);

        textView80gp = findViewById(R.id.textView80gp);
        textView80sp = findViewById(R.id.textView80sp);

        nakshatras_list.add("Ashwini");
        nakshatras_list.add("Bharni");
        nakshatras_list.add("Krittika");
        nakshatras_list.add("Rohini");
        nakshatras_list.add("Mrigshira");
        nakshatras_list.add("Ardra");
        nakshatras_list.add("Punarvasu");
        nakshatras_list.add("Pushya");
        nakshatras_list.add("Ashlesha");
        nakshatras_list.add("Magha");
        nakshatras_list.add("Purva Phalguni");
        nakshatras_list.add("Uttra Phalguni");
        nakshatras_list.add("Hast");
        nakshatras_list.add("Chitra");
        nakshatras_list.add("Swati");
        nakshatras_list.add("Vishakha");
        nakshatras_list.add("Anuradha");
        nakshatras_list.add("Jyeshtha");
        nakshatras_list.add("Mool");
        nakshatras_list.add("Purva Shadha");
        nakshatras_list.add("Uttra Shadha");
        nakshatras_list.add("Shravan");
        nakshatras_list.add("Dhanishtha");
        nakshatras_list.add("Shatbhisha");
        nakshatras_list.add("Purva Bhadrapad");
        nakshatras_list.add("Uttra Bhadrapad");
        nakshatras_list.add("Revati");

        textView = findViewById(R.id.textView);
    }
}