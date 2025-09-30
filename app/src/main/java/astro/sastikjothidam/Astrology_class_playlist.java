package astro.sastikjothidam;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class Astrology_class_playlist extends AppCompatActivity implements PaymentResultWithDataListener {

    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"Astrology_class_playlist.php";
    String json_string="";

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"Astrology_class_playlist_with_filter.php";
    String json_string2="";

    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    List<String> Lclass_id = new ArrayList<>();
    List<String> Lclass_name = new ArrayList<>();
    List<String> Lclass_description = new ArrayList<>();
    List<String> Lclass_price = new ArrayList<>();
    List<String> Lclass_image_link = new ArrayList<>();
    List<String> Lclass_video_link = new ArrayList<>();
    List<String> Lclass_video_link2 = new ArrayList<>();
    List<String> Lclass_num_of_videos = new ArrayList<>();
    List<String> Lclass_payment_status = new ArrayList<>();
    CustomAdapter customAdapter;
    ListView listView;
    AutoCompleteTextView autocompletetextView;
    String selected_text="";

    String order_id="",samount="",sdesc="",splan_dur="",splan_id="";
    String payment_id="",payment_signature="",payment_status="";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    String key_secret="8wivMe89eJvtU2jjaZRvLwcQ";

    StringBuffer sb22 = new StringBuffer();
    String json_url22 = Url_interface.url+"generation_of_order_id_class.php";
    String json_string22;
    JSONObject jsonObject22;

    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"updation_of_payment_class.php";
    String json_string3;

    Intent book_intent;

    String type="";

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
        setContentView(R.layout.activity_astrology_class_playlist);

        intialise();

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat insetsController =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());

        // Hide both status bar and navigation bar
        insetsController.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());

        // Optional: Make them re-appear with swipe
        insetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);



        autocompletetextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                //Toast.makeText(Astrology_class_playlist.this, ""+selected, Toast.LENGTH_SHORT).show();
                selected_text = selected;
                progressDialog.show();
                new backgroundworker2().execute();
            }
        });

        findViewById(R.id.imageView6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autocompletetextView.setText("");
                progressDialog.show();
                new backgroundworker().execute();
            }
        });

        try{
            if(book_intent.getStringExtra("type").equals("pdf")){
                json_url1 = Url_interface.url+"Astrology_class_playlist2.php";
                json_url2 = Url_interface.url+"Astrology_class_playlist_with_filter2.php";
                json_url22 = Url_interface.url+"generation_of_order_id_class3.php";
                json_url3 = Url_interface.url+"updation_of_payment_class3.php";
                type="pdf";
                progressDialog.show();
                new backgroundworker().execute();
            }
        }catch (Exception e){
            json_url1 = Url_interface.url+"Astrology_class_playlist.php";
            json_url2 = Url_interface.url+"Astrology_class_playlist_with_filter.php";
            json_url22 = Url_interface.url+"generation_of_order_id_class.php";
            json_url3 = Url_interface.url+"updation_of_payment_class.php";
            type="pdf";
            progressDialog.show();
            new backgroundworker().execute();
        }



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

                Lclass_id.clear();
                Lclass_name.clear();
                Lclass_description.clear();
                Lclass_price.clear();
                Lclass_image_link.clear();
                Lclass_video_link.clear();
                Lclass_video_link2.clear();
                Lclass_num_of_videos.clear();
                Lclass_payment_status.clear();

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

                    Lclass_id.add(jsonObject1.getString("id"));
                    Lclass_name.add(jsonObject1.getString("class_name"));
                    Lclass_description.add(jsonObject1.getString("class_description"));
                    Lclass_price.add(jsonObject1.getString("class_price"));
                    Lclass_image_link.add(jsonObject1.getString("class_image"));
                    Lclass_video_link.add(jsonObject1.getString("class_video_preview_link"));
                    if(jsonObject1.has("class_video_preview_link1")){
                        Lclass_video_link2.add(jsonObject1.getString("class_video_preview_link1"));
                    }
                    Lclass_num_of_videos.add(jsonObject1.getString("num_of_videos"));
                    Lclass_payment_status.add(jsonObject1.getString("payment_status"));
                    count++;
                }
                if(Lclass_id.size()>0){
                    listView.setAdapter(customAdapter);
                    ArrayAdapter<String> Astrology_class_adapter=new ArrayAdapter<String>(Astrology_class_playlist.this,android.R.layout.simple_dropdown_item_1line,Lclass_name);
                    autocompletetextView.setThreshold(1);
                    autocompletetextView.setAdapter(Astrology_class_adapter);
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

                Lclass_id.clear();
                Lclass_name.clear();
                Lclass_description.clear();
                Lclass_price.clear();
                Lclass_image_link.clear();
                Lclass_video_link.clear();
                Lclass_video_link2.clear();
                Lclass_num_of_videos.clear();
                Lclass_payment_status.clear();

                sb2=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("selected_class_name","UTF-8")+"="+ URLEncoder.encode(selected_text,"UTF-8");
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
            int count=0;
            Log.d("RESULT", "" + result);
            try{
                JSONObject jsonObject = new JSONObject(json_string2);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                while(count < jsonArray.length()){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);

                    Lclass_id.add(jsonObject1.getString("id"));
                    Lclass_name.add(jsonObject1.getString("class_name"));
                    Lclass_description.add(jsonObject1.getString("class_description"));
                    Lclass_price.add(jsonObject1.getString("class_price"));
                    Lclass_image_link.add(jsonObject1.getString("class_image"));
                    Lclass_video_link.add(jsonObject1.getString("class_video_preview_link"));
                    if(jsonObject1.has("class_video_preview_link1")){
                        Lclass_video_link2.add(jsonObject1.getString("class_video_preview_link1"));
                    }
                    Lclass_num_of_videos.add(jsonObject1.getString("num_of_videos"));
                    Lclass_payment_status.add(jsonObject1.getString("payment_status"));
                    count++;
                }
                if(Lclass_id.size()>0){
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e){

            }


        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lclass_id.size();
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

            view = getLayoutInflater().inflate(R.layout.custom_layout_astrology_class_playlist, null);

            TextView class_name_txt = view.findViewById(R.id.textView19);
            TextView class_name = view.findViewById(R.id.textView20);
            TextView class_vieos = view.findViewById(R.id.textView1200);
            ImageView class_image = view.findViewById(R.id.imageView7);
            ImageView class_video_link = view.findViewById(R.id.imageView13);
            Button pay_now_button = view.findViewById(R.id.button3);

            class_video_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Astrology_class_playlist.this, Single_Video_Youtube_player.class);
                    intent.putExtra("videoId",Lclass_video_link.get(i));
                    startActivity(intent);
                }
            });

            class_name.setText(Lclass_name.get(i));

            Glide
                    .with(Astrology_class_playlist.this)
                    .load(Lclass_image_link.get(i))
                    .into(class_image);

            class_vieos.setText(Lclass_num_of_videos.get(i));



            if(Lclass_payment_status.get(i).equals("Success")){
                pay_now_button.setText("VIEW");
                pay_now_button.setBackgroundColor(pay_now_button.getContext().getResources().getColor(R.color.dark_green));
            }else{
                pay_now_button.setText("PAY NOW - "+Lclass_price.get(i)+" ₹");
            }

            if(pay_now_button.getText().toString().equals("VIEW")){
                pay_now_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(type.equals("pdf")){
                            Intent intent = new Intent(Astrology_class_playlist.this, Pdf_Player.class);
                            intent.putExtra("url", Lclass_video_link.get(i));
                            intent.putExtra("type", type);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(Astrology_class_playlist.this, Astrology_Class_List_View.class);
                            intent.putExtra("class_category_id", Lclass_id.get(i));
                            startActivity(intent);
                        }
                    }
                });
            }else{
                pay_now_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        splan_id = Lclass_id.get(i);
                        samount = Lclass_price.get(i);
                        sdesc = Lclass_name.get(i);
                        progressDialog.show();
                        new backgroundworker22().execute();
                    }
                });
            }

            if(type.equals("pdf")) {
                class_name_txt.setText("BOOK TITLE");
                view.findViewById(R.id.textView1199).setVisibility(GONE);
                view.findViewById(R.id.textView1200).setVisibility(GONE);
                view.findViewById(R.id.textView11999).setVisibility(GONE);
                view.findViewById(R.id.imageView13).setVisibility(GONE);
                view.findViewById(R.id.divider1000).setVisibility(GONE);
                view.findViewById(R.id.divider10).setVisibility(GONE);
            }


            return view;

        }


    }

    public void intialise(){

        progressDialog = new ProgressDialog(Astrology_class_playlist.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(Astrology_class_playlist.this);

        customAdapter = new CustomAdapter();

        listView = findViewById(R.id.listView);
        listView.setDivider(null);

        autocompletetextView=(AutoCompleteTextView)findViewById(R.id.editTextText);

        book_intent = getIntent();
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

    public class backgroundworker22 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url22);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb22=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("amount","UTF-8")+"="+URLEncoder.encode(samount,"UTF-8")+"&"
                        +URLEncoder.encode("mobile_no","UTF-8")+"="+ URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("plan_id","UTF-8")+"="+ URLEncoder.encode(splan_id,"UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string22=bufferedReader.readLine())!=null)
                {
                    sb22.append(json_string22+"\n");
                    Log.d("json_string",""+json_string22);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb22.toString());
                return sb22.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){

            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string22 = result;
            progressDialog.dismiss();
            Log.d("RESULT123", "" + json_string22);
            try {
                jsonObject22 = new JSONObject(json_string22);
                order_id = jsonObject22.getString("id");
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
                Intent intent = new Intent(Astrology_class_playlist.this,Main_menu.class);
                Toast.makeText(Astrology_class_playlist.this, "Payment is Success", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }else if(payment_status.equals("Failure")){
                Intent intent = new Intent(Astrology_class_playlist.this,Main_menu.class);
                startActivity(intent);
            }else if(payment_status.equals("Success")){
                Intent intent = new Intent(Astrology_class_playlist.this,Main_menu.class);
                Toast.makeText(Astrology_class_playlist.this, "Payment is Success", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }

        }
    }
}