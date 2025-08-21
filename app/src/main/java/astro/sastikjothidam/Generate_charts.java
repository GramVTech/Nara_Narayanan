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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generate_charts extends AppCompatActivity {

    List<String> types_of_chart = new ArrayList<>();
    Spinner s_types_of_chart;
    Map<String, String> house_values = new HashMap<String, String>();
    Map<String, String> house_button_values = new HashMap<String, String>();
    String h1="",h2="",h3="",h4="",h5="",h6="",h7="",h8="",h9="",h10="",h11="",h12="";
    HashMap<String,String> planetary_short_name = new HashMap<>();
    List<String> zodiac_signs = new ArrayList<>();
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12;
    ProgressDialog progressDialog;
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = "http://astrochinnaraj.net/1_astro_software/rasi_chart_api.php";
    String json_string2 = "";
    Intent intent;
    TextView tsun,tmoon,tmars,tmercury,tjupiter,tvenus,tsaturn,trahu,tketu,tasc;
    String temp="";
    List<String> planet_norm_degree = new ArrayList<>();

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
        setContentView(R.layout.activity_generate_charts);
        intialise();

        findViewById(R.id.cardView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate_charts.this,Generate_horoscope_birth_details.class);
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
                Intent intent = new Intent(Generate_charts.this,Generate_horoscope_panchangam.class);
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
                Intent intent = new Intent(Generate_charts.this,Generate_charts.class);
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
                Intent intent = new Intent(Generate_charts.this,Generate_horoscope_planetary_positions.class);
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
                Intent intent = new Intent(Generate_charts.this,Generate_horoscope_Dasa_Bukti.class);
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
                Intent intent = new Intent(Generate_charts.this,Generate_special_report_Main_Menu.class);
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


        s_types_of_chart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                temp = s_types_of_chart.getSelectedItem().toString();

                b1.setText("");b2.setText("");b3.setText("");b4.setText("");b5.setText("");b6.setText("");
                b7.setText("");b8.setText("");b9.setText("");b10.setText("");b11.setText("");b12.setText("");
                h1="";h2="";h3="";h4="";h5="";h6="";h7="";h8="";h9="";h10="";h11="";h12="";

                if(temp.equals("D1 Chart")){
                    json_url2 = "http://astrochinnaraj.net/1_astro_software/rasi_chart_api.php";
                    progressDialog.show();
                    new backgroundworker2().execute();
                }else if(temp.equals("D6 Chart")){
                    json_url2 = "http://astrochinnaraj.net/1_astro_software/D6_rasi_chart_api.php";
                    progressDialog.show();
                    new backgroundworker2().execute();
                }else if(temp.equals("D9 Chart")){
                    json_url2 = "http://astrochinnaraj.net/1_astro_software/D9_rasi_chart_api.php";
                    progressDialog.show();
                    new backgroundworker2().execute();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
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
                    if(temp.equals("D1 Chart")&&(count<=9)) {
                        planet_norm_degree.add(jsonObject.getString("normDegree").substring(0, 4));
                    }
                    String temp_house = jsonObject.getString("house");
                    Log.d("ASASASS",""+temp_house+"\n");
                    if(temp_house.equals("1")){
                        h1 = h1+planetary_short_name.get(jsonObject.getString("name"))+" ";
                    }else if(temp_house.equals("2")){
                        h2 = h2+planetary_short_name.get(jsonObject.getString("name"))+" ";
                    }else if(temp_house.equals("3")){
                        h3 = h3+planetary_short_name.get(jsonObject.getString("name"))+" ";
                    }else if(temp_house.equals("4")){
                        h4 = h4+planetary_short_name.get(jsonObject.getString("name"))+" ";
                    }else if(temp_house.equals("5")){
                        h5 = h5+planetary_short_name.get(jsonObject.getString("name"))+" ";
                    }else if(temp_house.equals("6")){
                        h6 = h6+planetary_short_name.get(jsonObject.getString("name"))+" ";
                    }else if(temp_house.equals("7")){
                        h7 = h7+planetary_short_name.get(jsonObject.getString("name"))+" ";
                    }else if(temp_house.equals("8")){
                        h8 = h8+planetary_short_name.get(jsonObject.getString("name"))+" ";
                    }else if(temp_house.equals("9")){
                        h9 = h9+planetary_short_name.get(jsonObject.getString("name"))+" ";
                    }else if(temp_house.equals("10")){
                        h10 = h10+planetary_short_name.get(jsonObject.getString("name"))+" ";
                    }else if(temp_house.equals("11")){
                        h11 = h11+planetary_short_name.get(jsonObject.getString("name"))+" ";
                    }else if(temp_house.equals("12")){
                        h12 = h12+planetary_short_name.get(jsonObject.getString("name"))+" ";
                    }
                    count++;
                }
                house_values.put("h1",h1);
                house_values.put("h2",h2);
                house_values.put("h3",h3);
                house_values.put("h4",h4);
                house_values.put("h5",h5);
                house_values.put("h6",h6);
                house_values.put("h7",h7);
                house_values.put("h8",h8);
                house_values.put("h9",h9);
                house_values.put("h10",h10);
                house_values.put("h11",h11);
                house_values.put("h12",h12);
                fun_align();
            }catch (Exception e){

            }
        }
    }

    void fun_align(){
        String start_house="";
        int count =0 ;
        try {
            JSONArray jsonArray = new JSONArray(json_string2);
            JSONObject jsonObject1 = jsonArray.getJSONObject(9);
            start_house = String.valueOf(zodiac_signs.indexOf(jsonObject1.getString("sign")));
            while (count < jsonArray.length()) {
                JSONObject jsonObject = jsonArray.getJSONObject(count);
                String temp_house = "h"+jsonObject.getString("house");
                String graha_values = house_values.get(temp_house);
                String temp = house_button_values.get(temp_house);
                String split_temp = temp.substring(6,temp.length());
                int total = 0;
                total = Integer.parseInt(start_house.trim()) + Integer.parseInt(split_temp.trim());
                Log.e("What Happens", String.valueOf(total));
                if(total>12){
                    total = (total-12);
                }
                String final_temp = "button"+String.valueOf(total);
                int resourceId = getResources().getIdentifier(final_temp, "id", getPackageName());
                Button button = findViewById(resourceId);
                button.setText(graha_values);
                count++;
            }

            if(temp.equals("D1 Chart")) {
                tsun.setText("சூரி : "+planet_norm_degree.get(0));
                tmoon.setText("சந் : "+planet_norm_degree.get(1));
                tmars.setText("செ : "+planet_norm_degree.get(2));
                tmercury.setText("புத : "+planet_norm_degree.get(3));
                tjupiter.setText("குரு : "+planet_norm_degree.get(4));
                tvenus.setText("சுக் : "+planet_norm_degree.get(5));
                tsaturn.setText("சனி : "+planet_norm_degree.get(6));
                trahu.setText("ரா : "+planet_norm_degree.get(7));
                tketu.setText("கே : "+planet_norm_degree.get(8));
                tasc.setText("லக் : "+planet_norm_degree.get(9));
            }


        }catch (Exception e){

        }


    }

    public void intialise(){
        types_of_chart.add("D1 Chart");
        types_of_chart.add("D6 Chart");
        types_of_chart.add("D9 Chart");
        s_types_of_chart =findViewById(R.id.chart);
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(Generate_charts.this, R.layout.custom_spinner_layout5, types_of_chart);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_types_of_chart.setAdapter(dataAdapter4);

        planetary_short_name.put("Sun","சூரி");
        planetary_short_name.put("Moon","சந்");
        planetary_short_name.put("Mars","செ");
        planetary_short_name.put("Mercury","புத");
        planetary_short_name.put("Jupiter","குரு");
        planetary_short_name.put("Venus","சுக்");
        planetary_short_name.put("Saturn","சனி");
        planetary_short_name.put("Ascendant","லக்");
        planetary_short_name.put("Rahu","ரா");
        planetary_short_name.put("Ketu","கே");
        planetary_short_name.put("Mandhi","மாந்");

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

        b1= findViewById(R.id.button1);
        b2= findViewById(R.id.button2);
        b3= findViewById(R.id.button3);
        b4= findViewById(R.id.button4);
        b5= findViewById(R.id.button5);
        b6= findViewById(R.id.button6);
        b7= findViewById(R.id.button7);
        b8= findViewById(R.id.button8);
        b9= findViewById(R.id.button9);
        b10= findViewById(R.id.button10);
        b11= findViewById(R.id.button11);
        b12= findViewById(R.id.button12);

        house_button_values.put("h1","button1");
        house_button_values.put("h2","button2");
        house_button_values.put("h3","button3");
        house_button_values.put("h4","button4");
        house_button_values.put("h5","button5");
        house_button_values.put("h6","button6");
        house_button_values.put("h7","button7");
        house_button_values.put("h8","button8");
        house_button_values.put("h9","button9");
        house_button_values.put("h10","button10");
        house_button_values.put("h11","button11");
        house_button_values.put("h12","button12");

        progressDialog = new ProgressDialog(Generate_charts.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        intent = getIntent();

        tsun = findViewById(R.id.textView52);
        tmoon = findViewById(R.id.textView54);
        tmars = findViewById(R.id.textView56);
        tmercury = findViewById(R.id.textView58);
        tjupiter = findViewById(R.id.textView60);
        tvenus = findViewById(R.id.textView53);
        tsaturn = findViewById(R.id.textView55);
        trahu = findViewById(R.id.textView57);
        tketu = findViewById(R.id.textView59);
        tasc = findViewById(R.id.textView61);



    }
}