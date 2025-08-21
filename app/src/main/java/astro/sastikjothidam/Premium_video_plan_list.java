package astro.sastikjothidam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Premium_video_plan_list extends AppCompatActivity implements PaymentResultWithDataListener {

    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"Premium_video_plans.php";
    String json_string="";
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    List<String> Lplan_name = new ArrayList<>();
    List<String> Lplan_price = new ArrayList<>();
    List<String> Lplan_offer_price = new ArrayList<>();
    List<String> Lplan_final_price = new ArrayList<>();
    List<String> Lplan_duration = new ArrayList<>();
    List<String> Lplan_id = new ArrayList<>();
    CustomAdapter customAdapter;
    ListView listView;

    String order_id="",samount="",sdesc="",splan_dur="",splan_id="";
    String payment_id="",payment_signature="",payment_status="";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    String key_secret="8wivMe89eJvtU2jjaZRvLwcQ";

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"generation_of_order_id_premium.php";
    String json_string2;
    JSONObject jsonObject2;

    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"updation_of_payment_premium.php";
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
        setContentView(R.layout.activity_premium_video_plan_list);

        intialise();

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat insetsController =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());

        // Hide both status bar and navigation bar
        insetsController.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());

        // Optional: Make them re-appear with swipe
        insetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                splan_id = Lplan_id.get(position);
                samount = Lplan_final_price.get(position);
                sdesc = Lplan_name.get(position);
                splan_dur = Lplan_duration.get(position);
                progressDialog.show();
                new backgroundworker2().execute();
            }
        });

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
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                while(count < jsonArray.length()){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);

                    Lplan_name.add(jsonObject1.getString("plan_name")+" Months");
                    Lplan_price.add(jsonObject1.getString("amount"));
                    Lplan_offer_price.add(jsonObject1.getString("offer"));
                    Lplan_final_price.add(jsonObject1.getString("final_price"));
                    Lplan_duration.add(jsonObject1.getString("duration"));
                    Lplan_id.add(jsonObject1.getString("id"));

                    count++;
                }
                if(Lplan_name.size()>0){
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e){

            }


        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lplan_name.size();
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

            view = getLayoutInflater().inflate(R.layout.custom_layout_premium_plan_screen, null);

            TextView plan_name = view.findViewById(R.id.textView);
            TextView plan_price = view.findViewById(R.id.textView60);
            TextView plan_offer_price = view.findViewById(R.id.textView62);
            TextView plan_final_price = view.findViewById(R.id.textView64);

            plan_name.setText(Lplan_name.get(i));
            plan_price.setText(Lplan_price.get(i)+" Rs");
            plan_offer_price.setText(Lplan_offer_price.get(i)+" Rs");
            plan_final_price.setText(Lplan_final_price.get(i)+" Rs/-");

            return view;

        }


    }

    public void intialise(){

        progressDialog = new ProgressDialog(Premium_video_plan_list.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(Premium_video_plan_list.this);

        customAdapter = new CustomAdapter();

        listView = findViewById(R.id.listView);
        listView.setDivider(null);

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
                String post_data = URLEncoder.encode("amount","UTF-8")+"="+URLEncoder.encode(samount,"UTF-8")+"&"
                        +URLEncoder.encode("mobile_no","UTF-8")+"="+ URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("plan_period","UTF-8")+"="+ URLEncoder.encode(splan_dur,"UTF-8")+"&"
                        +URLEncoder.encode("plan_id","UTF-8")+"="+ URLEncoder.encode(splan_id,"UTF-8");
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
            }catch (Exception e){

            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string2 = result;
            progressDialog.dismiss();
            Log.d("RESULT123", "" + json_string2);
            try {
                jsonObject2 = new JSONObject(json_string2);
                order_id = jsonObject2.getString("id");
                startPayment();
            } catch (Exception e) {
                e.printStackTrace();
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
                Intent intent = new Intent(Premium_video_plan_list.this,Main_menu.class);
                Toast.makeText(Premium_video_plan_list.this, "Payment is Success", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }else if(payment_status.equals("Failure")){
                Intent intent = new Intent(Premium_video_plan_list.this,Main_menu.class);
                startActivity(intent);
            }else if(payment_status.equals("Success")){
                Intent intent = new Intent(Premium_video_plan_list.this,Main_menu.class);
                Toast.makeText(Premium_video_plan_list.this, "Payment is Success", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }

        }
    }
}