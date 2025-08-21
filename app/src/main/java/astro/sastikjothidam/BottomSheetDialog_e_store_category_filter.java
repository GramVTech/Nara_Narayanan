package astro.sastikjothidam;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class BottomSheetDialog_e_store_category_filter extends BottomSheetDialogFragment {

    View view;
    Context context;
    String which_area_msg="";
    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"Estore_Product_list_based_on_Category1.php";
    String json_string="";
    List<String> Lcat2_name = new ArrayList<>();
    Map<String,String> Lcat2_map = new HashMap<>();
    ArrayList<String> Lcat1_name = new ArrayList<>();
    Map<String,String> Lcat1_map = new HashMap<>();
    HashSet<String> Lcat1_duplicate_removal = new HashSet<>();
    String scat1_id = "",scat2_id="";
    Spinner category1_spinner;
    Spinner category2_spinner;
    ProgressBar progressBar;

    public BottomSheetDialog_e_store_category_filter(Context context1, ArrayList<String> category1_name,HashMap<String,String> category1_map) {
        Lcat1_map.clear();Lcat1_name.clear();
        context= context1;
        Lcat1_name = category1_name;
        Lcat1_map = category1_map;

        for(String z : Lcat1_name){
            Lcat1_duplicate_removal.add(z);
        }

        Lcat1_name.clear();

        for(String z : Lcat1_duplicate_removal){
            Lcat1_name.add(z);
        }


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.bottom_sheet_category_filter_screen,container,false);

        ImageView close = view.findViewById(R.id.imageView12);
        category1_spinner = view.findViewById(R.id.spinner);
        category2_spinner = view.findViewById(R.id.spinner2);
        Button submit = view.findViewById(R.id.button4);
        progressBar = view.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(context, R.layout.custom_spinner_layout2, Lcat1_name);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category1_spinner.setAdapter(dataAdapter3);

        category1_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    String temp = category1_spinner.getSelectedItem().toString();
                    scat1_id = Lcat1_map.get(temp);
                    progressBar.setVisibility(View.VISIBLE);
                    new backgroundworker().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        category2_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    String temp = category2_spinner.getSelectedItem().toString();
                    scat2_id = Lcat2_map.get(temp);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                E_store_Product_list.passing_data.receive_data(scat1_id,scat2_id);
                new E_store_Product_list.backgroundworker3().execute();
                dismiss();
            }
        });

        return view;
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

                Lcat2_name.clear();Lcat2_map.clear();
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("cat1_id","UTF-8")+"="+URLEncoder.encode(scat1_id,"UTF-8");
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
            progressBar.setVisibility(View.GONE);
            Log.d("RESULT", "" + result);
            int count=0;
            try{
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                while(count < jsonArray.length()){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);
                    Lcat2_name.add(jsonObject1.getString("category2_name"));
                    Lcat2_map.put(jsonObject1.getString("category2_name"),jsonObject1.getString("categroy2_id"));
                    count++;
                }

                if(count>0){
                    progressBar.setVisibility(View.INVISIBLE);
                    ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(context, R.layout.custom_spinner_layout2, Lcat2_name);
                    dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    category2_spinner.setAdapter(dataAdapter4);

                }else{
                    Toast.makeText(context, "No Sub Products", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){

            }
        }
    }

}


