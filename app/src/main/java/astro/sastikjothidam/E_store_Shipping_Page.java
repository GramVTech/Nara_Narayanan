package astro.sastikjothidam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//import nl.dionsegijn.konfetti.KonfettiView;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import nl.dionsegijn.konfetti.compose.KonfettiViewKt;
//import nl.dionsegijn.konfetti.models.Shape;
//import nl.dionsegijn.konfetti.models.Size;

public class E_store_Shipping_Page extends AppCompatActivity implements PaymentResultWithDataListener {

    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"E_store_Promo_Code_Check.php";
    String json_string="";
    ProgressDialog progressDialog;
    KonfettiViewKt konfettiView;
    Spinner Country;
    Spinner State,District;
    String[] districts;
    EditText editText_state,editText_district,epromo_code,editTextText4_mob;
    TextView T_gross_amt,T_final_amt,T_promo_amt,T_shipping_area;
    Intent intent;
    List<String> Lpromo_code_id = new ArrayList<>();
    List<String> Lpromo_code_name = new ArrayList<>();
    List<String> Lpromo_code_offer = new ArrayList<>();
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"Estore_Product_Shipping_Charge.php";
    String json_string2="";
    String shipping_area_txt="";
    List<String> L_shipping_id = new ArrayList<>();
    List<String> L_shipping_name = new ArrayList<>();
    List<String> L_shipping_amount = new ArrayList<>();
    EditText eaddress;
    String saddress="",sstate="",sdistrict="",smobile="",sgross_amt="",sfinal_amt="",
            spromo_code_amount="",spromo_code_id="",
            sshipping_id="",sshipping_amt="",
            sorder_status="",
            suser_id="",sorder_id_string="",
            sproduct_id="",sproduct_qty="",sproduct_price="";

    List<String> Lproduct_id = new ArrayList<>();
    List<String> Lproduct_qty = new ArrayList<>();
    List<String> Lproduct_final_price = new ArrayList<>();
    SessionMaintance sessionMaintance;

    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"Estore_product_buying_insert.php";
    String json_string3="";

    String order_id="",sdesc="";
    String payment_id="",payment_signature="",payment_status="";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    String key_secret="8wivMe89eJvtU2jjaZRvLwcQ";

    StringBuffer sb33 = new StringBuffer();
    String json_url33 = Url_interface.url+"updation_of_payment_e_store.php";
    String json_string33;

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
        setContentView(R.layout.activity_estore_shipping_page);

        intialise();

        T_gross_amt.setText("+ "+intent.getStringExtra("gross_price")+" Rs");
        T_final_amt.setText("YOUR FINAL PRICE - "+intent.getStringExtra("gross_price")+" Rs");

        Country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {


                    shipping_area_txt = Country.getSelectedItem().toString();
                    if(shipping_area_txt.equals("Select Your Country")){
                        //
                        Log.e("ASAS","Please select your country");
                    }else {
                        progressDialog.show();
                        new backgroundworker2().execute();
                    }

                    if (Country.getSelectedItem().toString().equals("India")) {

                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout3, getResources().getStringArray(R.array.array_indian_states));
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        State.setAdapter(dataAdapter4);

                        editText_state.setVisibility(View.INVISIBLE);
                        editText_district.setVisibility(View.INVISIBLE);
                        State.setVisibility(View.VISIBLE);
                        District.setVisibility(View.VISIBLE);

                    } else {
                        editText_state.setVisibility(View.VISIBLE);
                        editText_district.setVisibility(View.VISIBLE);
                        State.setVisibility(View.INVISIBLE);
                        District.setVisibility(View.INVISIBLE);
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                try {
                    if (State.getSelectedItem().toString().equals("Select Your State")) {

                    } else if (State.getSelectedItem().toString().equals("Andaman and Nicobar Islands")) {
                        districts = getResources().getStringArray(R.array.array_andaman_nicobar_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Andhra Pradesh")) {
                        districts = getResources().getStringArray(R.array.array_andhra_pradesh_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Arunachal Pradesh")) {
                        districts = getResources().getStringArray(R.array.array_arunachal_pradesh_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Assam")) {
                        districts = getResources().getStringArray(R.array.array_assam_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Bihar")) {
                        districts = getResources().getStringArray(R.array.array_bihar_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Chandigarh")) {
                        districts = getResources().getStringArray(R.array.array_chandigarh_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Chhattisgarh")) {
                        districts = getResources().getStringArray(R.array.array_chhattisgarh_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Dadra and Nagar Haveli")) {
                        districts = getResources().getStringArray(R.array.array_dadra_nagar_haveli_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Daman and Diu")) {
                        districts = getResources().getStringArray(R.array.array_daman_diu_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Delhi")) {
                        districts = getResources().getStringArray(R.array.array_delhi_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Goa")) {
                        districts = getResources().getStringArray(R.array.array_goa_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Gujarat")) {
                        districts = getResources().getStringArray(R.array.array_gujarat_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Haryana")) {
                        districts = getResources().getStringArray(R.array.array_haryana_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Himachal Pradesh")) {
                        districts = getResources().getStringArray(R.array.array_himachal_pradesh_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Jammu and Kashmir")) {
                        districts = getResources().getStringArray(R.array.array_jammu_kashmir_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Jharkhand")) {
                        districts = getResources().getStringArray(R.array.array_jharkhand_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Karnataka")) {
                        districts = getResources().getStringArray(R.array.array_karnataka_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Kerala")) {
                        districts = getResources().getStringArray(R.array.array_kerala_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Ladakh")) {
                        districts = getResources().getStringArray(R.array.array_ladakh_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Lakshadweep")) {
                        districts = getResources().getStringArray(R.array.array_lakshadweep_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Madhya Pradesh")) {
                        districts = getResources().getStringArray(R.array.array_madhya_pradesh_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Maharashtra")) {
                        districts = getResources().getStringArray(R.array.array_maharashtra_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Manipur")) {
                        districts = getResources().getStringArray(R.array.array_manipur_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Meghalaya")) {
                        districts = getResources().getStringArray(R.array.array_meghalaya_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Mizoram")) {
                        districts = getResources().getStringArray(R.array.array_mizoram_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Nagaland")) {
                        districts = getResources().getStringArray(R.array.array_nagaland_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Orissa")) {
                        districts = getResources().getStringArray(R.array.array_odisha_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Puducherry")) {
                        districts = getResources().getStringArray(R.array.array_puducherry_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Punjab")) {
                        districts = getResources().getStringArray(R.array.array_punjab_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Rajasthan")) {
                        districts = getResources().getStringArray(R.array.array_rajasthan_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Sikkim")) {
                        districts = getResources().getStringArray(R.array.array_sikkim_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Tamil Nadu")) {
                        districts = getResources().getStringArray(R.array.array_tamil_nadu_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Telangana")) {
                        districts = getResources().getStringArray(R.array.array_telangana_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Tripura")) {
                        districts = getResources().getStringArray(R.array.array_tripura_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Uttarakhand")) {
                        districts = getResources().getStringArray(R.array.array_uttarakhand_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("Uttar Pradesh")) {
                        districts = getResources().getStringArray(R.array.array_uttar_pradesh_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    } else if (State.getSelectedItem().toString().equals("West Bengal")) {
                        districts = getResources().getStringArray(R.array.array_west_bengal_districts);
                        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout4, districts);
                        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        District.setAdapter(dataAdapter4);
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                new backgroundworker().execute();

            }
        });

        findViewById(R.id.final_checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Country.getSelectedItem().toString().equals("Select Your Country")||editTextText4_mob.getText().toString().length()<10
                    ||eaddress.getText().toString().length()<=0){

                    Toast.makeText(E_store_Shipping_Page.this, "Some Fields Missing", Toast.LENGTH_SHORT).show();

                }else{

                            saddress = eaddress.getText().toString();

                            if(Country.getSelectedItem().toString().equals("India")){
                                sstate = State.getSelectedItem().toString();
                                sdistrict = District.getSelectedItem().toString();
                            }else{
                                sstate = editText_state.getText().toString();
                                sdistrict = editText_district.getText().toString();
                            }

                            smobile = editTextText4_mob.getText().toString();

//                            sgross_amt = T_gross_amt.getText().toString();
//
//                            sfinal_amt = T_final_amt.getText().toString();

                            if(Lpromo_code_id.size()>0){
                                int temp = Integer.valueOf(intent.getStringExtra("gross_price"));
                                int temp2 = temp*Integer.parseInt(Lpromo_code_offer.get(0))/100;
                                spromo_code_amount = String.valueOf(temp2);
                                spromo_code_id = Lpromo_code_id.get(0);
                            }else{
                                spromo_code_amount="0";
                                spromo_code_id = "0";
                            }

                            sshipping_id = L_shipping_id.get(0);
                            sshipping_amt = L_shipping_amount.get(0);
                            sorder_status = "Pending";

                            suser_id = sessionMaintance.get_user_mail();

                            Long tsLong = System.currentTimeMillis()/1000;
                            String ts = tsLong.toString();
                            Random rand = new Random();
                            int min =1000, max= 9999;
                            int randomNum = rand.nextInt((max - min) + 1) + min;
                            sorder_id_string =  "H"+String.valueOf(randomNum)+ts;

                            String temp1 = "",temp2="",temp3="";

                            for(String z : Lproduct_id){
                                temp1 = temp1.concat(z)+"###";
                            }
                            for(String z : Lproduct_qty){
                                temp2 = temp2.concat(z)+"###";
                            }
                            for(String z : Lproduct_final_price){
                                temp3 = temp3.concat(z)+"###";
                            }

                            sproduct_id = temp1.substring(0,temp1.length()-3);
                            sproduct_qty = temp2.substring(0,temp2.length()-3);
                            sproduct_price = temp3.substring(0,temp3.length()-3);

                            if(sstate.length()>0&&sdistrict.length()>0) {
                                progressDialog.show();
                                new backgroundworker3().execute();
                            }



                }


            }
        });
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
                String post_data = URLEncoder.encode("user_mail","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("promo_code_id","UTF-8")+"="+ URLEncoder.encode(spromo_code_id,"UTF-8")+"&"
                        +URLEncoder.encode("promo_code_amount","UTF-8")+"="+ URLEncoder.encode(spromo_code_amount,"UTF-8")+"&"
                        +URLEncoder.encode("shipping_id","UTF-8")+"="+ URLEncoder.encode(sshipping_id,"UTF-8")+"&"
                        +URLEncoder.encode("shipping_amount","UTF-8")+"="+ URLEncoder.encode(sshipping_amt,"UTF-8")+"&"
                        +URLEncoder.encode("product_amount","UTF-8")+"="+ URLEncoder.encode(sgross_amt,"UTF-8")+"&"
                        +URLEncoder.encode("final_amount","UTF-8")+"="+ URLEncoder.encode(sfinal_amt,"UTF-8")+"&"
                        +URLEncoder.encode("delivery_address","UTF-8")+"="+ URLEncoder.encode(saddress,"UTF-8")+"&"
                        +URLEncoder.encode("country","UTF-8")+"="+ URLEncoder.encode(Country.getSelectedItem().toString(),"UTF-8")+"&"
                        +URLEncoder.encode("state","UTF-8")+"="+ URLEncoder.encode(sstate,"UTF-8")+"&"
                        +URLEncoder.encode("district","UTF-8")+"="+ URLEncoder.encode(sdistrict,"UTF-8")+"&"
                        +URLEncoder.encode("delivery_mobile_number","UTF-8")+"="+ URLEncoder.encode(smobile,"UTF-8")+"&"
                        +URLEncoder.encode("order_id","UTF-8")+"="+ URLEncoder.encode(sorder_id_string,"UTF-8")+"&"
                        +URLEncoder.encode("product_id","UTF-8")+"="+ URLEncoder.encode(sproduct_id,"UTF-8")+"&"
                        +URLEncoder.encode("quantity","UTF-8")+"="+ URLEncoder.encode(sproduct_qty,"UTF-8")+"&"
                        +URLEncoder.encode("amount","UTF-8")+"="+ URLEncoder.encode(sproduct_price,"UTF-8");
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
            progressDialog.dismiss();
            int count=0;
            Log.d("RESULT", "" + result);
            try {
                JSONObject jsonObject2 = new JSONObject(json_string3);
                order_id = jsonObject2.getString("id");
                startPayment();
            } catch (Exception e) {
                e.printStackTrace();
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
                Lpromo_code_id.clear();Lpromo_code_name.clear();Lpromo_code_offer.clear();
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("promo_code","UTF-8")+"="+URLEncoder.encode(epromo_code.getText().toString(),"UTF-8");
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

                    Lpromo_code_id.add(jsonObject1.getString("id"));
                    Lpromo_code_name.add(jsonObject1.getString("promo_code"));
                    Lpromo_code_offer.add(jsonObject1.getString("offer_percentage"));

                    count++;
                }

                if(Lpromo_code_id.size()>0){
                    int temp = Integer.valueOf(intent.getStringExtra("gross_price"));
                    int temp2 = temp*Integer.parseInt(Lpromo_code_offer.get(0))/100;
                    T_promo_amt.setText("- "+String.valueOf(temp2)+" Rs ("+Lpromo_code_offer.get(0)+"%)");
                    calc_total();
                    DisplayMetrics display = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(display);
//                    konfettiView.build()
//                            .addColors(Color.parseColor("#192DE5"), Color.parseColor("#E52019"), Color.parseColor("#E519DE"))
//                            .setDirection(0.0, 359.0)
//                            .setSpeed(1f, 5f)
//                            .setFadeOutEnabled(true)
//                            .setTimeToLive(3000L)
//                            .addShapes(Shape.RECT, Shape.CIRCLE)
//                            .addSizes(new Size(12, 5))
//                            .setPosition(-50f, display.widthPixels + 50f, -50f, -50f)
//                            .streamFor(300, 3000L);
                }else{
                    Toast.makeText(E_store_Shipping_Page.this, "Invalid Promo Code.!", Toast.LENGTH_SHORT).show();
                    calc_total();
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
                L_shipping_id.clear();L_shipping_amount.clear();L_shipping_name.clear();
                sb2=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("shipping_area","UTF-8")+"="+URLEncoder.encode(shipping_area_txt,"UTF-8");
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

                    L_shipping_id.add(jsonObject1.getString("id"));
                    L_shipping_name.add(jsonObject1.getString("loaction"));
                    L_shipping_amount.add(jsonObject1.getString("amount"));

                    count++;
                }
                if(L_shipping_id.size()>0){
                    calc_total();
                }else{

                }
            }catch (Exception e){
                Toast.makeText(E_store_Shipping_Page.this, ""+e, Toast.LENGTH_SHORT).show();
                Log.e("ASQWSSDEDSEEDd",""+e);
            }


        }
    }

    public void calc_total(){
        int temp2 = 0;
        int temp1 = Integer.parseInt(intent.getStringExtra("gross_price"));

        if(Lpromo_code_id.size()>0){
            temp2 = temp1*Integer.parseInt(Lpromo_code_offer.get(0))/100;
            T_promo_amt.setText("- "+String.valueOf(temp2)+" Rs ("+Lpromo_code_offer.get(0)+"%)");
        }else{
            temp2 = 0;
            T_promo_amt.setText("- "+"0"+" Rs");
        }

        int temp3 = Integer.parseInt(L_shipping_amount.get(0));
        int temp4 = (temp1+temp3) - temp2;
        T_gross_amt.setText("+ "+intent.getStringExtra("gross_price")+" Rs");
        sgross_amt = String.valueOf(intent.getStringExtra("gross_price"));
        sfinal_amt = String.valueOf(temp4);
        T_final_amt.setText("YOUR FINAL PRICE - "+String.valueOf(temp4)+" Rs");
        T_shipping_area.setText("+ "+L_shipping_amount.get(0)+" Rs");

    }

    public void intialise(){

        sessionMaintance = new SessionMaintance(E_store_Shipping_Page.this);

//        konfettiView = findViewById(R.id.viewKonfetti);

        Country = findViewById(R.id.country);
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(E_store_Shipping_Page.this, R.layout.custom_spinner_layout, Centrailzed_Product_Map.countries);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Country.setAdapter(dataAdapter3);

        State = findViewById(R.id.state);

        District = findViewById(R.id.district);

        editText_state = findViewById(R.id.editTextText4_state);
        editText_district = findViewById(R.id.editTextText4_district);

        State.setVisibility(View.INVISIBLE);
        District.setVisibility(View.INVISIBLE);

        T_gross_amt = findViewById(R.id.textView44);
        T_final_amt = findViewById(R.id.textView47);

        intent = getIntent();

        progressDialog = new ProgressDialog(E_store_Shipping_Page.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        epromo_code = findViewById(R.id.editTextText4);

        T_promo_amt = findViewById(R.id.textView46);

        T_shipping_area = findViewById(R.id.textView45);

        eaddress = findViewById(R.id.editTextText5);

        editTextText4_mob = findViewById(R.id.editTextText4_mob);

        for (Map.Entry<String,String> entry : Centrailzed_Product_Map.Mproduct_qty.entrySet()){
            Lproduct_id.add(entry.getKey());
            Lproduct_qty.add(entry.getValue());
        }

        for (Map.Entry<String,String> entry : Centrailzed_Product_Map.Mproduct_price.entrySet()){
            Lproduct_final_price.add(entry.getValue());
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
                new backgroundworker33().execute();
            }else{
                payment_status = "Failure";
                progressDialog.show();
                new backgroundworker33().execute();
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
        new backgroundworker33().execute();
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

    public class backgroundworker33 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url33);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb33=new StringBuffer();
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
                while((json_string33=bufferedReader.readLine())!=null)
                {
                    sb33.append(json_string33+"\n");
                    Log.d("json_string123",""+json_string33);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb33.toString());
                return sb33.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){

            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string33 = result;
            progressDialog.dismiss();
            Log.d("RESULT1234", "" + json_string33);
            if(json_string33.equals("1")&&payment_status.equals("Success")){
                Intent intent = new Intent(E_store_Shipping_Page.this,Main_menu.class);
                Toast.makeText(E_store_Shipping_Page.this, "Payment is Success", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }else if(payment_status.equals("Failure")){
                Intent intent = new Intent(E_store_Shipping_Page.this,Main_menu.class);
                startActivity(intent);
            }else if(payment_status.equals("Success")){
                Intent intent = new Intent(E_store_Shipping_Page.this,Main_menu.class);
                Toast.makeText(E_store_Shipping_Page.this, "Payment is Success", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }

        }
    }
}

