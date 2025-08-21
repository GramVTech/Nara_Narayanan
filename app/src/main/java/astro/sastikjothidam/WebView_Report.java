package astro.sastikjothidam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class WebView_Report extends AppCompatActivity {

    StringBuffer sb3 = new StringBuffer();
    String json_url3 = "";
    String json_string3="";
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    Intent intent;
    private WebView myWebView;
    String cloud_id= "";
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
        setContentView(R.layout.activity_web_view_report);

        intialise();

        if(intent.getStringExtra("type").equals("Member")){
            json_url3 = Url_interface.url+"horo_member_report_update.php";
        }else if(intent.getStringExtra("type").equals("Student")){
            json_url3 = Url_interface.url+"horo_student_report_update.php";
        }

        new backgroundworker3().execute();
    }

    public void intialise(){

        progressDialog = new ProgressDialog(WebView_Report.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(WebView_Report.this);

        intent = getIntent();

        cloud_id = intent.getStringExtra("id");

        myWebView = findViewById(R.id.webview);

        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });


        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.addJavascriptInterface(new WebAppInterface(), "AndroidInterface");

        myWebView.clearCache(true);
        myWebView.clearHistory();
        myWebView.clearFormData();
        Log.e("ASAS",""+"https://astrochinnaraj.net/1_astro_software/special_report.php?" +
                "date="+ intent.getStringExtra("date")
                +"&time="+intent.getStringExtra("time")
                +"&type="+intent.getStringExtra("type")
                +"&lat="+intent.getStringExtra("lat")
                +"&lon="+intent.getStringExtra("lon")
                +"&tz="+intent.getStringExtra("tz")
                +"&cloud_id="+cloud_id);

        myWebView.loadUrl("https://astrochinnaraj.net/1_astro_software/special_report.php?date="+intent.getStringExtra("date")+"&time="+intent.getStringExtra("time")+"&type="+intent.getStringExtra("type")+"&lat="+intent.getStringExtra("lat")+"&lon="+intent.getStringExtra("lon")+"&tz="+intent.getStringExtra("tz")+"&mail_id="+sessionMaintance.get_user_mail()+"&cloud_id="+cloud_id);

    }

    public class WebAppInterface {
        @JavascriptInterface
        public void downloadBase64Pdf(String base64Data, String filename) {
            try {
                byte[] pdfAsBytes = Base64.decode(base64Data.split(",")[1], Base64.DEFAULT);
                File file = new File(getExternalFilesDir(null), filename + ".pdf");

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(pdfAsBytes);
                fos.close();

                runOnUiThread(() -> {
                    Toast.makeText(WebView_Report.this, "PDF saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    sharePdfViaWhatsApp(file);  // 👈 Trigger share after saving
                });

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(WebView_Report.this, "Failed to save PDF", Toast.LENGTH_SHORT).show());
            }
        }
    }

    private void sharePdfViaWhatsApp(File file) {
        Uri uri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".provider",
                file
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setPackage("com.whatsapp");  // Optional: target only WhatsApp
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(shareIntent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
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
                String post_data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(intent.getStringExtra("date"),"UTF-8")+"&"
                        +URLEncoder.encode("time","UTF-8")+"="+ URLEncoder.encode(intent.getStringExtra("time"),"UTF-8")+"&"
                        +URLEncoder.encode("mobile","UTF-8")+"="+ URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("type","UTF-8")+"="+ URLEncoder.encode(intent.getStringExtra("type"),"UTF-8")+"&"
                        +URLEncoder.encode("cloud_id","UTF-8")+"="+ URLEncoder.encode(cloud_id,"UTF-8");
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
            int count=0;
        }
    }
}