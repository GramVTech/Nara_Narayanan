package astro.sastikjothidam;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Order_summary_Sub extends AppCompatActivity {

    Intent intent;
    List<String> Lproduct_name = new ArrayList<>();
    List<String> Lproduct_image = new ArrayList<>();
    List<String> Lqty = new ArrayList<>();
    List<String> Lqty_amount = new ArrayList<>();
    CustomAdapter customAdapter;
    ListView listView;


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
        setContentView(R.layout.activity_order_summary_sub);
        intialise();

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat insetsController =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());

        // Hide both status bar and navigation bar
        insetsController.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());

        // Optional: Make them re-appear with swipe
        insetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        int count =0;
        try{
            JSONArray jsonArray = new JSONArray(intent.getStringExtra("sub_order_details"));
            while(count < jsonArray.length()){
                JSONObject jsonObject1 = jsonArray.getJSONObject(count);

                Lproduct_name.add(jsonObject1.getString("product_name"));
                Lproduct_image.add(jsonObject1.getString("product_image1"));
                Lqty_amount.add(jsonObject1.getString("qty_amount"));
                Lqty.add(jsonObject1.getString("quantity"));

                count++;
            }
            if(Lproduct_image.size()>0){
                listView.setAdapter(customAdapter);
            }
        }catch (Exception e){

        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lproduct_image.size();
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

            view = getLayoutInflater().inflate(R.layout.custom_layout_order_product_details, null);

            TextView tproduct_name = view.findViewById(R.id.textView121);
            TextView tquantity = view.findViewById(R.id.textView133);
            TextView tamount = view.findViewById(R.id.textView135);
            ImageView imageView = view.findViewById(R.id.imageView49);

            tproduct_name.setText("PRODUCT NAME : " +Lproduct_name.get(i));
            tquantity.setText("QUANTITY : "+Lqty.get(i));
            tamount.setText(Lqty_amount.get(i));
            Glide.with(Order_summary_Sub.this)
                    .load(Lproduct_image.get(i))
                    .into(imageView);
            return view;
        }
    }

    public void intialise(){
        intent = getIntent();
        customAdapter = new CustomAdapter();
        listView = findViewById(R.id.listView);
        listView.setDivider(null);

    }
}