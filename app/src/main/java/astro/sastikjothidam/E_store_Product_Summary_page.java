package astro.sastikjothidam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class E_store_Product_Summary_page extends AppCompatActivity {

    CustomAdapter customAdapter;
    ListView listView;

    List<String> Lproduct_id = new ArrayList<>();
    List<String> Lproduct_name = new ArrayList<>();
    List<String> Lproduct_image2 = new ArrayList<>();
    List<String> Lproduct_qty = new ArrayList<>();
    List<String> Lproduct_final_price = new ArrayList<>();
    TextView price;
    TextView no_of_cart_items;
    int temp =0;

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
        setContentView(R.layout.activity_estore_product_summary_page);

        intialise();

        findViewById(R.id.materialCardView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(E_store_Product_Summary_page.this,E_store_Shipping_Page.class);
                intent.putExtra("gross_price",String.valueOf(temp));
                startActivity(intent);
            }
        });


        listView.setAdapter(customAdapter);

    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lproduct_name.size();
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

            if (view != null) {
                return view;
            }

            view = getLayoutInflater().inflate(R.layout.custom_layout_e_store_product_summary_page, null);

            TextView product_name = view.findViewById(R.id.textView21);
            TextView product_final_price = view.findViewById(R.id.textView24);
            TextView product_qty = view.findViewById(R.id.textView26);
            TextView product_gross_price = view.findViewById(R.id.textView28);
            TextView card_qty_adjuster = view.findViewById(R.id.textView35);
            ImageView minus = view.findViewById(R.id.imageView17);
            ImageView add = view.findViewById(R.id.imageView18);
            ImageView product_image = view.findViewById(R.id.imageView15);

            Glide
                    .with(E_store_Product_Summary_page.this)
                    .load(Lproduct_image2.get(i))
                    .into(product_image);

            product_name.setText(Lproduct_name.get(i));
            product_final_price.setText(Lproduct_final_price.get(i));
            product_qty.setText(Lproduct_qty.get(i));
            product_gross_price.setText(String.valueOf(Integer.valueOf(Lproduct_final_price.get(i))*Integer.valueOf(Lproduct_qty.get(i))));
            card_qty_adjuster.setText(Lproduct_qty.get(i));

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String temp = String.valueOf(Integer.parseInt(card_qty_adjuster.getText().toString())+1);
                    card_qty_adjuster.setText(temp);
                    product_qty.setText(temp);
                    product_gross_price.setText(String.valueOf(Integer.valueOf(product_final_price.getText().toString()) * Integer.valueOf(product_qty.getText().toString())));
                    Centrailzed_Product_Map.Mproduct_name.put(Lproduct_id.get(i),Lproduct_name.get(i));
                    Centrailzed_Product_Map.Mproduct_price.put(Lproduct_id.get(i),Lproduct_final_price.get(i));
                    Centrailzed_Product_Map.Mproduct_qty.put(Lproduct_id.get(i),temp);
                    Centrailzed_Product_Map.Mproduct_image.put(Lproduct_id.get(i),Lproduct_image2.get(i));
                    calc();
                    gross_calc();
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int qt = Integer.parseInt(card_qty_adjuster.getText().toString())-1;
                    if(qt<1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(E_store_Product_Summary_page.this);
                        builder.setMessage(Html.fromHtml("<font color='#e61e4f'>"+"Are you sure, you want to remove this product ?"+"</font>"));
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setTitle("REMOVE PRODUCT");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                            product_qty.setText("0");
                            product_gross_price.setText(String.valueOf(Integer.valueOf(product_final_price.getText().toString()) * Integer.valueOf(product_qty.getText().toString())));
                            Centrailzed_Product_Map.Mproduct_name.remove(Lproduct_id.get(i));
                            Centrailzed_Product_Map.Mproduct_price.remove(Lproduct_id.get(i));
                            Centrailzed_Product_Map.Mproduct_qty.remove(Lproduct_id.get(i));
                            Centrailzed_Product_Map.Mproduct_image.remove(Lproduct_id.get(i));
                            card_qty_adjuster.setText("0");
                            calc();
                            gross_calc();
                        });
                        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                            dialog.cancel();
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else{
                        String temp = String.valueOf(Integer.parseInt(card_qty_adjuster.getText().toString())-1);
                        card_qty_adjuster.setText(temp);
                        product_qty.setText(temp);
                        product_gross_price.setText(String.valueOf(Integer.valueOf(product_final_price.getText().toString()) * Integer.valueOf(product_qty.getText().toString())));
                        Centrailzed_Product_Map.Mproduct_name.put(Lproduct_id.get(i),Lproduct_name.get(i));
                        Centrailzed_Product_Map.Mproduct_price.put(Lproduct_id.get(i),Lproduct_final_price.get(i));
                        Centrailzed_Product_Map.Mproduct_qty.put(Lproduct_id.get(i),temp);
                        Centrailzed_Product_Map.Mproduct_image.put(Lproduct_id.get(i),Lproduct_image2.get(i));
                        calc();
                        gross_calc();
                    }
                }
            });


            return view;

        }


    }

    public void calc(){
        no_of_cart_items.setText(String.valueOf(Centrailzed_Product_Map.Mproduct_name.size()));
    }

    public void gross_calc(){

        temp =0;

        List<String> Lproduct_qty_temp = new ArrayList<>();
        List<String> Lproduct_final_price_temp = new ArrayList<>();

        for (Map.Entry<String,String> entry : Centrailzed_Product_Map.Mproduct_qty.entrySet()){
            Lproduct_qty_temp.add(entry.getValue());
        }

        for (Map.Entry<String,String> entry : Centrailzed_Product_Map.Mproduct_price.entrySet()){
            Lproduct_final_price_temp.add(entry.getValue());
        }

        for(int i=0;i<Lproduct_qty_temp.size();i++){
            temp = temp+Integer.valueOf(Lproduct_final_price_temp.get(i))*Integer.valueOf(Lproduct_qty_temp.get(i));
        }

        price.setText("PROCEED TO BUY - ₹ "+String.valueOf(temp));

        Lproduct_qty_temp.clear();
        Lproduct_final_price_temp.clear();
    }

    public void intialise(){

        customAdapter = new CustomAdapter();
        listView = findViewById(R.id.listView);
        listView.setDivider(null);

        TextView no_of_qty = findViewById(R.id.textView28);

        price = findViewById(R.id.textView27);

        for (Map.Entry<String,String> entry : Centrailzed_Product_Map.Mproduct_name.entrySet()){
            Lproduct_id.add(entry.getKey());
            Lproduct_name.add(entry.getValue());
        }

        for (Map.Entry<String,String> entry : Centrailzed_Product_Map.Mproduct_image.entrySet()){
            Lproduct_image2.add(entry.getValue());
        }

        for (Map.Entry<String,String> entry : Centrailzed_Product_Map.Mproduct_qty.entrySet()){
            Lproduct_qty.add(entry.getValue());
        }

        for (Map.Entry<String,String> entry : Centrailzed_Product_Map.Mproduct_price.entrySet()){
            Lproduct_final_price.add(entry.getValue());
        }

        temp=0;

        for(int i=0;i<Lproduct_id.size();i++){
             temp = temp+Integer.valueOf(Lproduct_final_price.get(i))*Integer.valueOf(Lproduct_qty.get(i));
        }

        no_of_qty.setText(String.valueOf(Lproduct_name.size()));

        price.setText("PROCEED TO BUY - ₹ "+String.valueOf(temp));

        no_of_cart_items = findViewById(R.id.textView28);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(E_store_Product_Summary_page.this,E_store_Product_list.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}