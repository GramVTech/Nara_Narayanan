package astro.sastikjothidam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

public class Consultation_Slot extends AppCompatActivity {

    EditText date_picker;
    List<String> time_Slot = new ArrayList<String>();
    GridView listView;
    CustomAdapter customAdapter;
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    String selected_date = "";
    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"time_slot_availability.php";
    String json_string="";
    List<String> Lslot_status = new ArrayList<String>();
    List<CardView> Ltemp_list = new ArrayList<>();
    List<TextView> Ltemp_list2 = new ArrayList<>();

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
        setContentView(R.layout.activity_consultation_slot);
        intialise();
        setCurrentDate();
        setTimeSlotArray();
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        selected_date = date_picker.getText().toString();
        progressDialog.show();
        new backgroundworker().execute();

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    TextView temper = Ltemp_list2.get(0);
                    String date = temper.getText().toString().split("--")[0].trim();
                    String time = temper.getText().toString().split("--")[1].trim();
                    Intent intent = new Intent(Consultation_Slot.this, Consultation_Summary_Page.class);
                    intent.putExtra("dater", date);
                    intent.putExtra("timer", time);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(Consultation_Slot.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                        date_picker.setText(selectedDate);

                        time_Slot.clear();
                        Ltemp_list.clear();
                        Ltemp_list2.clear();
                        setTimeSlotArray();

                        selected_date = selectedDate;
                        progressDialog.show();
                        new backgroundworker().execute();
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    public void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        date_picker.setText(currentDate);
    }
    public void setTimeSlotArray(){

        String today_date = date_picker.getText().toString();

        time_Slot.add(today_date+" -- "+"09:00 A.M TO 09:30 A.M");
        time_Slot.add(today_date+" -- "+"09:30 A.M TO 10:00 A.M");
        time_Slot.add(today_date+" -- "+"10:00 A.M TO 10:30 A.M");
        time_Slot.add(today_date+" -- "+"10:30 A.M TO 11:00 A.M");
        time_Slot.add(today_date+" -- "+"11:00 A.M TO 11:30 A.M");
        time_Slot.add(today_date+" -- "+"11:30 A.M TO 12:00 A.M");
        time_Slot.add(today_date+" -- "+"12:00 A.M TO 12:30 P.M");
        time_Slot.add(today_date+" -- "+"12:30 P.M TO 01:00 P.M");
        time_Slot.add(today_date+" -- "+"01:00 P.M TO 01:30 P.M");
        time_Slot.add(today_date+" -- "+"01:30 P.M TO 02:00 P.M");
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
                String post_data = URLEncoder.encode("selected_date","UTF-8")+"="+URLEncoder.encode(selected_date,"UTF-8")+"&"
                        +URLEncoder.encode("mail","UTF-8")+"="+ URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8");
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
                    String[] slotsArray = jsonObject1.getString("slot_status").substring(0,jsonObject1.getString("slot_status").length()-1).split(",");
                    Lslot_status = Arrays.asList(slotsArray);
                    count++;
                }
                if(Lslot_status.size()>0){
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e){

            }


        }
    }
    public class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return time_Slot.size();
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

            view = getLayoutInflater().inflate(R.layout.custom_layout_consultation_slot, null);

            TextView time_info = view.findViewById(R.id.textView10);

            CardView cardView = view.findViewById(R.id.horoscope);

            if(Lslot_status.get(i).equals("0")){
                time_info.setText(time_Slot.get(i));
                time_info.setTextColor(ContextCompat.getColor(Consultation_Slot.this, R.color.dark_green));
                cardView.setBackgroundColor(ContextCompat.getColor(Consultation_Slot.this, R.color.white));
            }else if(Lslot_status.get(i).equals("1")){
                time_info.setText(time_Slot.get(i));
                time_info.setTextColor(ContextCompat.getColor(Consultation_Slot.this, R.color.white));
                cardView.setBackgroundColor(ContextCompat.getColor(Consultation_Slot.this, R.color.red));
            }


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Ltemp_list.size()>0){
                        for(int j=0;j<Ltemp_list.size();j++){
                            CardView cardView1 = Ltemp_list.get(j);
                            TextView textView = Ltemp_list2.get(j);
                            textView.setTextColor(ContextCompat.getColor(Consultation_Slot.this, R.color.dark_green));
                            cardView1.setBackgroundColor(ContextCompat.getColor(Consultation_Slot.this, R.color.white));
                        }
                    }
                    if(Lslot_status.get(i).equals("1")) {
                        Toast.makeText(Consultation_Slot.this, "Slot is Already Booked", Toast.LENGTH_SHORT).show();
                    }else{
                        time_info.setTextColor(ContextCompat.getColor(Consultation_Slot.this, R.color.white));
                        cardView.setBackgroundColor(ContextCompat.getColor(Consultation_Slot.this, R.color.green));
                        Ltemp_list.add(cardView);
                        Ltemp_list2.add(time_info);
                    }

                }
            });

            return view;
        }
    }
    public void intialise(){

        date_picker = findViewById(R.id.editTextText6);

        sessionMaintance = new SessionMaintance(Consultation_Slot.this);

        customAdapter = new CustomAdapter();

        listView = findViewById(R.id.listView);

        progressDialog = new ProgressDialog(Consultation_Slot.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);
    }
}