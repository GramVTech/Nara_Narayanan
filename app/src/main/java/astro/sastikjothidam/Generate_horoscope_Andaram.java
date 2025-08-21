package astro.sastikjothidam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Generate_horoscope_Andaram extends AppCompatActivity {

    Intent intent;
    TextView txt81,txt82,txt83,txt84,txt85,txt86,txt87,txt88,txt89,
            txt91,txt92,txt93,txt94,txt95,txt96,txt97,txt98,txt99,
            txt71,txt72,txt73,txt74,txt75,txt76,txt77,txt78,txt79,textView25;

    ImageView img24,img25,img26,img27,img28,img29,img30,img31,img32;
    int lord_span=0;
    ArrayList<String> grahas = new ArrayList<>();
    ArrayList<TextView> grahas_txt = new ArrayList<>();
    ArrayList<TextView> grahas_SD_txt = new ArrayList<>();
    ArrayList<TextView> grahas_ED_txt = new ArrayList<>();
    ArrayList<ImageView> grahas_IM = new ArrayList<>();
    Map<String, Integer> Dasa_map = new HashMap<>();
    String Begin_lord = "",dasa_lord="";
    int dasa_span =0;

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
        setContentView(R.layout.activity_generate_horoscope_andaram);
        intailise();
        Begin_lord = intent.getStringExtra("Graha");
        dasa_lord = intent.getStringExtra("DASA");
        textView25.setText("DASA ( " + dasa_lord + " ) " + "- BUKTI ( "+Begin_lord+" ) "+" Andaram");
        calc();
    }

    public void calc(){
        try {
            int start = grahas.indexOf(Begin_lord);
            String from_date = intent.getStringExtra("from_date");
            String to_date = "";
            double x = Double.parseDouble(intent.getStringExtra("x"));

            for (int i = 0; i < grahas_txt.size(); i++) {
                if (start > 8)
                    start = 0;
                TextView gtx = grahas_txt.get(i);
                gtx.setText(grahas.get(start));

                TextView sd = grahas_SD_txt.get(i);
                sd.setText(from_date);

                TextView ed = grahas_ED_txt.get(i);

                dasa_span = Dasa_map.get(grahas.get(start));
                double bukti_days = dasa_span * x;
                to_date = bukti_to_date(bukti_days, from_date);
                ed.setText(to_date.split("-")[2] + "-" + to_date.split("-")[1] + "-" + to_date.split("-")[0]);

                from_date = to_date.split("-")[2] + "-" + to_date.split("-")[1] + "-" + to_date.split("-")[0];
                start = start + 1;
            }
            txt79.setText(intent.getStringExtra("to_date"));
        }catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
    }

    public String bukti_to_date(double val,String fd){
        try {
            String[] dater = fd.split("-");
            LocalDate initialDate = null;
            LocalDate newDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int years = Integer.parseInt(dater[2]);
                int months = Integer.parseInt(dater[1]);
                int days = Integer.parseInt(dater[0]);
                initialDate = LocalDate.of(years, months, days);
                double daysToAdd = val;
                long wholeDaysToAdd = (long) daysToAdd;
                newDate = initialDate.plusDays(wholeDaysToAdd);
                return String.valueOf(newDate);
            }
        }catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    public void intailise(){

        intent = getIntent();

        Dasa_map.put("Sun", 6);
        Dasa_map.put("Moon", 10);
        Dasa_map.put("Mars", 7);
        Dasa_map.put("Mercury", 17);
        Dasa_map.put("Jupiter", 16);
        Dasa_map.put("Venus", 20);
        Dasa_map.put("Saturn", 19);
        Dasa_map.put("Rahu", 18);
        Dasa_map.put("Ketu", 7);

        txt81 = findViewById(R.id.textView81);
        txt82 = findViewById(R.id.textView82);
        txt83 = findViewById(R.id.textView83);
        txt84 = findViewById(R.id.textView84);
        txt85 = findViewById(R.id.textView85);
        txt86 = findViewById(R.id.textView86);
        txt87 = findViewById(R.id.textView87);
        txt88 = findViewById(R.id.textView88);
        txt89 = findViewById(R.id.textView89);

        txt91 = findViewById(R.id.textView91);
        txt92 = findViewById(R.id.textView92);
        txt93 = findViewById(R.id.textView93);
        txt94 = findViewById(R.id.textView94);
        txt95 = findViewById(R.id.textView95);
        txt96 = findViewById(R.id.textView96);
        txt97 = findViewById(R.id.textView97);
        txt98 = findViewById(R.id.textView98);
        txt99 = findViewById(R.id.textView99);

        txt71 = findViewById(R.id.textView71);
        txt72 = findViewById(R.id.textView72);
        txt73 = findViewById(R.id.textView73);
        txt74 = findViewById(R.id.textView74);
        txt75 = findViewById(R.id.textView75);
        txt76 = findViewById(R.id.textView76);
        txt77 = findViewById(R.id.textView77);
        txt78 = findViewById(R.id.textView78);
        txt79 = findViewById(R.id.textView79);

        img24 = findViewById(R.id.imageView24);
        img25 = findViewById(R.id.imageView25);
        img26 = findViewById(R.id.imageView26);
        img27 = findViewById(R.id.imageView27);
        img28 = findViewById(R.id.imageView28);
        img29 = findViewById(R.id.imageView29);
        img30 = findViewById(R.id.imageView30);
        img31 = findViewById(R.id.imageView31);
        img32 = findViewById(R.id.imageView32);

        textView25 = findViewById(R.id.textView25);

        grahas.add("Sun");
        grahas.add("Moon");
        grahas.add("Mars");
        grahas.add("Rahu");
        grahas.add("Jupiter");
        grahas.add("Saturn");
        grahas.add("Mercury");
        grahas.add("Ketu");
        grahas.add("Venus");

        grahas_txt.add(txt81);
        grahas_txt.add(txt82);
        grahas_txt.add(txt83);
        grahas_txt.add(txt84);
        grahas_txt.add(txt85);
        grahas_txt.add(txt86);
        grahas_txt.add(txt87);
        grahas_txt.add(txt88);
        grahas_txt.add(txt89);

        grahas_SD_txt.add(txt91);
        grahas_SD_txt.add(txt92);
        grahas_SD_txt.add(txt93);
        grahas_SD_txt.add(txt94);
        grahas_SD_txt.add(txt95);
        grahas_SD_txt.add(txt96);
        grahas_SD_txt.add(txt97);
        grahas_SD_txt.add(txt98);
        grahas_SD_txt.add(txt99);

        grahas_ED_txt.add(txt71);
        grahas_ED_txt.add(txt72);
        grahas_ED_txt.add(txt73);
        grahas_ED_txt.add(txt74);
        grahas_ED_txt.add(txt75);
        grahas_ED_txt.add(txt76);
        grahas_ED_txt.add(txt77);
        grahas_ED_txt.add(txt78);
        grahas_ED_txt.add(txt79);

        grahas_IM.add(img24);
        grahas_IM.add(img25);
        grahas_IM.add(img26);
        grahas_IM.add(img27);
        grahas_IM.add(img28);
        grahas_IM.add(img29);
        grahas_IM.add(img30);
        grahas_IM.add(img31);
        grahas_IM.add(img32);

    }
}