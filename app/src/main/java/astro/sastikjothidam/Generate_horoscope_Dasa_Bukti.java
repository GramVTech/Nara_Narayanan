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
import android.widget.ImageView;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Generate_horoscope_Dasa_Bukti extends AppCompatActivity {

    ProgressDialog progressDialog;
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = "http://astrochinnaraj.net/1_astro_software/Basic_Panchangam.php";
    String json_string2 = "";
    Intent intent;
    String moon_degree = "",moon_naksthara="",moon_span_degree="",naksthara_lord="";
    Map<String, String> nakshatraDegreeSpans = new HashMap<>();
    Map<String, Integer> Dasa_map = new HashMap<>();
    double span_degree_1,span_degree_2,span_degree_3;
    double remain_degree;
    double remaining_years,remaining_years_total;
    int lord_span=0;
    int r_years,r_months,r_days;
    String adjusted_date = "";

    TextView txt81,txt82,txt83,txt84,txt85,txt86,txt87,txt88,txt89,
            txt91,txt92,txt93,txt94,txt95,txt96,txt97,txt98,txt99,
            txt71,txt72,txt73,txt74,txt75,txt76,txt77,txt78,txt79;

    ImageView img24,img25,img26,img27,img28,img29,img30,img31,img32;

    ArrayList<String> grahas = new ArrayList<>();
    ArrayList<TextView> grahas_txt = new ArrayList<>();
    ArrayList<TextView> grahas_SD_txt = new ArrayList<>();
    ArrayList<TextView> grahas_ED_txt = new ArrayList<>();
    ArrayList<ImageView> grahas_IM = new ArrayList<>();

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
        setContentView(R.layout.activity_generate_horoscope_dasa_bukti);

        intailise();

        progressDialog.show();
        new backgroundworker2().execute();

        findViewById(R.id.cardView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_horoscope_birth_details.class);
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
                Intent intent = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_horoscope_panchangam.class);
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
                Intent intent = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_charts.class);
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
                Intent intent = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_horoscope_planetary_positions.class);
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
                Intent intent = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_horoscope_Dasa_Bukti.class);
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
                Intent intent = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_special_report_Main_Menu.class);
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

        img24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_Horoscope_Bukti.class);
                intent1.putExtra("Graha",String.valueOf(img24.getTag()));
                intent1.putExtra("to_date",txt71.getText().toString());
                int span = Dasa_map.get(String.valueOf(img24.getTag()));
                String first_bukti_date = min_years(txt71.getText().toString(),span);
                intent1.putExtra("from_date",first_bukti_date.split("-")[2]+"-"+first_bukti_date.split("-")[1]+"-"+first_bukti_date.split("-")[0]);
                startActivity(intent1);
            }
        });

        img25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_Horoscope_Bukti.class);
                intent1.putExtra("Graha",String.valueOf(img25.getTag()));
                intent1.putExtra("from_date",txt92.getText().toString());
                intent1.putExtra("to_date",txt72.getText().toString());
                startActivity(intent1);
            }
        });

        img26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_Horoscope_Bukti.class);
                intent1.putExtra("Graha",String.valueOf(img26.getTag()));
                intent1.putExtra("from_date",txt93.getText().toString());
                intent1.putExtra("to_date",txt73.getText().toString());
                startActivity(intent1);
            }
        });

        img27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_Horoscope_Bukti.class);
                intent1.putExtra("Graha",String.valueOf(img27.getTag()));
                intent1.putExtra("from_date",txt94.getText().toString());
                intent1.putExtra("to_date",txt74.getText().toString());
                startActivity(intent1);
            }
        });

        img28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_Horoscope_Bukti.class);
                intent1.putExtra("Graha",String.valueOf(img28.getTag()));
                intent1.putExtra("from_date",txt95.getText().toString());
                intent1.putExtra("to_date",txt75.getText().toString());
                startActivity(intent1);
            }
        });

        img29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_Horoscope_Bukti.class);
                intent1.putExtra("Graha",String.valueOf(img29.getTag()));
                intent1.putExtra("from_date",txt96.getText().toString());
                intent1.putExtra("to_date",txt76.getText().toString());
                startActivity(intent1);
            }
        });

        img30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_Horoscope_Bukti.class);
                intent1.putExtra("Graha",String.valueOf(img30.getTag()));
                intent1.putExtra("from_date",txt97.getText().toString());
                intent1.putExtra("to_date",txt77.getText().toString());
                startActivity(intent1);
            }
        });

        img31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_Horoscope_Bukti.class);
                intent1.putExtra("Graha",String.valueOf(img31.getTag()));
                intent1.putExtra("from_date",txt98.getText().toString());
                intent1.putExtra("to_date",txt78.getText().toString());
                startActivity(intent1);
            }
        });

        img32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Generate_horoscope_Dasa_Bukti.this,Generate_Horoscope_Bukti.class);
                intent1.putExtra("Graha",String.valueOf(img32.getTag()));
                intent1.putExtra("from_date",txt99.getText().toString());
                intent1.putExtra("to_date",txt79.getText().toString());
                startActivity(intent1);
            }
        });
    }

    public String min_years(String temp,int span){
        try {
            String[] dater = temp.split("-");
            LocalDate initialDate = null;
            LocalDate newDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int years = Integer.valueOf(dater[2]);
                int months = Integer.valueOf(dater[1]);
                int days = Integer.valueOf(dater[0]);
                initialDate = LocalDate.of(years, months, days);
                long year_to_minus = (long) span;
                newDate = initialDate.minusYears(year_to_minus);
                return String.valueOf(newDate);
            }
        }catch (Exception e){

        }
        return "";
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
                JSONArray jsonArray2 = new JSONArray(jsonArray.get(1).toString());
                JSONObject jsonObject123 = jsonArray2.getJSONObject(1);
                moon_degree = jsonObject123.getString("normDegree");
                moon_naksthara = jsonObject123.getString("nakshatra");
                moon_span_degree = nakshatraDegreeSpans.get(moon_naksthara);
                span_degree_1 = Double.parseDouble(moon_degree);
                span_degree_2 = convertToDecimal(moon_span_degree.split(" ")[0]);
                span_degree_3 = convertToDecimal(moon_span_degree.split(" ")[2]);
                naksthara_lord = jsonObject123.getString("nakshatraLord");
                lord_span = Dasa_map.get(naksthara_lord);
                if(span_degree_1<span_degree_2){
                     remain_degree = span_degree_3-span_degree_1;
                     remaining_years = remain_degree/13.33;
                     remaining_years_total = lord_span * remaining_years;
                     degree_to_Y_M_D(remaining_years_total);
                     adj_date();
                     fun();
                     fun2();
                }else if(span_degree_1>span_degree_2){
                    double temp = span_degree_1-span_degree_2;
                    remain_degree = 13.33 - temp;
                    remaining_years = remain_degree/13.33;
                    remaining_years_total = lord_span * remaining_years;
                    degree_to_Y_M_D(remaining_years_total);
                    adj_date();
                    fun();
                    fun2();
                }
                //Toast.makeText(Generate_horoscope_Dasa_Bukti.this, ""+moon_degree, Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }
        }
    }

    public void fun2(){
        int start = grahas.indexOf(naksthara_lord)+1;
        String from_date = adjusted_date;
        String to_date = "";
        String start_naks = "";
        for(int i=0;i<grahas_txt.size();i++){

            if(start>8)
                start = 0;

            TextView gtx = grahas_txt.get(i);
            gtx.setText(grahas.get(start));

            start_naks = grahas.get(start);

            TextView sd = grahas_SD_txt.get(i);
            sd.setText(from_date.split("-")[2]+"-"+from_date.split("-")[1]+"-"+from_date.split("-")[0]);

            String r_to_date = adj_date2(from_date,start_naks);
            TextView ed = grahas_ED_txt.get(i);
            ed.setText(r_to_date.split("-")[2]+"-"+r_to_date.split("-")[1]+"-"+r_to_date.split("-")[0]);

            ImageView img = grahas_IM.get(i);
            img.setTag(grahas.get(start));

            from_date = r_to_date;
            start = start + 1;

        }
    }

    public void fun(){
        txt81.setText(naksthara_lord);
        String temp1 = reverse_date(adjusted_date);
        txt91.setText(intent.getStringExtra("date"));
        txt71.setText(temp1);
        img24.setTag(naksthara_lord);
    }

    public String reverse_date(String str){
        String[] splitter = str.split("-");
        return splitter[2]+"-"+splitter[1]+"-"+splitter[0];
    }

    public double convertToDecimal(String degreeMinuteInput) {
        String[] parts = degreeMinuteInput.split("[°']");
        int degrees = Integer.parseInt(parts[0].trim());
        int minutes = Integer.parseInt(parts[1].trim());
        return degrees + (minutes / 60.0);
    }

    public void degree_to_Y_M_D(double inputYears){
        int years = (int) inputYears;
        double fractionalPart = inputYears - years;
        int months = (int) (fractionalPart * 12);
        double remainingMonthsFraction = (fractionalPart * 12) - months;
        int days = (int) (remainingMonthsFraction * 30.44);
        r_years = years;
        r_months = months;
        r_days = days;
    }

    public void adj_date(){
        String[] dater = intent.getStringExtra("date").split("-");
        LocalDate birthDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            birthDate = LocalDate.of(Integer.valueOf(dater[2]), Integer.valueOf(dater[1]), Integer.valueOf(dater[0]));
            LocalDate adjustedDate = birthDate.plusYears(r_years)
                    .plusMonths(r_months)
                    .plusDays(r_days);
            adjusted_date = String.valueOf(adjustedDate);
        }
    }

    public String adj_date2(String to_date,String naksthara_lord){
        String[] dater = to_date.split("-");
        LocalDate birthDate = null;
        LocalDate adjustedDate = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int years = Integer.valueOf(dater[0]);
                int months = Integer.valueOf(dater[1]);
                int days = Integer.valueOf(dater[2]);
                birthDate = LocalDate.of(years, months, days);
                int span_years = Dasa_map.get(naksthara_lord);
                adjustedDate = birthDate.plusYears(span_years)
                        .plusMonths(0)
                        .plusDays(0);
            }
            return String.valueOf(adjustedDate);
        }catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
        return "";
    }



    public void intailise(){

        progressDialog = new ProgressDialog(Generate_horoscope_Dasa_Bukti.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        intent = getIntent();

        nakshatraDegreeSpans.put("Ashwini", "0°0' to 13°20' Aries");
        nakshatraDegreeSpans.put("Bharni", "13°20' to 26°40' Aries");
        nakshatraDegreeSpans.put("Krittika", "26°40' to 10°0' Taurus");
        nakshatraDegreeSpans.put("Rohini", "10°0' to 23°20' Taurus");
        nakshatraDegreeSpans.put("Mrigshira", "23°20' to 6°40' Gemini");
        nakshatraDegreeSpans.put("Ardra", "6°40' to 20°0' Gemini");
        nakshatraDegreeSpans.put("Punarvasu", "20°0' to 3°20' Cancer");
        nakshatraDegreeSpans.put("Pushya", "3°20' to 16°40' Cancer");
        nakshatraDegreeSpans.put("Ashlesha", "16°40' to 30°0' Cancer");
        nakshatraDegreeSpans.put("Magha", "0°0' to 13°20' Leo");
        nakshatraDegreeSpans.put("Purva Phalguni", "13°20' to 26°40' Leo");
        nakshatraDegreeSpans.put("Uttra Phalguni", "26°40' to 10°0' Virgo");
        nakshatraDegreeSpans.put("Hast", "10°0' to 23°20' Virgo");
        nakshatraDegreeSpans.put("Chitra", "23°20' to 6°40' Libra");
        nakshatraDegreeSpans.put("Swati", "6°40' to 20°0' Libra");
        nakshatraDegreeSpans.put("Vishakha", "20°0' to 3°20' Scorpio");
        nakshatraDegreeSpans.put("Anuradha", "3°20' to 16°40' Scorpio");
        nakshatraDegreeSpans.put("Jyeshtha", "16°40' to 30°0' Scorpio");
        nakshatraDegreeSpans.put("Mool", "0°0' to 13°20' Sagittarius");
        nakshatraDegreeSpans.put("Purva Shadha", "13°20' to 26°40' Sagittarius");
        nakshatraDegreeSpans.put("Uttra Shadha", "26°40' to 10°0' Capricorn");
        nakshatraDegreeSpans.put("Shravan", "10°0' to 23°20' Capricorn");
        nakshatraDegreeSpans.put("Dhanishtha", "23°20' to 6°40' Aquarius");
        nakshatraDegreeSpans.put("Shatbhisha", "6°40' to 20°0' Aquarius");
        nakshatraDegreeSpans.put("Purva Bhadrapad", "20°0' to 3°20' Pisces");
        nakshatraDegreeSpans.put("Uttra Bhadrapad", "3°20' to 16°40' Pisces");
        nakshatraDegreeSpans.put("Revati", "16°40' to 30°0' Pisces");

        Dasa_map.put("Sun", 6);
        Dasa_map.put("Moon", 10);
        Dasa_map.put("Mars", 7);
        Dasa_map.put("Mercury", 17);
        Dasa_map.put("Jupiter", 16);
        Dasa_map.put("Venus", 20);
        Dasa_map.put("Saturn", 19);
        Dasa_map.put("Rahu", 18);
        Dasa_map.put("Ketu", 7);

        txt81 = findViewById(R.id.textView81);
        txt82 = findViewById(R.id.textView82);
        txt83 = findViewById(R.id.textView83);
        txt84 = findViewById(R.id.textView84);
        txt85 = findViewById(R.id.textView85);
        txt86 = findViewById(R.id.textView86);
        txt87 = findViewById(R.id.textView87);
        txt88 = findViewById(R.id.textView88);
        txt89 = findViewById(R.id.textView89);

        txt91 = findViewById(R.id.textView91);
        txt92 = findViewById(R.id.textView92);
        txt93 = findViewById(R.id.textView93);
        txt94 = findViewById(R.id.textView94);
        txt95 = findViewById(R.id.textView95);
        txt96 = findViewById(R.id.textView96);
        txt97 = findViewById(R.id.textView97);
        txt98 = findViewById(R.id.textView98);
        txt99 = findViewById(R.id.textView99);

        txt71 = findViewById(R.id.textView71);
        txt72 = findViewById(R.id.textView72);
        txt73 = findViewById(R.id.textView73);
        txt74 = findViewById(R.id.textView74);
        txt75 = findViewById(R.id.textView75);
        txt76 = findViewById(R.id.textView76);
        txt77 = findViewById(R.id.textView77);
        txt78 = findViewById(R.id.textView78);
        txt79 = findViewById(R.id.textView79);

        img24 = findViewById(R.id.imageView24);
        img25 = findViewById(R.id.imageView25);
        img26 = findViewById(R.id.imageView26);
        img27 = findViewById(R.id.imageView27);
        img28 = findViewById(R.id.imageView28);
        img29 = findViewById(R.id.imageView29);
        img30 = findViewById(R.id.imageView30);
        img31 = findViewById(R.id.imageView31);
        img32 = findViewById(R.id.imageView32);

        grahas.add("Sun");
        grahas.add("Moon");
        grahas.add("Mars");
        grahas.add("Rahu");
        grahas.add("Jupiter");
        grahas.add("Saturn");
        grahas.add("Mercury");
        grahas.add("Ketu");
        grahas.add("Venus");

        grahas_txt.add(txt82);
        grahas_txt.add(txt83);
        grahas_txt.add(txt84);
        grahas_txt.add(txt85);
        grahas_txt.add(txt86);
        grahas_txt.add(txt87);
        grahas_txt.add(txt88);
        grahas_txt.add(txt89);

        grahas_SD_txt.add(txt92);
        grahas_SD_txt.add(txt93);
        grahas_SD_txt.add(txt94);
        grahas_SD_txt.add(txt95);
        grahas_SD_txt.add(txt96);
        grahas_SD_txt.add(txt97);
        grahas_SD_txt.add(txt98);
        grahas_SD_txt.add(txt99);

        grahas_ED_txt.add(txt72);
        grahas_ED_txt.add(txt73);
        grahas_ED_txt.add(txt74);
        grahas_ED_txt.add(txt75);
        grahas_ED_txt.add(txt76);
        grahas_ED_txt.add(txt77);
        grahas_ED_txt.add(txt78);
        grahas_ED_txt.add(txt79);

        grahas_IM.add(img25);
        grahas_IM.add(img26);
        grahas_IM.add(img27);
        grahas_IM.add(img28);
        grahas_IM.add(img29);
        grahas_IM.add(img30);
        grahas_IM.add(img31);
        grahas_IM.add(img32);

    }
}