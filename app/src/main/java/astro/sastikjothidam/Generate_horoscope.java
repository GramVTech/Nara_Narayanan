package astro.sastikjothidam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Generate_horoscope extends AppCompatActivity {

    String city;
    EditText ecity,Ecountry,Elat,Elon,Etz,Edob,Etime,Ename;
    String sname="",scity="",scountry="",slat="",slon="",stz="",sdob="",stime="";

    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"Cloud_Horoscope_insert.php";
    String json_string="";
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    List<String> Address_List = new ArrayList<>();

    StringBuffer sb3 = new StringBuffer();
    String json_url3 = "https://astrochinnaraj.net/1_astro_software/tester.php";
    String json_string3="";

    String dst="";

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
        setContentView(R.layout.activity_generate_horoscope);
        intialise();

        Edob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        Etime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        findViewById(R.id.google_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate_horoscope.this,Generate_horoscope_Open.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address_List.clear();
                city = ecity.getText().toString().trim();
                Geocoder geocoder = new Geocoder(Generate_horoscope.this, Locale.getDefault());
                try {
                    List<Address> ads = geocoder.getFromLocationName(city,100);
                    Log.e("ASASA",""+ads);
                    int count=0;String temp="";
                    while(count<ads.size()){
                        Address address = ads.get(count);
                        temp = temp + address.getLatitude()+"@"+address.getLongitude()+"@"+address.getCountryName()+"\n";
                        Address_List.add(temp);
                        temp = "";
                        count++;
                    }
                    Log.e("ASASASASSAS",""+temp);

                    Elat.setText(Address_List.get(0).toString().split("@")[0].toString());
                    Elon.setText(Address_List.get(0).toString().split("@")[1].toString());
                    Ecountry.setText(Address_List.get(0).toString().split("@")[2].toString());

                    String timezone = SearchTimeZone(Address_List.get(0).toString().split("@")[2].toString().toUpperCase().trim());

                    Etz.setText(timezone);

                } catch (Exception e){
                    sdob = Edob.getText().toString();
                    progressDialog.show();
                    new backgroundworker3().execute();
                }
            }
        });



        findViewById(R.id.cardView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slat = Elat.getText().toString().trim();
                slon = Elon.getText().toString().trim();
                stz = Etz.getText().toString().trim();

                if(slat.length()>0&&slon.length()>0&&stz.length()>0) {
                    progressDialog.show();
                    new backgroundworker().execute();
                    sname = Ename.getText().toString().trim();
                    stime = Etime.getText().toString();
                    sdob = Edob.getText().toString();
                    scity = ecity.getText().toString().trim();
                    scountry = Ecountry.getText().toString().trim();
                }else{
                    Toast.makeText(Generate_horoscope.this, "Enter All Details And Click the Search Button After You Entering the Place", Toast.LENGTH_SHORT).show();
                }

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
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mail","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("name","UTF-8")+"="+ URLEncoder.encode(sname,"UTF-8")+"&"
                        +URLEncoder.encode("dob","UTF-8")+"="+ URLEncoder.encode(sdob,"UTF-8")+"&"
                        +URLEncoder.encode("tob","UTF-8")+"="+ URLEncoder.encode(stime,"UTF-8")+"&"
                        +URLEncoder.encode("pob","UTF-8")+"="+ URLEncoder.encode(scity,"UTF-8")+"&"
                        +URLEncoder.encode("country","UTF-8")+"="+ URLEncoder.encode(scountry,"UTF-8")+"&"
                        +URLEncoder.encode("lat","UTF-8")+"="+ URLEncoder.encode(slat,"UTF-8")+"&"
                        +URLEncoder.encode("lon","UTF-8")+"="+ URLEncoder.encode(slon,"UTF-8")+"&"
                        +URLEncoder.encode("tz","UTF-8")+"="+ URLEncoder.encode(stz,"UTF-8")+"&"
                        +URLEncoder.encode("dst","UTF-8")+"="+ URLEncoder.encode(dst,"UTF-8");
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
                if(result.equals("User Already Exists")){
                    Toast.makeText(Generate_horoscope.this, "User Already Exists Try with Some Other Name", Toast.LENGTH_SHORT).show();
                }else if(result.equals("Contact Admin")){
                    Toast.makeText(Generate_horoscope.this, "Contact Admin", Toast.LENGTH_SHORT).show();
                }else if(result.equals("YES")){
                    Intent intent = new Intent(Generate_horoscope.this,Generate_horoscope_main_menu.class);
                    intent.putExtra("time",Etime.getText().toString());
                    intent.putExtra("date",Edob.getText().toString());
                    intent.putExtra("name",Ename.getText().toString().trim());
                    intent.putExtra("city",ecity.getText().toString().trim());
                    intent.putExtra("country",Ecountry.getText().toString().trim());
                    intent.putExtra("lat",Elat.getText().toString().trim());
                    intent.putExtra("lon",Elon.getText().toString().trim());
                    intent.putExtra("tz",Etz.getText().toString().trim());
                    intent.putExtra("dst",dst);
                    startActivity(intent);
                }else{
                    Toast.makeText(Generate_horoscope.this, "Contact Admin", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(Generate_horoscope.this, "Contact Admin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        Edob.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        // Get current time
        final Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHourOfDay, selectedMinute) -> {
                    // Set selected time to the EditText
                    String selectedTime = String.format("%02d:%02d", selectedHourOfDay, selectedMinute);
                    Etime.setText(selectedTime);
                },
                hourOfDay,
                minute,
                true // 24-hour format
        );

        // Show the time picker dialog
        timePickerDialog.show();
    }

    public void intialise(){

        ecity = findViewById(R.id.editTextTextplace);
        Ecountry = findViewById(R.id.country);
        Elat = findViewById(R.id.lat);
        Elon = findViewById(R.id.lon);
        Etz = findViewById(R.id.tz);
        Edob = findViewById(R.id.editTextTextdob);
        Etime = findViewById(R.id.editTextTexttime);
        Ename = findViewById(R.id.editTextText);

        progressDialog = new ProgressDialog(Generate_horoscope.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(Generate_horoscope.this);

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = Generate_horoscope.this.getAssets().open("timezone.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String SearchTimeZone(String country){
        String timezone = "";
        try{
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            JSONArray jsonArray = jsonObject.getJSONArray("countries");
            int counter=0;
            while(counter<jsonArray.length()){
                JSONObject jsonObject1 = jsonArray.getJSONObject(counter);
                if(jsonObject1.getString("name").toString().toUpperCase().equals(country)){
                    timezone = jsonObject1.getString("timezone_offset").toString();
                }
                counter++;
            }
        }catch (Exception e){

        }
        return timezone;
    }

    public class backgroundworker3 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url3);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {

                sb3=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("city","UTF-8")+"="+URLEncoder.encode(city.trim(),"UTF-8")+"&"
                        +URLEncoder.encode("date","UTF-8")+"="+ URLEncoder.encode(sdob,"UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string3=bufferedReader.readLine())!=null)
                {
                    sb3.append(json_string3+"\n");
                    Log.d("json_string3",""+json_string3);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG3",""+sb3.toString());
                return sb3.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string3 = result;
            progressDialog.dismiss();
            int count=0;
            Log.d("RESULT", "" + result);
            try{
                JSONObject jsonObject = new JSONObject(json_string3);
                Elat.setText(jsonObject.getString("lat").toString());
                Elon.setText(jsonObject.getString("lon").toString());
                Ecountry.setText(jsonObject.getString("country").toString());

                dst = jsonObject.getString("daylight_saving").toString();

                String timezone = SearchTimeZone(jsonObject.getString("country").toString().toUpperCase().trim());

                Etz.setText(timezone);

            }catch (Exception e){
                Toast.makeText(Generate_horoscope.this, "Contact Admin", Toast.LENGTH_SHORT).show();
            }


        }
    }
}