package astro.sastikjothidam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Chat_member extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"Member_chat.php";
    String json_string="";
    List<String> Lmessage_id = new ArrayList<>();
    List<String> Lmessage = new ArrayList<>();
    List<String> Lmessage_date = new ArrayList<>();
    List<String> Lmessager_mail_id = new ArrayList<>();
    List<String> Lmessager_name = new ArrayList<>();
    List<String> Limage_url = new ArrayList<>();
    List<String> Ltopic = new ArrayList<>();
    CustomAdapter customAdapter;
    ListView listView;
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    FloatingActionButton send_button;
    EditText mymsg;
    String smymsg = "";
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"member_message_post.php";
    String json_string2="";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_PERMISSION_CODE = 100;
    private Uri imageUri;
    String base64Image="";
    String topic, designation, phone_no;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri = null;

    AutoCompleteTextView autocompletetextView;
    String selected_text="";
    StringBuffer sb22 = new StringBuffer();
    String json_url22 = Url_interface.url+"member_chat_topic_filter.php";
    String json_string22="";

    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"member_chat_answer_post_table.php";
    String json_string3="";
    String que_id="";

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
        setContentView(R.layout.activity_chat_member);
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

        autocompletetextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                selected_text = selected;
                progressDialog.show();
                new backgroundworker22().execute();
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

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Chat_member.this);
                builder.setTitle("Enter Person Details");
                LinearLayout layout = new LinearLayout(Chat_member.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(50, 40, 50, 10);
                final EditText topic_input = new EditText(Chat_member.this);
                topic_input.setHint("Topic");
                layout.addView(topic_input);
                final EditText designation_input = new EditText(Chat_member.this);
                designation_input.setHint("Jathagar Designation");
                layout.addView(designation_input);
                final EditText phone_input = new EditText(Chat_member.this);
                phone_input.setHint("Jathagar Phone Number");
                phone_input.setInputType(InputType.TYPE_CLASS_PHONE);
                layout.addView(phone_input);

                builder.setView(layout);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        topic = topic_input.getText().toString();
                        designation = designation_input.getText().toString();
                        phone_no = phone_input.getText().toString();
                        smymsg = mymsg.getText().toString();
                        if(smymsg.length()>0){
                            progressDialog.show();
                            new backgroundworker2().execute();
                            mymsg.setText("");
                            hideKeyboard(mymsg);
                        }
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.show();


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem_Que = Lmessage.get(position);
                String selectedItem_Ima = Limage_url.get(position);
                que_id = Lmessage_id.get(position);

                // Show Popup
                AlertDialog.Builder builder = new AlertDialog.Builder(Chat_member.this);
                builder.setTitle("Choose Option");
                builder.setMessage("What do you want to do?");

                builder.setPositiveButton("View Answer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Open ViewAnswerActivity
                        Intent intent = new Intent(Chat_member.this, ViewAnswerActivity.class);
                        intent.putExtra("question", selectedItem_Que);
                        intent.putExtra("image",selectedItem_Ima);
                        intent.putExtra("que_id",que_id);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Give Answer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Chat_member.this);
                        builder.setTitle("Give Answer");

                        LinearLayout layout = new LinearLayout(Chat_member.this);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        layout.setPadding(30, 30, 30, 30);

                        EditText answerInput = new EditText(Chat_member.this);
                        answerInput.setHint("Type your answer");
                        layout.addView(answerInput);

                        // Choose Image Button
                        Button chooseImageButton = new Button(Chat_member.this);
                        chooseImageButton.setText("Choose Image");
                        layout.addView(chooseImageButton);



                        // Choose Image click
                        chooseImageButton.setOnClickListener(v -> {
                            checkPermissionAndOpenGallery();
                        });

                        builder.setView(layout);

                        // Submit
                        builder.setPositiveButton("Submit", (dialog1, which1) -> {
                            smymsg = answerInput.getText().toString();
                            if (imageUri != null) {
                                base64Image = encodeImageToBase64(selectedImageUri);
                                Toast.makeText(Chat_member.this, "Answer: " + smymsg + "\nImage selected", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Chat_member.this, "Answer: " + smymsg + "\nNo image selected", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.show();
                            new backgroundworker3().execute();
                        });

                        // Cancel
                        builder.setNegativeButton("Cancel", null);
                        builder.show();

                    }
                });

                builder.setNeutralButton("Cancel", null);
                builder.show();
            }
        });

    }

    private String encodeImageToBase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            inputStream.close();
            byteArrayOutputStream.close();

            return Base64.encodeToString(imageBytes, Base64.NO_WRAP); // This is your Base64 encoded string

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
            selectedImageUri = imageUri;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                base64Image = encodeImageToBase64(bitmap);
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

    public void intialise(){

        progressDialog = new ProgressDialog(Chat_member.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(Chat_member.this);

        customAdapter = new CustomAdapter();

        listView = findViewById(R.id.listView);
        listView.setDivider(null);

        send_button = findViewById(R.id.floatingActionButton);

        mymsg = findViewById(R.id.editTextText5);

        autocompletetextView=(AutoCompleteTextView)findViewById(R.id.editTextText);
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
                Ltopic.clear();
                json_string="";
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = "";
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
                    Lmessage.add(jsonObject1.getString("message"));
                    Lmessager_mail_id.add(jsonObject1.getString("mail_id"));
                    Limage_url.add(jsonObject1.getString("image"));
                    Ltopic.add(jsonObject1.getString("topic"));
                    count++;
                }
                if(Lmessage_id.size()>0){
                    listView.setAdapter(customAdapter);
                    ArrayAdapter<String> Astrology_class_adapter=new ArrayAdapter<String>(Chat_member.this,android.R.layout.simple_dropdown_item_1line,Ltopic);
                    autocompletetextView.setThreshold(1);
                    autocompletetextView.setAdapter(Astrology_class_adapter);
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

                    Glide.with(Chat_member.this)
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
                    Glide.with(Chat_member.this)
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

                json_string2 = "";
                sb2=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mail","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("message","UTF-8")+"="+ URLEncoder.encode(smymsg,"UTF-8")+"&"
                        +URLEncoder.encode("image","UTF-8")+"="+ URLEncoder.encode(base64Image,"UTF-8")+"&"
                        +URLEncoder.encode("topic","UTF-8")+"="+ URLEncoder.encode(topic,"UTF-8")+"&"
                        +URLEncoder.encode("designation","UTF-8")+"="+ URLEncoder.encode(designation,"UTF-8")+"&"
                        +URLEncoder.encode("phone","UTF-8")+"="+ URLEncoder.encode(phone_no,"UTF-8");
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
                if(json_string2.equals("YES")){
                    new backgroundworker().execute();
                    //customAdapter.notifyDataSetChanged();
                }
            }catch (Exception e){

            }


        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            IBinder windowToken = view.getWindowToken();
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
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

                Lmessage_id.clear();
                Lmessage.clear();
                Lmessage_date.clear();
                Lmessager_mail_id.clear();
                Lmessager_name.clear();
                Limage_url.clear();
                Ltopic.clear();

                sb22=new StringBuffer();
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
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string22 = result;
            progressDialog.dismiss();
            int count=0;
            Log.d("RESULT", "" + result);
            try{
                JSONObject jsonObject = new JSONObject(json_string22);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                while(count < jsonArray.length()){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);
                    Lmessage_id.add(jsonObject1.getString("id"));
                    Lmessager_name.add(jsonObject1.getString("user_name"));
                    Lmessage_date.add(jsonObject1.getString("created_on"));
                    Lmessage.add(jsonObject1.getString("message"));
                    Lmessager_mail_id.add(jsonObject1.getString("mail_id"));
                    Limage_url.add(jsonObject1.getString("image"));
                    Ltopic.add(jsonObject1.getString("topic"));
                    count++;
                    count++;
                }
                if(Lmessage_id.size()>0){
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e){

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

                json_string3 = "";
                sb3=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mail","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("message","UTF-8")+"="+ URLEncoder.encode(smymsg,"UTF-8")+"&"
                        +URLEncoder.encode("image","UTF-8")+"="+ URLEncoder.encode(base64Image,"UTF-8")+"&"
                        +URLEncoder.encode("que_id","UTF-8")+"="+ URLEncoder.encode(que_id,"UTF-8");
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
                    Log.d("json_string",""+json_string3);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb3.toString());
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
                if(json_string3.equals("YES")){
                    Toast.makeText(Chat_member.this, "Posted", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }


        }
    }
}