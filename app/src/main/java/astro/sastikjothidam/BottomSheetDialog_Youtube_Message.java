package astro.sastikjothidam;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;

public class BottomSheetDialog_Youtube_Message extends BottomSheetDialogFragment {

    View view;
    Context context;
    String which_area_msg="";
    StringBuffer sb = new StringBuffer();
    String json_url1 = Url_interface.url+"message_view_for_video.php";
    ProgressDialog progressDialog;
    String json_string="";
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"message_post_for_video.php";
    String json_string2="";
    List<String> Lmsg_id = new ArrayList<>();
    List<String> Lmsg_by = new ArrayList<>();
    List<String> Lmsg_value = new ArrayList<>();
    ListView listView;
    CustomAdapter customAdapter;
    String VideoId="";
    String Comment_msg="";

    public BottomSheetDialog_Youtube_Message(Context context1, String Area_of_Message, String videoid) {
        context= context1;
        which_area_msg = Area_of_Message;
        VideoId = videoid;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.bottom_sheet_comments_screen,container,false);
        ImageView close = view.findViewById(R.id.imageView12);
        EditText comment = view.findViewById(R.id.editTextTextPersonName11);
        listView = view.findViewById(R.id.listView);
        Button submit = view.findViewById(R.id.button2);
        //progressDialog.show();
        new backgroundworker().execute();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment_msg = comment.getText().toString();
                new backgroundworker2().execute();
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
                Lmsg_id.clear();
                Lmsg_by.clear();
                Lmsg_value.clear();

                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(context);
                String post_data = URLEncoder.encode("msg_area","UTF-8")+"="+URLEncoder.encode(which_area_msg,"UTF-8")+"&"
                        +URLEncoder.encode("mobile","UTF-8")+"="+ URLEncoder.encode(status.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("video_id","UTF-8")+"="+ URLEncoder.encode(VideoId,"UTF-8");
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
            int count=0;
            try{
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                while(count < jsonArray.length()){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);

                    Lmsg_id.add(jsonObject1.getString("id"));
                    Lmsg_by.add(jsonObject1.getString("user_name"));
                    Lmsg_value.add(jsonObject1.getString("msg"));

                    count++;
                }
                if(Lmsg_id.size()>0){
                    customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e){

            }
        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lmsg_id.size();
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

            view = getLayoutInflater().inflate(R.layout.bottom_sheet_inside_youtube_player_message, null);

            TextView tuser_name = view.findViewById(R.id.textView65);

            TextView tmsg = view.findViewById(R.id.textView50);

            tuser_name.setText(Lmsg_by.get(i));

            tmsg.setText(Lmsg_value.get(i));

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
                sb2=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(context);
                String post_data = URLEncoder.encode("msg_area","UTF-8")+"="+URLEncoder.encode(which_area_msg,"UTF-8")+"&"
                        +URLEncoder.encode("mobile","UTF-8")+"="+ URLEncoder.encode(status.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("video_id","UTF-8")+"="+ URLEncoder.encode(VideoId,"UTF-8")+"&"
                        +URLEncoder.encode("msg","UTF-8")+"="+ URLEncoder.encode(Comment_msg,"UTF-8");
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
            Log.d("RESULT", "" + result);
            new backgroundworker().execute();
        }
    }
}


