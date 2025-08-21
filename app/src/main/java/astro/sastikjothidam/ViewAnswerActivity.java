package astro.sastikjothidam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

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
import java.util.List;

public class ViewAnswerActivity extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"member_chat_view_answer.php";
    String json_string="";
    List<String> Lmessage_id = new ArrayList<>();
    List<String> Lmessage = new ArrayList<>();
    List<String> Lmessage_date = new ArrayList<>();
    List<String> Lmessager_mail_id = new ArrayList<>();
    List<String> Lmessager_name = new ArrayList<>();
    List<String> Limage_url = new ArrayList<>();
    CustomAdapter customAdapter;
    ListView listView;
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    Intent intent;
    ImageView Iquestionimage;
    TextView Iquestion;

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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_answer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intialise();

        progressDialog.show();
        new backgroundworker().execute();
    }

    public void intialise(){

        progressDialog = new ProgressDialog(ViewAnswerActivity.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(ViewAnswerActivity.this);

        customAdapter = new CustomAdapter();

        listView = findViewById(R.id.listView);
        listView.setDivider(null);

        intent = getIntent();

        Iquestionimage = findViewById(R.id.imageView7);
        Glide.with(ViewAnswerActivity.this)
                .load(intent.getStringExtra("image"))
                .into(Iquestionimage);

        Iquestion = findViewById(R.id.textView13);
        Iquestion.setText(intent.getStringExtra("question"));
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
                Lmessage.clear();
                Lmessage_date.clear();
                Lmessager_mail_id.clear();
                Lmessager_name.clear();
                Limage_url.clear();
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("que_id","UTF-8")+"="+URLEncoder.encode(intent.getStringExtra("que_id"),"UTF-8");
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
                    Lmessager_name.add(jsonObject1.getString("user_name"));
                    Lmessage_date.add(jsonObject1.getString("created_on"));
                    Lmessage.add(jsonObject1.getString("answer"));
                    Lmessager_mail_id.add(jsonObject1.getString("mail_id"));
                    Limage_url.add(jsonObject1.getString("image"));
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
            return Lmessage.size();
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

            TextView sent,received;

            if(Limage_url.get(i).equals("-")){
                if(Lmessager_mail_id.get(i).equals(sessionMaintance.get_user_mail())) {
                    view = getLayoutInflater().inflate(R.layout.custom_layout_sent_message_item, null);
                    sent = view.findViewById(R.id.sent_message);
                    sent.setText(Lmessage.get(i));
                }else{
                    view = getLayoutInflater().inflate(R.layout.custom_layout_received_message_item, null);
                    received = view.findViewById(R.id.received_message);

                    String fullMessage = Lmessager_name.get(i) + "\n" + Lmessage.get(i) + "\n" + Lmessage_date.get(i);
                    SpannableString spannable = new SpannableString(fullMessage);

                    // Set the style for the sender's name (bold, larger size)
                    spannable.setSpan(new StyleSpan(Typeface.ITALIC), 0, Lmessager_name.get(i).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, Lmessager_name.get(i).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new RelativeSizeSpan(0.75f), 0, Lmessager_name.get(i).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Set the style for the message (default style)
                    int messageStart = Lmessager_name.get(i).length() + 1;
                    int messageEnd = messageStart + Lmessage.get(i).length();
                    spannable.setSpan(new ForegroundColorSpan(Color.BLACK), messageStart, messageEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Set the style for the time (smaller size, grey color)
                    int timeStart = messageEnd + 1;
                    spannable.setSpan(new ForegroundColorSpan(Color.GRAY), timeStart, fullMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new RelativeSizeSpan(0.75f), timeStart, fullMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Set the spannable string to the TextView
                    received.setText(spannable);
                }
            }else{
                if(Lmessager_mail_id.get(i).equals(sessionMaintance.get_user_mail())) {
                    view = getLayoutInflater().inflate(R.layout.custom_layout_sent_message_item_with_image, null);
                    sent = view.findViewById(R.id.sent_message);
                    sent.setText(Lmessage.get(i));
                    ImageView sender_image = view.findViewById(R.id.sender_image);

                    Glide.with(ViewAnswerActivity.this)
                            .load(Limage_url.get(i))
                            .into(sender_image);



                    sender_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = Limage_url.get(i);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(intent);
                        }
                    });

                }else{
                    view = getLayoutInflater().inflate(R.layout.custom_layout_received_message_item_with_image, null);
                    received = view.findViewById(R.id.received_message);

                    String fullMessage = Lmessager_name.get(i) + "\n" + Lmessage.get(i) + "\n" + Lmessage_date.get(i);
                    SpannableString spannable = new SpannableString(fullMessage);

                    // Set the style for the sender's name (bold, larger size)
                    spannable.setSpan(new StyleSpan(Typeface.ITALIC), 0, Lmessager_name.get(i).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, Lmessager_name.get(i).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new RelativeSizeSpan(0.75f), 0, Lmessager_name.get(i).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Set the style for the message (default style)
                    int messageStart = Lmessager_name.get(i).length() + 1;
                    int messageEnd = messageStart + Lmessage.get(i).length();
                    spannable.setSpan(new ForegroundColorSpan(Color.BLACK), messageStart, messageEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Set the style for the time (smaller size, grey color)
                    int timeStart = messageEnd + 1;
                    spannable.setSpan(new ForegroundColorSpan(Color.GRAY), timeStart, fullMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new RelativeSizeSpan(0.75f), timeStart, fullMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Set the spannable string to the TextView
                    received.setText(spannable);

                    ImageView receiver_image = view.findViewById(R.id.receiver_image);
                    Glide.with(ViewAnswerActivity.this)
                            .load(Limage_url.get(i))
                            .into(receiver_image);

                    receiver_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = Limage_url.get(i);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(intent);
                        }
                    });
                }
            }



            return view;
        }


    }
}