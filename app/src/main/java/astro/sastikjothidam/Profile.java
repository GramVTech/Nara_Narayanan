package astro.sastikjothidam;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

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
import java.util.Calendar;
import java.util.Locale;


public class Profile extends AppCompatActivity {

    ImageView ProfileImage;
    TextView Name, Mobile, Mail, Address;

    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"profile.php";
    String json_string="";

    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;

    String sprofession,spob,sdob,stob,sgender;

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"profile_update.php";
    String json_string2="";

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
        setContentView(R.layout.activity_profile);
        intialise();

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat insetsController =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());

        // Hide both status bar and navigation bar
        insetsController.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());

        // Optional: Make them re-appear with swipe
        insetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        progressDialog.show();
        new backgroundworker().execute();

        findViewById(R.id.cardView13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Order_summary.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageView48).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionMaintance sessionMaintance = new SessionMaintance(Profile.this);
                sessionMaintance.set_user_mail("");
                finish();
                Intent intent = new Intent(Profile.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
            Log.d("RESULT", "" + result);
            try{
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                Name.setText(jsonObject1.getString("full_name"));
                Mobile.setText(jsonObject1.getString("mobile_number"));
                Mail.setText(jsonObject1.getString("mail_id"));
                Address.setText(jsonObject1.getString("address"));

                String firstLetter = String.valueOf(jsonObject1.getString("full_name").toString().trim().toUpperCase().charAt(0));
                int bgColor = generateColorFromLetter(firstLetter);
                int textColor = Color.WHITE;
                Bitmap initialsBitmap = getInitialBitmap(firstLetter, 200, bgColor, textColor);
                ProfileImage.setImageBitmap(initialsBitmap);





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
                String post_data = URLEncoder.encode("user_mail","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("profession","UTF-8")+"="+ URLEncoder.encode(sprofession,"UTF-8")+"&"
                        +URLEncoder.encode("dob","UTF-8")+"="+ URLEncoder.encode(sdob,"UTF-8")+"&"
                        +URLEncoder.encode("tob","UTF-8")+"="+ URLEncoder.encode(stob,"UTF-8")+"&"
                        +URLEncoder.encode("pob","UTF-8")+"="+ URLEncoder.encode(spob,"UTF-8")+"&"
                        +URLEncoder.encode("gender","UTF-8")+"="+ URLEncoder.encode(sgender,"UTF-8");
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
            new backgroundworker().execute();
        }
    }

    private void showUserInfoDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_user_info, null);

        EditText editProfession = view.findViewById(R.id.edit_profession);
        EditText editPob = view.findViewById(R.id.edit_pob);
        EditText editDob = view.findViewById(R.id.edit_dob);
        EditText editTob = view.findViewById(R.id.edit_tob);
        Spinner spinnerGender = view.findViewById(R.id.spinner_gender);

        // Gender Spinner Setup
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Male", "Female"});
        spinnerGender.setAdapter(genderAdapter);

        // Date Picker for DoB
        editDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this,
                    (view1, year, month, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editDob.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Time Picker for ToB
        editTob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(this,
                    (view12, hourOfDay, minute) -> {
                        String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        editTob.setText(time);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true).show();
        });

        // AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Enter Personal Details")
                .setView(view)
                .setPositiveButton("Submit", (dialog, which) -> {
                    // 🟡 Store in Strings
                    sprofession = editProfession.getText().toString().trim();
                    spob = editPob.getText().toString().trim();
                    sdob = editDob.getText().toString().trim();
                    stob = editTob.getText().toString().trim();
                    sgender = spinnerGender.getSelectedItem().toString();

                    // 🟡 Send to Server
                    progressDialog.show();
                    new backgroundworker2().execute();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    public Bitmap getInitialBitmap(String letter, int size, int bgColor, int textColor) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(bgColor);

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw background circle
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);

        // Draw letter
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(size * 0.5f);
        paint.setFakeBoldText(true);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float x = size / 2f;
        float y = size / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2;

        canvas.drawText(letter.toUpperCase(), x, y, paint);

        return bitmap;
    }

    public int generateColorFromLetter(String key) {
        int[] colors = new int[]{
                Color.parseColor("#F44336"), // red
                Color.parseColor("#E91E63"), // pink
                Color.parseColor("#9C27B0"), // purple
                Color.parseColor("#3F51B5"), // indigo
                Color.parseColor("#2196F3"), // blue
                Color.parseColor("#009688"), // teal
                Color.parseColor("#4CAF50"), // green
                Color.parseColor("#FF9800"), // orange
                Color.parseColor("#FF5722"), // deep orange
                Color.parseColor("#795548"), // brown
                Color.parseColor("#607D8B"), // blue grey
        };

        int hash = Math.abs(key.hashCode());
        return colors[hash % colors.length];
    }

    private void intialise() {
        ProfileImage = findViewById(R.id.imageView47);
        Name = findViewById(R.id.textView107);
        Mobile = findViewById(R.id.textView109);
        Mail = findViewById(R.id.textView111);
        Address = findViewById(R.id.textView114);


        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(Profile.this);

    }
}