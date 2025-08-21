package astro.sastikjothidam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Consultation_Summary_Page extends AppCompatActivity implements PaymentResultWithDataListener {

    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"consultation_mode_list.php";
    String json_string="";
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"consultation_insert.php";
    String json_string2="";
    List<String> Lconsultation_mode = new ArrayList<String>();
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    Spinner consultation_mode_spinner;
    EditText name,mobile,time_date_booked,consultation_info;
    String sname="",smobile="",stime="",sdate="",samount="";
    Intent intent;
    Map<String,String> hamp = new HashMap<>();
    String id="",user_id;
    TextView tamount;

    String order_id="",sdesc="";
    String payment_id="",payment_signature="",payment_status="";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    String key_secret="8wivMe89eJvtU2jjaZRvLwcQ";

    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"updation_of_payment_consultation.php";
    String json_string3;

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
        setContentView(R.layout.activity_consultation_summary_page);
        intialise();
        progressDialog.show();
        new backgroundworker().execute();
        time_date_booked.setText(intent.getStringExtra("dater")+" : "+intent.getStringExtra("timer"));

        consultation_mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String temp = hamp.get(consultation_mode_spinner.getSelectedItem().toString());
                    consultation_info.setText("YOUR WAITING TIME : "+temp.split("-")[2].toString().toUpperCase());
                    id = temp.split("-")[0];
                    tamount.setText("YOUR FINAL PRICE - "+temp.split("-")[1]+" Rs");
                    samount = temp.split("-")[1];
                }catch (Exception e){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.final_checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sname = name.getText().toString();
                smobile = mobile.getText().toString();
                stime = intent.getStringExtra("timer").trim();
                sdate = intent.getStringExtra("dater").trim();
                progressDialog.show();
                new backgroundworker2().execute();
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
                Lconsultation_mode.clear();
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
                    Lconsultation_mode.add(jsonObject1.getString("category_of_appointment"));
                    sname = jsonObject1.getString("name");
                    smobile = jsonObject1.getString("mobile_number");
                    user_id = jsonObject1.getString("user_id");
                    hamp.put(jsonObject1.getString("category_of_appointment"),jsonObject1.getString("id")+"-"+jsonObject1.getString("amount")+"-"+jsonObject1.getString("time_of_consultation"));
                    count++;
                }
                if(Lconsultation_mode.size()>0){
                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(Consultation_Summary_Page.this, R.layout.custom_spinner_layout, Lconsultation_mode);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    consultation_mode_spinner.setAdapter(dataAdapter3);
                    name.setText(sname);
                    mobile.setText(smobile);

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
                sb2=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8")+"&"
                        +URLEncoder.encode("category_of_consultation","UTF-8")+"="+ URLEncoder.encode(id,"UTF-8")+"&"
                        +URLEncoder.encode("amount","UTF-8")+"="+ URLEncoder.encode(samount,"UTF-8")+"&"
                        +URLEncoder.encode("booked_date","UTF-8")+"="+ URLEncoder.encode(sdate,"UTF-8")+"&"
                        +URLEncoder.encode("time_slot","UTF-8")+"="+ URLEncoder.encode(stime,"UTF-8")+"&"
                        +URLEncoder.encode("name","UTF-8")+"="+ URLEncoder.encode(sname,"UTF-8")+"&"
                        +URLEncoder.encode("mobile","UTF-8")+"="+ URLEncoder.encode(smobile,"UTF-8");
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
            try {
                JSONObject jsonObject2 = new JSONObject(json_string2);
                order_id = jsonObject2.getString("id");
                startPayment();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public void intialise(){

        sessionMaintance = new SessionMaintance(Consultation_Summary_Page.this);

        progressDialog = new ProgressDialog(Consultation_Summary_Page.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        consultation_mode_spinner = findViewById(R.id.country);

        name = findViewById(R.id.editTextText4_state);
        mobile = findViewById(R.id.editTextText4_district);
        time_date_booked = findViewById(R.id.editTextText4_mob);
        consultation_info = findViewById(R.id.editTextText4_booked_time);

        tamount = findViewById(R.id.textView47);

        intent = getIntent();

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            payment_id = s;
            payment_signature = paymentData.getSignature();
            String  abc = calculateRFC2104HMAC(order_id+"|"+s,key_secret);
            if(abc.equals(paymentData.getSignature())){
                Toast.makeText(this, "Successfully Validated", Toast.LENGTH_SHORT).show();
                payment_status = "Success";
                progressDialog.show();
                new backgroundworker3().execute();
            }else{
                payment_status = "Failure";
                progressDialog.show();
                new backgroundworker3().execute();
            }
            Log.d("ASD123","\n\n"+s+"\n\n"+paymentData.getOrderId()+"\n\n"+paymentData.getSignature()+"\n\n"+abc+"\n\n"
                    +order_id+"\n\n"+payment_status);


        } catch (SignatureException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        payment_id = s;
        payment_signature = "";
        payment_status = "Failure";
        progressDialog.show();
        new backgroundworker3().execute();
    }

    public void startPayment() {

        Checkout checkout = new Checkout();
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();
            options.put("name", sessionMaintance.get_user_mail());
            options.put("description", sdesc);
            options.put("order_id", order_id);
            options.put("currency", "INR");
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("GGG123", "Error in starting Razorpay Checkout", e);
        }
    }

    public static String calculateRFC2104HMAC(String data, String key) throws java.security.SignatureException {
        String result ="";
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            result =  new String(Hex.encodeHex(sha256_HMAC.doFinal(data.getBytes("UTF-8"))));
            return new String(Hex.encodeHex(sha256_HMAC.doFinal(data.getBytes("UTF-8"))));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
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
                String post_data = URLEncoder.encode("order_id","UTF-8")+"="+URLEncoder.encode(order_id,"UTF-8")+"&"
                        +URLEncoder.encode("payment_id","UTF-8")+"="+ URLEncoder.encode(payment_id,"UTF-8")+"&"
                        +URLEncoder.encode("payment_signature","UTF-8")+"="+ URLEncoder.encode(payment_signature,"UTF-8")+"&"
                        +URLEncoder.encode("payment_status","UTF-8")+"="+ URLEncoder.encode(payment_status,"UTF-8");
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
                    Log.d("json_string123",""+json_string3);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb3.toString());
                return sb3.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){

            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string3 = result;
            progressDialog.dismiss();
            Log.d("RESULT1234", "" + json_string3);
            if(json_string3.equals("1")&&payment_status.equals("Success")){
                Intent intent = new Intent(Consultation_Summary_Page.this,Main_menu.class);
                Toast.makeText(Consultation_Summary_Page.this, "Payment is Success", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }else if(payment_status.equals("Failure")){
                Intent intent = new Intent(Consultation_Summary_Page.this,Main_menu.class);
                startActivity(intent);
            }else if(payment_status.equals("Success")){
                Intent intent = new Intent(Consultation_Summary_Page.this,Main_menu.class);
                Toast.makeText(Consultation_Summary_Page.this, "Payment is Success", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }

        }
    }
}