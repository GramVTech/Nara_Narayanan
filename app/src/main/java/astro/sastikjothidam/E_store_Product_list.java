package astro.sastikjothidam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class E_store_Product_list extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"Estore_Product_list.php";
    String json_string="";
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"Estore_Product_list_with_filter.php";
    String json_string2="";
    static StringBuffer sb3 = new StringBuffer();
    static String json_url3 = Url_interface.url+"Estore_Product_list_based_on_Category_1_and2.php";
    static String json_string3="";
    static ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    static List<String> Lproduct_id = new ArrayList<>();
    static List<String> Lproduct_name = new ArrayList<>();
    static List<String> Lproduct_description = new ArrayList<>();
    static List<String> Lproduct_cat1_id = new ArrayList<>();
    static List<String> Lproduct_cat2_id = new ArrayList<>();
    static List<String> Lproduct_image1 = new ArrayList<>();
    static List<String> Lproduct_image2 = new ArrayList<>();
    static List<String> Lproduct_image3 = new ArrayList<>();
    static List<String> Lproduct_video = new ArrayList<>();
    static List<String> Lproduct_price = new ArrayList<>();
    static List<String> Lproduct_offer = new ArrayList<>();
    static List<String> Lproduct_final_price = new ArrayList<>();
    static List<String> Lproduct_review = new ArrayList<>();
    static ArrayList<String> Lcat1_name = new ArrayList<>();
    static HashMap<String,String> Lcat1_map = new HashMap<String,String>();
    static HashMap<String,String> Lcat1_map_reverse = new HashMap<String,String>();
    static CustomAdapter customAdapter;
    static GridView listView;
    AutoCompleteTextView autocompletetextView;
    String selected_text="";
    Intent intent;
    TextView no_of_cart_items;
    static String category1_id="",category2_id="";

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
        setContentView(R.layout.activity_estore_product_list);

        intialise();

        calc();

        progressDialog.show();
        new backgroundworker().execute();

        autocompletetextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                selected_text = selected;
                progressDialog.show();
                new backgroundworker2().execute();
            }
        });

        findViewById(R.id.materialCardView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ASASASAS",""+Centrailzed_Product_Map.Mproduct_name);
                Log.d("ASASASAS",""+Centrailzed_Product_Map.Mproduct_price);
                Log.d("ASASASAS",""+Centrailzed_Product_Map.Mproduct_qty);
                if(Integer.valueOf(no_of_cart_items.getText().toString())>0){
                    Intent intent1 = new Intent(E_store_Product_list.this,E_store_Product_Summary_page.class);
                    startActivity(intent1);
                }else{
                    Toast.makeText(E_store_Product_list.this, "Add Items in Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.filter_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog_e_store_category_filter bottomSheetDialogEStoreCategoryFilter = new BottomSheetDialog_e_store_category_filter(E_store_Product_list.this,Lcat1_name,Lcat1_map_reverse);
                bottomSheetDialogEStoreCategoryFilter.setCancelable(true);
                bottomSheetDialogEStoreCategoryFilter.show(getSupportFragmentManager(), "Dialog");
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

                Lproduct_id.clear();
                Lproduct_name.clear();
                Lproduct_description.clear();
                Lproduct_cat1_id.clear();
                Lproduct_cat2_id.clear();
                Lproduct_image1.clear();
                Lproduct_image2.clear();
                Lproduct_image3.clear();
                Lproduct_video.clear();
                Lproduct_price.clear();
                Lproduct_offer.clear();
                Lproduct_final_price.clear();
                Lproduct_review.clear();

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

                    Lproduct_id.add(jsonObject1.getString("id"));
                    Lproduct_name.add(jsonObject1.getString("product_name"));
                    Lproduct_description.add(jsonObject1.getString("product_description"));
                    Lproduct_cat1_id.add(jsonObject1.getString("categroy1_id"));
                    Lproduct_cat2_id.add(jsonObject1.getString("category2_id"));
                    Lproduct_image1.add(jsonObject1.getString("product_image1"));
                    Lproduct_image2.add(jsonObject1.getString("product_image2"));
                    Lproduct_image3.add(jsonObject1.getString("product_image3"));
                    Lproduct_video.add(jsonObject1.getString("product_video1"));
                    Lproduct_price.add(jsonObject1.getString("amount"));
                    Lproduct_offer.add(jsonObject1.getString("offer_percentage"));
                    Lproduct_final_price.add(jsonObject1.getString("final_price"));
                    Lproduct_review.add(jsonObject1.getString("review"));
                    Lcat1_name.add(jsonObject1.getString("category1_name"));
                    Lcat1_map.put(jsonObject1.getString("categroy1_id"),jsonObject1.getString("category1_name"));
                    Lcat1_map_reverse.put(jsonObject1.getString("category1_name"),jsonObject1.getString("categroy1_id"));

                    count++;
                }
                if(Lproduct_id.size()>0){
                    listView.setAdapter(customAdapter);
                    ArrayAdapter<String> Astrology_class_adapter=new ArrayAdapter<String>(E_store_Product_list.this,android.R.layout.simple_dropdown_item_1line,Lproduct_name);
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

                Lproduct_id.clear();
                Lproduct_name.clear();
                Lproduct_description.clear();
                Lproduct_cat1_id.clear();
                Lproduct_cat2_id.clear();
                Lproduct_image1.clear();
                Lproduct_image2.clear();
                Lproduct_image3.clear();
                Lproduct_video.clear();
                Lproduct_price.clear();
                Lproduct_offer.clear();
                Lproduct_final_price.clear();
                Lproduct_review.clear();

                sb2=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("product_name","UTF-8")+"="+URLEncoder.encode(selected_text,"UTF-8");
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

                    Lproduct_id.add(jsonObject1.getString("id"));
                    Lproduct_name.add(jsonObject1.getString("product_name"));
                    Lproduct_description.add(jsonObject1.getString("product_description"));
                    Lproduct_cat1_id.add(jsonObject1.getString("categroy1_id"));
                    Lproduct_cat2_id.add(jsonObject1.getString("category2_id"));
                    Lproduct_image1.add(jsonObject1.getString("product_image1"));
                    Lproduct_image2.add(jsonObject1.getString("product_image2"));
                    Lproduct_image3.add(jsonObject1.getString("product_image3"));
                    Lproduct_video.add(jsonObject1.getString("product_video1"));
                    Lproduct_price.add(jsonObject1.getString("amount"));
                    Lproduct_offer.add(jsonObject1.getString("offer_percentage"));
                    Lproduct_final_price.add(jsonObject1.getString("final_price"));
                    Lproduct_review.add(jsonObject1.getString("review"));
                    Lcat1_name.add(jsonObject1.getString("category1_name"));
                    Lcat1_map.put(jsonObject1.getString("categroy1_id"),jsonObject1.getString("category1_name"));
                    Lcat1_map_reverse.put(jsonObject1.getString("category1_name"),jsonObject1.getString("categroy1_id"));

                    count++;
                }
                if(Lproduct_name.size()>0){
                    listView.setAdapter(null);
                    customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e){
                Toast.makeText(E_store_Product_list.this, ""+e, Toast.LENGTH_SHORT).show();
                Log.e("ASQWSSDEDSEEDd",""+e);
            }


        }
    }

    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Lproduct_id.size();
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

            view = getLayoutInflater().inflate(R.layout.custom_layout_e_store_product_list, null);

            ImageView product_image = view.findViewById(R.id.imageView15);

            TextView product_name = view.findViewById(R.id.textView21);

            TextView product_price = view.findViewById(R.id.textView23);

            TextView product_final_price = view.findViewById(R.id.textView24);

            TextView product_offer = view.findViewById(R.id.textView29);

            TextView product_desc = view.findViewById(R.id.textView26);

            RatingBar ratingBar = view.findViewById(R.id.ratingBar);

            TextView rating = view.findViewById(R.id.textView30);

            TextView adder = view.findViewById(R.id.textView33);

            CardView qtyer = view.findViewById(R.id.qtyer);

            ImageView minus = view.findViewById(R.id.imageView17);

            ImageView add = view.findViewById(R.id.imageView18);

            TextView qty = view.findViewById(R.id.textView35);

            qtyer.setVisibility(View.GONE);

            try {
                if (Centrailzed_Product_Map.Mproduct_name.get(Lproduct_id.get(i)).equals("")) {

                } else {
                    qty.setText(Centrailzed_Product_Map.Mproduct_qty.get(Lproduct_id.get(i)));
                    adder.setVisibility(View.GONE);
                    qtyer.setVisibility(View.VISIBLE);
                    calc();
                }
            }catch (Exception e){

            }


            adder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adder.setVisibility(View.GONE);
                    qtyer.setVisibility(View.VISIBLE);
                    qty.setText("1");
                    Centrailzed_Product_Map.Mproduct_name.put(Lproduct_id.get(i),Lproduct_name.get(i));
                    Centrailzed_Product_Map.Mproduct_price.put(Lproduct_id.get(i),Lproduct_final_price.get(i));
                    Centrailzed_Product_Map.Mproduct_qty.put(Lproduct_id.get(i),"1");
                    Centrailzed_Product_Map.Mproduct_image.put(Lproduct_id.get(i),Lproduct_image2.get(i));
                    calc();
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String temp = String.valueOf(Integer.parseInt(qty.getText().toString())+1);
                    qty.setText(temp);
                    Centrailzed_Product_Map.Mproduct_name.put(Lproduct_id.get(i),Lproduct_name.get(i));
                    Centrailzed_Product_Map.Mproduct_price.put(Lproduct_id.get(i),Lproduct_final_price.get(i));
                    Centrailzed_Product_Map.Mproduct_qty.put(Lproduct_id.get(i),temp);
                    Centrailzed_Product_Map.Mproduct_image.put(Lproduct_id.get(i),Lproduct_image2.get(i));
                    calc();
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int qt = Integer.parseInt(qty.getText().toString())-1;
                    if(qt<1){
                        qtyer.setVisibility(View.GONE);
                        adder.setVisibility(View.VISIBLE);

                        Centrailzed_Product_Map.Mproduct_name.remove(Lproduct_id.get(i));
                        Centrailzed_Product_Map.Mproduct_price.remove(Lproduct_id.get(i));
                        Centrailzed_Product_Map.Mproduct_qty.remove(Lproduct_id.get(i));
                        Centrailzed_Product_Map.Mproduct_image.remove(Lproduct_id.get(i));

                        calc();

                    }else{
                        String temp = String.valueOf(Integer.parseInt(qty.getText().toString())-1);
                        qty.setText(temp);
                        Centrailzed_Product_Map.Mproduct_qty.put(Lproduct_id.get(i),temp);

                        calc();
                    }
                }
            });

            try {
                    Glide
                            .with(E_store_Product_list.this)
                            .load(Lproduct_image2.get(i))
                            .into(product_image);

                    product_name.setText(Lproduct_name.get(i));
                    product_price.setText("₹ " + Lproduct_price.get(i));
                    product_final_price.setText("₹ " + Lproduct_final_price.get(i));
                    product_offer.setText(Lproduct_offer.get(i) + "% OFF");
                    product_price.setPaintFlags(product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    product_desc.setText(Lproduct_description.get(i));
                    ratingBar.setRating(3.5F);
                    rating.setText(String.valueOf( 3.5));

                } catch (Exception e) {
                    Log.e("AQWERRTT", "" + e);
                }

                return view;
        }


    }

    public void intialise(){

        progressDialog = new ProgressDialog(E_store_Product_list.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(E_store_Product_list.this);

        customAdapter = new CustomAdapter();

        listView = findViewById(R.id.listView);

        autocompletetextView=(AutoCompleteTextView)findViewById(R.id.editTextText);

        intent = getIntent();

        no_of_cart_items = findViewById(R.id.textView28);
    }

    public void calc(){

        no_of_cart_items.setText(String.valueOf(Centrailzed_Product_Map.Mproduct_name.size()));
    }

    public static class passing_data{
        public static void receive_data(String cat1_id, String cat2_id){
            category1_id = cat1_id;
            category2_id = cat2_id;
        }
    }

    public static class backgroundworker3 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url3);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {

                Lproduct_id.clear();
                Lproduct_name.clear();
                Lproduct_description.clear();
                Lproduct_cat1_id.clear();
                Lproduct_cat2_id.clear();
                Lproduct_image1.clear();
                Lproduct_image2.clear();
                Lproduct_image3.clear();
                Lproduct_video.clear();
                Lproduct_price.clear();
                Lproduct_offer.clear();
                Lproduct_final_price.clear();
                Lproduct_review.clear();

                sb3=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("cat1","UTF-8")+"="+URLEncoder.encode(category1_id,"UTF-8")+"&"
                        +URLEncoder.encode("cat2","UTF-8")+"="+ URLEncoder.encode(category2_id,"UTF-8");
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
                JSONObject jsonObject = new JSONObject(json_string3);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                while(count < jsonArray.length()){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);

                    Lproduct_id.add(jsonObject1.getString("id"));
                    Lproduct_name.add(jsonObject1.getString("product_name"));
                    Lproduct_description.add(jsonObject1.getString("product_description"));
                    Lproduct_cat1_id.add(jsonObject1.getString("categroy1_id"));
                    Lproduct_cat2_id.add(jsonObject1.getString("category2_id"));
                    Lproduct_image1.add(jsonObject1.getString("product_image1"));
                    Lproduct_image2.add(jsonObject1.getString("product_image2"));
                    Lproduct_image3.add(jsonObject1.getString("product_image3"));
                    Lproduct_video.add(jsonObject1.getString("product_video1"));
                    Lproduct_price.add(jsonObject1.getString("amount"));
                    Lproduct_offer.add(jsonObject1.getString("offer_percentage"));
                    Lproduct_final_price.add(jsonObject1.getString("final_price"));
                    Lproduct_review.add(jsonObject1.getString("review"));

                    count++;
                }
                if(Lproduct_name.size()>0){
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e){

            }


        }
    }


}