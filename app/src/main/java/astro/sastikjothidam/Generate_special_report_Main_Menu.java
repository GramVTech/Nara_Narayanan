package astro.sastikjothidam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import java.util.HashMap;
import java.util.Map;

public class Generate_special_report_Main_Menu extends AppCompatActivity {

    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"Horo_member_Active_check.php";
    String json_string3="";
    String temporary = "";
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"Any_Horo_Plan_Active_Check.php";
    String json_string="";

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
        setContentView(R.layout.activity_generate_special_report_main_menu);

        intialise();

        findViewById(R.id.general_remedies).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                new backgroundworker().execute();
            }
        });

        findViewById(R.id.horoscope).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temporary = "Member";
                json_url3 = Url_interface.url+"Horo_member_Active_check.php";
                progressDialog.show();
                new backgroundworker3().execute();
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temporary = "Student";
                json_url3 = Url_interface.url+"Horo_Student_Active_check.php";
                progressDialog.show();
                new backgroundworker3().execute();
            }
        });

        findViewById(R.id.cardView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Generate_special_report_Main_Menu.this,Generate_horoscope_birth_details.class);
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
                Intent intent = new Intent(Generate_special_report_Main_Menu.this,Generate_horoscope_panchangam.class);
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
                Intent intent = new Intent(Generate_special_report_Main_Menu.this,Generate_charts.class);
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
                Intent intent = new Intent(Generate_special_report_Main_Menu.this,Generate_horoscope_planetary_positions.class);
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
                Intent intent = new Intent(Generate_special_report_Main_Menu.this,Generate_horoscope_Dasa_Bukti.class);
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

    public void intialise(){

        progressDialog = new ProgressDialog(Generate_special_report_Main_Menu.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(Generate_special_report_Main_Menu.this);

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
                String post_data = URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8");
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
                if(result.equals("Success")){
                    Intent intent = new Intent(Generate_special_report_Main_Menu.this,Generate_horoscope_general_remedies.class);
                    startActivity(intent);
                }else if(result.equals("Failure")){
                    Toast.makeText(Generate_special_report_Main_Menu.this, "Purchase Any One Of Memeber Plan", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                Toast.makeText(Generate_special_report_Main_Menu.this, "Contact Admin", Toast.LENGTH_SHORT).show();
            }
        }
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
                String post_data = URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8");
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
                if(result.equals("Failure")){
                    Intent intent1 = getIntent();
                    Intent intent = new Intent(Generate_special_report_Main_Menu.this, Special_Report_Plans.class);
                    intent.putExtra("type",temporary);
                    intent.putExtra("date",intent1.getStringExtra("date"));
                    intent.putExtra("time",intent1.getStringExtra("time"));
                    intent.putExtra("name",intent1.getStringExtra("name"));
                    intent.putExtra("city",intent1.getStringExtra("city"));
                    intent.putExtra("country",intent1.getStringExtra("country"));
                    intent.putExtra("lat",intent1.getStringExtra("lat"));
                    intent.putExtra("lon",intent1.getStringExtra("lon"));
                    intent.putExtra("tz",intent1.getStringExtra("tz"));
                    intent.putExtra("id",intent1.getStringExtra("id"));
                    startActivity(intent);
                }else if(result.equals("Success")){
                    Intent intent2 = getIntent();
                    Intent intent1 = new Intent(Generate_special_report_Main_Menu.this, WebView_Report.class);
                    intent1.putExtra("type",temporary);
                    intent1.putExtra("date",intent2.getStringExtra("date"));
                    intent1.putExtra("time",intent2.getStringExtra("time"));
                    intent1.putExtra("name",intent2.getStringExtra("name"));
                    intent1.putExtra("city",intent2.getStringExtra("city"));
                    intent1.putExtra("country",intent2.getStringExtra("country"));
                    intent1.putExtra("lat",intent2.getStringExtra("lat"));
                    intent1.putExtra("lon",intent2.getStringExtra("lon"));
                    intent1.putExtra("tz",intent2.getStringExtra("tz"));
                    intent1.putExtra("id",intent2.getStringExtra("id"));
                    startActivity(intent1);
                }
            }catch (Exception e){

            }


        }
    }

}