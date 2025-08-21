package astro.sastikjothidam;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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

public class MainActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    CardView google_sign_in_button;
    String User_Email = "";

    String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET,
    Manifest.permission.READ_MEDIA_IMAGES};

    int permission_All = 1;

    SessionMaintance sessionMaintance;

    TextView Textinfotext;

    ProgressDialog progressDialog;

    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"Login.php";
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
        setContentView(R.layout.activity_main);
        intialise();
        if(!haspermission(this,permissions)){
            ActivityCompat.requestPermissions(this,permissions,permission_All);

        }

        google_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        findViewById(R.id.google_sign_in_button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionMaintance.set_user_mail("santhoshgopal32@gmail.com");
                Intent intent = new Intent(MainActivity.this,Main_menu.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.cardView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.textView7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(Url_interface.url+"Privacy_policy.html"));
                startActivity(viewIntent);
            }
        });

        findViewById(R.id.google_sign_in_button1).setVisibility(GONE);
    }

    public static boolean haspermission(Context context, String... permissions) {
        if((Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)&&(context!=null)&&(permissions!=null))
        {
            for(String temp : permissions)
                if(ActivityCompat.checkSelfPermission(context,temp)!= PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
        }
        return true;
    }

    void signIn(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
            }
        });

        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                if(acct!=null){
                    String personEmail = acct.getEmail();
                    User_Email = personEmail;
                    if(User_Email.length()>0&&User_Email.contains("@")) {
                        progressDialog.show();
                        new backgroundworker().execute();
                    }
                    Toast.makeText(this, ""+User_Email, Toast.LENGTH_SHORT).show();
                }

            } catch (ApiException e) {
                Log.e("ASASASASASASA",""+e.toString());
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }
        }

    }

    public void intialise(){

        google_sign_in_button = findViewById(R.id.google_sign_in_button);

        Textinfotext = findViewById(R.id.textView3);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(MainActivity.this);

    }

    public class backgroundworker extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url);
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
                String post_data = URLEncoder.encode("mail","UTF-8")+"="+URLEncoder.encode(User_Email,"UTF-8");
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
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string = result;
            progressDialog.dismiss();
            int count=0;
            if(result.equals("Success")){
                Textinfotext.setText("EXISTING USER LOGIN BY HERE");
                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                sessionMaintance.set_user_mail(User_Email);
                Intent intent = new Intent(MainActivity.this,Main_menu.class);
                startActivity(intent);
            }else{
                Textinfotext.setText(result);
                Textinfotext.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}