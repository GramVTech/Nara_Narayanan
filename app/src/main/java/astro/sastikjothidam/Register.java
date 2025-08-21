package astro.sastikjothidam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
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
import java.util.List;
import java.util.Locale;

public class Register extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    CardView google_sign_in_button;
    String User_Email = "";
    Spinner Country;
    String[] countries = new String[]{"India","Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine"};
    String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET,
            Manifest.permission.READ_MEDIA_IMAGES};
    int permission_All = 1;
    EasyLocationProvider easyLocationProvider;
    String add="";
    EditText eaddress,Ename,Emobile;
    TextView Textinfotext;
    ProgressDialog progressDialog;
    String Sname="",Smobile="",SmailID="",Saddress="",Scountry="";
    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"Register.php";
    String json_string="";
    SessionMaintance sessionMaintance;
    CardView RegisterSubmit;

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
        setContentView(R.layout.activity_register);
        intialise();
        RegisterSubmit.setVisibility(View.GONE);
        if(!haspermission(this,permissions)){
            ActivityCompat.requestPermissions(this,permissions,permission_All);

        }
        if (ActivityCompat.checkSelfPermission(Register.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }else {
            //getLoc();
        }

        //progressDialog.show();

        google_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((Ename.getText().toString().length()>0)&&(Emobile.getText().toString().length()>0)&&
                        (eaddress.getText().toString().length()>0)&&(User_Email.toString().length()>0)
                &&(User_Email.contains("@"))){

                    Sname = Ename.getText().toString();
                    SmailID = User_Email;
                    Smobile = Emobile.getText().toString();
                    Saddress = eaddress.getText().toString();
                    Scountry = Country.getSelectedItem().toString();

                    Textinfotext.setText("REGISTER HERE");

                    progressDialog.show();
                    new backgroundworker().execute();

                }else{
                    Textinfotext.setText("Fill All The Fields");
                }
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

    }

    void signIn(){

        RegisterSubmit.setVisibility(View.GONE);
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
                    Toast.makeText(this, ""+User_Email, Toast.LENGTH_SHORT).show();
                    RegisterSubmit.setVisibility(View.VISIBLE);
                }

            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }
        }

    }

    public void intialise(){

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        Country = findViewById(R.id.country);
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(Register.this, R.layout.custom_spinner_layout, countries);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Country.setAdapter(dataAdapter3);

        google_sign_in_button = findViewById(R.id.google_sign_in_button);

        eaddress = findViewById(R.id.editTextText3);
        Ename = findViewById(R.id.editTextText);
        Emobile = findViewById(R.id.editTextText2);

        Textinfotext = findViewById(R.id.textView4);

        sessionMaintance = new SessionMaintance(Register.this);

        RegisterSubmit = findViewById(R.id.submit);
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

    public void getLoc(){
        easyLocationProvider = new EasyLocationProvider.Builder(Register.this)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setListener(new EasyLocationProvider.EasyLocationCallback() {
                    @Override
                    public void onGoogleAPIClient(GoogleApiClient googleApiClient, String message) {
                        Log.e("EasyLocationProvider","onGoogleAPIClient: "+message);
                    }
                    @Override
                    public void onLocationUpdated(double latitude, double longitude) {
                        Log.e("EasyLocationProvider","onLocationUpdated:: "+ "Latitude: "+latitude+" Longitude: "+longitude);
                        add = getCompleteAddressString(latitude,longitude);
                        eaddress.setText(add);
                        easyLocationProvider.removeUpdates();
                        getLifecycle().removeObserver(easyLocationProvider);
                        //progressDialog.dismiss();
                    }

                    @Override
                    public void onLocationUpdateRemoved() {
                        Log.e("EasyLocationProvider","onLocationUpdateRemoved");
                    }
                }).build();

        getLifecycle().addObserver(easyLocationProvider);
    }

    @Override
    protected void onDestroy() {
        easyLocationProvider.removeUpdates();
        getLifecycle().removeObserver(easyLocationProvider);
        super.onDestroy();
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
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
                String post_data = URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(Smobile,"UTF-8")+"&"
                        +URLEncoder.encode("mail","UTF-8")+"="+ URLEncoder.encode(SmailID,"UTF-8")+"&"
                        +URLEncoder.encode("name","UTF-8")+"="+ URLEncoder.encode(Sname,"UTF-8")+"&"
                        +URLEncoder.encode("address","UTF-8")+"="+ URLEncoder.encode(Saddress,"UTF-8")+"&"
                        +URLEncoder.encode("country","UTF-8")+"="+ URLEncoder.encode(Scountry,"UTF-8");
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
                Textinfotext.setText("REGISTER HERE");
                Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                sessionMaintance.set_user_mail(User_Email);
                Intent intent = new Intent(Register.this,Main_menu.class);
                startActivity(intent);
            }else{
                Textinfotext.setText(result);
                Textinfotext.setVisibility(View.VISIBLE);
            }
        }
    }
}