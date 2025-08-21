package astro.sastikjothidam;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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

public class consultation_chat extends AppCompatActivity implements PaymentResultWithDataListener {

    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"Consultation_chat.php";
    String json_string="";
    List<String> Lmessage_id = new ArrayList<>();
    List<String> Lmessage_date = new ArrayList<>();
    List<String> Lquestion = new ArrayList<>();
    List<String> Lque_image_url = new ArrayList<>();
    List<String> Lanswer = new ArrayList<>();
    List<String> Lanswer_image_url = new ArrayList<>();
    List<String> Lpayment_status = new ArrayList<>();
    EditText mymsg;
    String smymsg = "";;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_PERMISSION_CODE = 100;
    private Uri imageUri;
    String base64Image="";
    FloatingActionButton send_button;
    CustomAdapter customAdapter;
    ListView listView;
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;

    String order_id="",samount="750",sdesc="",splan_dur="",splan_id="";
    String payment_id="",payment_signature="",payment_status="";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    String key_secret="8wivMe89eJvtU2jjaZRvLwcQ";

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"generation_of_order_id_consultation_chat.php";
    String json_string2;
    JSONObject jsonObject2;

    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"updation_of_payment_consultation_chat.php";
    String json_string3;

    TextView imagename;

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
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultation_chat);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        intialise();

        progressDialog.show();
        new backgroundworker().execute();

        findViewById(R.id.imageView34).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionAndOpenGallery();
            }
        });

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smymsg = mymsg.getText().toString();
                if(smymsg.length()>0){
                    if(base64Image.toString().equals("")){
                        Toast.makeText(consultation_chat.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                    }else{
                        mymsg.setText("");
                        progressDialog.show();
                        new backgroundworker2().execute();
                        hideKeyboard(mymsg);
                    }

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Lpayment_status.get(position).equals("Success")){
                    if(Lanswer.get(position).length()>0) {
                        showImageDialog(consultation_chat.this, Lanswer_image_url.get(position), Lanswer.get(position));
                    }else{
                        Toast.makeText(consultation_chat.this, "No Answer Available", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(consultation_chat.this, "Payment not Made for This Chat Ask the Question Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void intialise(){
        progressDialog = new ProgressDialog(consultation_chat.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(consultation_chat.this);
        mymsg = findViewById(R.id.editTextText5);

        customAdapter = new CustomAdapter();

        listView = findViewById(R.id.listView);
        listView.setDivider(null);

        send_button = findViewById(R.id.floatingActionButton);

        mymsg = findViewById(R.id.editTextText5);

        imagename = findViewById(R.id.textView105);
    }

    public void showImageDialog(Context context,String ans_image_url,String answer) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        ImageView imageView = dialogView.findViewById(R.id.dialog_image);
        TextView textView = dialogView.findViewById(R.id.dialog_text);
        String imageUrl = ans_image_url;
        Glide.with(context).load(imageUrl).into(imageView);

        String url = ans_image_url;
        imageView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        });

        textView.setText(answer);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void checkPermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+ (API 33+)
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION_CODE);
            } else {
                openGallery();
            }
        } else { // Android 12 and below
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            } else {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                base64Image = encodeImageToBase64(bitmap);
                imagename.setText("Image Picked");
                Log.d("Base64_Image", base64Image); // You can now send this to your server
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos); // Compress if needed
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);

    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            IBinder windowToken = view.getWindowToken();
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
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
                        +URLEncoder.encode("user_mail","UTF-8")+"="+ URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("image","UTF-8")+"="+ URLEncoder.encode(base64Image.toString(),"UTF-8")+"&"
                        +URLEncoder.encode("question","UTF-8")+"="+ URLEncoder.encode(smymsg,"UTF-8");
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
                Intent intent = new Intent(consultation_chat.this,Main_menu.class);
                Toast.makeText(consultation_chat.this, "Payment is Success", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }else if(payment_status.equals("Failure")){
                Intent intent = new Intent(consultation_chat.this,Main_menu.class);
                startActivity(intent);
            }else if(payment_status.equals("Success")){
                Intent intent = new Intent(consultation_chat.this,Main_menu.class);
                Toast.makeText(consultation_chat.this, "Payment is Success", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }

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
                Lmessage_id.clear();
                Lmessage_date.clear();
                Lquestion.clear();
                Lque_image_url.clear();
                Lanswer.clear();
                Lanswer_image_url.clear();
                Lpayment_status.clear();
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_mail","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8");
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

                    Lmessage_id.add(jsonObject1.getString("id"));
                    Lmessage_date.add(jsonObject1.getString("created_on"));
                    Lquestion.add(jsonObject1.getString("question"));
                    Lque_image_url.add(jsonObject1.getString("que_image"));
                    Lanswer.add(jsonObject1.getString("answer"));
                    Lanswer_image_url.add(jsonObject1.getString("ans_image"));
                    Lpayment_status.add(jsonObject1.getString("payment_status"));

                    count++;
                }
                if(Lmessage_id.size()>0){
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e){

            }
        }
    }

    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Lmessage_id.size();
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
            if (view != null){
                return view;
            }
            TextView sent;
            view = getLayoutInflater().inflate(R.layout.custom_layout_sent_message_item_with_image, null);
            sent = view.findViewById(R.id.sent_message);
            sent.setText(Lquestion.get(i));
            ImageView sender_image = view.findViewById(R.id.sender_image);

            Glide.with(consultation_chat.this)
                            .load(Lque_image_url.get(i))
                            .into(sender_image);

            sender_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = Lque_image_url.get(i);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(intent);
                        }
            });

            return view;
        }


    }

}