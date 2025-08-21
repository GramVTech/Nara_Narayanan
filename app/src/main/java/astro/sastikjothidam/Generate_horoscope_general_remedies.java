package astro.sastikjothidam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generate_horoscope_general_remedies extends AppCompatActivity {

    Map<String,String> concept_with_key = new HashMap<>();
    Map<String,String> key_with_remedies = new HashMap<>();
    List<String> concepts = new ArrayList<>();
    ListView listView;
    CustomAdapter customAdapter;
    TextView tamilTextView;
    View dialogView;

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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generate_horoscope_general_remedies);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intailise();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = concept_with_key.get(concepts.get(i));
                String remedies = key_with_remedies.get(key);
                LayoutInflater inflater = LayoutInflater.from(Generate_horoscope_general_remedies.this);
                dialogView = inflater.inflate(R.layout.dialog_tamil_text, null);
                tamilTextView = dialogView.findViewById(R.id.tamilTextView);
                tamilTextView.setText(remedies);
                new AlertDialog.Builder(Generate_horoscope_general_remedies.this)
                        .setView(dialogView)
                        .setPositiveButton("சரி", null)
                        .show();
            }
        });
    }

    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return concepts.size();
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_layout_general_remedies,null);
            TextView textView = view.findViewById(R.id.textView10);
            textView.setText(concepts.get(i));
            return view;
        }
    }

    public void intailise(){

        listView = findViewById(R.id.listView);
        customAdapter = new CustomAdapter();
        listView.setDivider(null);


        concept_with_key.put("புகழ்","1");
        concept_with_key.put("மன நிம்மதி","1");
        concept_with_key.put("குணம் தலை","1");
        concept_with_key.put("மூளை","1");
        concept_with_key.put("முடி","1");

        concept_with_key.put("குடும்ப உறுப்பினர்கள்","2");
        concept_with_key.put("பெரியம்மா","2");
        concept_with_key.put("பெரிய தாய்மாமா","2");
        concept_with_key.put("வலது கண்","2");
        concept_with_key.put("பாய்","2");
        concept_with_key.put("பல்","2");
        concept_with_key.put("முகம்","2");
        concept_with_key.put("நெற்றி","2");
        concept_with_key.put("வருமானம்","2");
        concept_with_key.put("வாக்கு","2");
        concept_with_key.put("பலம்","2");

        concept_with_key.put("இளைய சகோதர சகோதரி","3");
        concept_with_key.put("மாமனார்","3");
        concept_with_key.put("முயற்சி","3");
        concept_with_key.put("குறுகிய தூர பயணம்","3");
        concept_with_key.put("பரீட்சை","3");

        concept_with_key.put("வீடு","4");
        concept_with_key.put("வண்டி","4");
        concept_with_key.put("வாகனம்","4");
        concept_with_key.put("நிலம்","4");
        concept_with_key.put("அம்மா நுரையீரல்","4");
        concept_with_key.put("மார்பகம்","4");

        concept_with_key.put("சினிமா","5");
        concept_with_key.put("அரசியல்","5");
        concept_with_key.put("ஜோதிடம்","5");
        concept_with_key.put("குலதெய்வம்","5");
        concept_with_key.put("நுண்ணறிவு","5");
        concept_with_key.put("இருதயம்","5");
        concept_with_key.put("வயிறு","5");
        concept_with_key.put("கல்லீரல்","5");
        concept_with_key.put("மண்ணீரல்","5");
        concept_with_key.put("பித்தப்பை","5");
        concept_with_key.put("அப்பாவுடைய அப்பா தாத்தா","5");

        concept_with_key.put("நிரந்தர உத்தியோகம்","6");
        concept_with_key.put("கடன்","6");
        concept_with_key.put("நோய்","6");
        concept_with_key.put("வம்பு","6");
        concept_with_key.put("வழக்கு","6");
        concept_with_key.put("சண்டை","6");
        concept_with_key.put("சின்ன தாய்மாமா","6");
        concept_with_key.put("சித்தி","6");
        concept_with_key.put("அடி வயிறு","6");
        concept_with_key.put("இரைப்பை","6");
        concept_with_key.put("ஜீரண மண்டலம்","6");

        concept_with_key.put("கணவன்","7");
        concept_with_key.put("மனைவி","7");
        concept_with_key.put("தொழில்","7");
        concept_with_key.put("கூட பணிபுரியும் நபர்கள்","7");
        concept_with_key.put("சிறுநீரகம்","7");
        concept_with_key.put("முதுகுத்தண்டு","7");
        concept_with_key.put("தொழில் பயணம்","7");
        concept_with_key.put("சமுதாய அந்தஸ்து","7");

        concept_with_key.put("கணவன் மனைவி குடும்ப உறுப்பினர்கள்","8");
        concept_with_key.put("மார்பக உறுப்புகள்","8");
        concept_with_key.put("திடீர் அதிர்ஷ்டம்","8");
        concept_with_key.put("லாட்டரி","8");
        concept_with_key.put("விபத்து","8");
        concept_with_key.put("கண்டம்","8");
        concept_with_key.put("அவமானம்","8");
        concept_with_key.put("கெட்ட பெயர்","8");
        concept_with_key.put("போராட்டம்","8");
        concept_with_key.put("தடை","8");
        concept_with_key.put("தாமதம்","8");

        concept_with_key.put("அப்பா","9");
        concept_with_key.put("குரு","9");
        concept_with_key.put("கணவன் மற்றும் மனைவியின் இளைய சகோதரன், சகோதரி","9");
        concept_with_key.put("ஆன்மீக வாழ்க்கை","9");
        concept_with_key.put("ஆன்மீக யாத்திரை","9");
        concept_with_key.put("தெய்வ அனுகூலம்","9");
        concept_with_key.put("தொடைப்பகுதி","9");

        concept_with_key.put("சொந்த தொழில்","10");
        concept_with_key.put("தொண்டு நிறுவனம்","10");
        concept_with_key.put("மாமியார்","10");
        concept_with_key.put("முழங்கால்","10");

        concept_with_key.put("மூத்த சகோதரர்","11");
        concept_with_key.put("மூத்த சகோதரி நண்பர்கள்","11");
        concept_with_key.put("அதிக ஆசை","11");
        concept_with_key.put("சேமிப்பு","11");
        concept_with_key.put("லாபம்","11");
        concept_with_key.put("கணுக்கால் பகுதி","11");

        concept_with_key.put("தூக்கம்","12");
        concept_with_key.put("வெளிநாட்டு பயணம்","12");
        concept_with_key.put("விரயம்","12");
        concept_with_key.put("அப்பாவின் அம்மா","12");
        concept_with_key.put("அம்மாவின் அப்பா","12");
        concept_with_key.put("கால் பாதம்","12");

        key_with_remedies.put("1","சீப்பு, தொப்பியை தானம் செய்ய வேண்டும். தலை, மூளை சம்பந்தமான பிரச்சினை உள்ள நோயாளிகளுக்கு மருத்துவ உதவி செய்ய வேண்டும். ஹேர் ஸ்டைல் மாற்ற வேண்டும். பிரச்சனையிலிருந்து வெளியில் வந்து வெற்றிகரமாக இருப்பவர்களை சந்திக்க வேண்டும். Mobile, whatapp DP - யில் தங்களுடைய போட்டோவை வைக்கக்கூடாது. எந்த ஒரு காரியத்தையும் முன்னிலையாக இருந்து செய்யக்கூடாது.");
        key_with_remedies.put("2","Brush, paste, Plate தானம் செய்ய வேண்டும். பல் சம்பந்தமான, வலது கண் சம்பந்தமான பிரச்சனை உள்ள நோயாளிகளுக்கு உதவ வேண்டும். சாப்பிடக்கூடிய பழக்கவழக்கத்தை மாற்ற வேண்டும். பிரச்சனையிலிருந்து வெளியில் வந்து வெற்றி பெற்றவர்களை சந்திக்க வேண்டும். பிடித்த உணவை தியாகம் செய்ய வேண்டும். அடிக்கடி மௌன விரதம் இருக்க வேண்டும்.");
        key_with_remedies.put("3","எழுதும் பொருள்கள் தானம் செய்ய வேண்டும். பேனா, பென்சில் தானம் செய்ய வேண்டும் காது, மூக்கு, தொண்டை சம்பந்தமான பிரச்சனை உள்ள நோயாளிகளுக்கு உதவ வேண்டும். Mobile Change செய்ய வேண்டும்.அல்லது mobile pouch மாற்ற வேண்டும். பிரச்சனையில் இருந்து வெளியே வந்து வெற்றியுடன் வாழ்பவர்களை சந்திக்க வேண்டும். அடிக்கடி mobile Switch off செய்ய வேண்டும்.");
        key_with_remedies.put("4","வஸ்திரதானம் செய்ய வேண்டும்.  நுரையீரல், மார்பகம் சம்பந்தமாக பிரச்சனை உள்ள நோயாளிகளுக்கு மருத்துவ உதவி செய்ய வேண்டும். dressing style மாற்ற வேண்டும். வீட்டிற்கு பெயிண்ட் அடிக்க வேண்டும். கார் அடிக்கடி சர்வீஸ் செய்ய வேண்டும். பிரச்சனை இருந்து வெளியே வந்து வெற்றியடைந்த நபர்களை அடிக்கடி சந்திக்க வேண்டும். சொந்த வீடு, சொந்தக்காரை அதிகமாக பயன்படுத்தாமல் வாடகை கார் வாடகை வீட்டை பயன்படுத்த வேண்டும்.");
        key_with_remedies.put("5","குழந்தை விளையாடக்கூடிய பொருள்களை தானம் செய்ய வேண்டும். வயிறு மற்றும் எரியும் சம்பந்தமான பிரச்சனை உள்ளவர்களுக்கு மருத்துவ உதவி செய்ய வேண்டும். சினிமாவுக்கு போகக்கூடிய செயல்பாடுகளை மாற்ற வேண்டும். வீட்டில் உள்ள டி.வி யை மாற்ற வேண்டும். பிரச்சனையில் இருந்து வெளியே வந்து வெற்றிகரமாக வாழ்பவர்களை சந்திக்க வேண்டும். சினிமாவுக்கு செல்வதை, டி.வி. பார்ப்பதை தியாகம் செய்ய வேண்டும்.");
        key_with_remedies.put("6","கூர்மையான ஆயுதங்களை தானம் செய்ய வேண்டும். அடி வயிறு சம்பந்தமான பிரச்சனை உள்ள நோயாளிகளுக்கு உதவி செய்ய வேண்டும். தினசரி செயல்பாடுகளை மாற்ற வேண்டும். பிரச்சினையில் இருந்து வெளியில் வந்து வெற்றிகரமான நபர்களை சந்திக்க வேண்டும். அதிகமாக உத்யோகம் செய்வது சண்டை போடுவது, கோபப்படுவதை குறைத்துக் கொள்ள வேண்டும்.");
        key_with_remedies.put("7","வாசனை திரவியங்களை அடிக்கடி தானம் செய்ய வேண்டும். சிறுநீரகம் மற்றும் முதுகு தண்டு பிரச்சனை உள்ள நோயாளிகளுக்கு உதவ வேண்டும். வாசனை திரவியங்கள் பயன்படுத்தும் Brand -ஐ மாற்ற வேண்டும். பிரச்சனையில் இருந்து வெளிய வந்த நபர்களை வெற்றி பெற்றவர்களை சந்திக்க வேண்டும். பூ வைப்பதை தியாகம் செய்ய வேண்டும். இல்லற வாழ்க்கையை தியாகம் செய்ய வேண்டும்.");
        key_with_remedies.put("8","Bathroom க்கு தேவையான பினாயில் பாட்டில் விளக்குமாறு இந்த பொருள்களை தானம் செய்ய வேண்டும். உள்ளாடை தானம் செய்ய வேண்டும். மர்ம உறுப்பில் பிரச்சனை விபத்தில் பாதிக்கப்பட்ட நோயாளிகளுக்கு உதவ வேண்டும். Indian Toilet use செய்பவர்கள் Western toilet use செய்ய வேண்டும். கழிப்பறையை மாற்றி மாற்றி பயன்படுத்த வேண்டும். பிரச்சினையிலிருந்து வெளியில் வந்தவர்களை வெற்றி பெற்றவர்களை அதிகமாக சந்திக்க வேண்டும். ஜோதிடம், அமானுஷ்யம், மாந்திரீகம் போன்ற விஷயத்தில் கவனம் செலுத்துவதை குறைத்துக் கொள்ள வேண்டும்.  இன்சூரன்ஸ் பணத்தை கட்டாமல் விட்டு விட வேண்டும்.");
        key_with_remedies.put("9","விபூதி, குங்குமம், ஜவ்வாது போன்ற ஆன்மீக பொருட்களை அடிக்கடி தானம் செய்ய வேண்டும். தொடை சார்ந்த உறுப்பில் பிரச்சினை உள்ள நோயாளிகளுக்கு உதவ வேண்டும்.. பூஜை செய்யக்கூடிய நேரத்தை அடிக்கடி மாற்ற வேண்டும். பிரச்சனையில் இருந்து வெளியில் வந்தவர்களை அடிக்கடி சந்திக்க வேண்டும். கோயிலுக்கு செல்வதை குறைத்துக்கொள்ள வேண்டும்.");
        key_with_remedies.put("10","முழங்காலுக்கு போடக்கூடிய Bandage பொருட்களை  தானம் செய்ய வேண்டும். முழங்காலில் அடிபட்டவர்களுக்கு உதவ வேண்டும். முழங்கால் வலி உள்ளவர்களுக்கு உதவ வேண்டும். தொழில் சம்பந்தமான திட்டங்களை அடிக்கடி மாற்றி அமைக்க வேண்டும். பிரச்சனையிலிருந்து வெளியில் வந்தவர்களை வெற்றி பெற்றவர்களை சந்திக்க வேண்டும். தொழில் தன்னுடைய பெயரில் இருந்து வேற ஒருவர் பெயரில் மாற்ற வேண்டும்.");
        key_with_remedies.put("11","கொலுசு தானம், Shocks தானம் அடிக்கடி செய்ய வேண்டும். கணுக்காலில் பிரச்சனை உள்ள நோயாளிகளுக்கு மருத்துவ உதவி செய்ய வேண்டும். பணத்தை சேமிக்க கூடிய Fixed Deposit ஐ வேறு ஒரு வங்கிக்கு மாற்ற வேண்டும். பிரச்சனையில் இருந்து வெளியே வந்து வெற்றிவுடன் வாழ்பவர்கள் அடிக்கடி சந்திக்க வேண்டும். தனக்கு பிடித்த விஷயத்தை Sacrifice செய்ய வேண்டும்.");
        key_with_remedies.put("12","காலணி, போர்வை அடிக்கடி தானம் செய்ய வேண்டும். கால் பாதத்தில் பிரச்சனை உள்ளவர்களுக்கு மருத்துவ உதவி செய்ய வேண்டும். தூங்கும் இடத்தை அடிக்கடி மாற்ற வேண்டும். காலணியை மாற்ற வேண்டும். பிரச்சினையில் இருந்து வெளியில் வந்தவர்களை அடிக்கடி சந்திக்க வேண்டும். தூக்கத்தை குறைத்துக் கொள்ள வேண்டும். விமான பயணங்களை குறைத்துக் கொள்ள வேண்டும். அதிக இரவு நேர பயணங்களை தவிர்க்க வேண்டும்.");

        concepts.add("புகழ்");
        concepts.add("மன நிம்மதி");
        concepts.add("குணம் தலை");
        concepts.add("மூளை");
        concepts.add("முடி");

        concepts.add("குடும்ப உறுப்பினர்கள்");
        concepts.add("பெரியம்மா");
        concepts.add("பெரிய தாய்மாமா");
        concepts.add("வலது கண்");
        concepts.add("பாய்");
        concepts.add("பல்");
        concepts.add("முகம்");
        concepts.add("நெற்றி");
        concepts.add("வருமானம்");
        concepts.add("வாக்கு");
        concepts.add("பலம்");

        concepts.add("இளைய சகோதர சகோதரி");
        concepts.add("மாமனார்");
        concepts.add("முயற்சி");
        concepts.add("குறுகிய தூர பயணம்");
        concepts.add("பரீட்சை");

        concepts.add("வீடு");
        concepts.add("வண்டி");
        concepts.add("வாகனம்");
        concepts.add("நிலம்");
        concepts.add("அம்மா நுரையீரல்");
        concepts.add("மார்பகம்");

        concepts.add("சினிமா");
        concepts.add("அரசியல்");
        concepts.add("ஜோதிடம்");
        concepts.add("குலதெய்வம்");
        concepts.add("நுண்ணறிவு");
        concepts.add("இருதயம்");
        concepts.add("வயிறு");
        concepts.add("கல்லீரல்");
        concepts.add("மண்ணீரல்");
        concepts.add("பித்தப்பை");
        concepts.add("அப்பாவுடைய அப்பா தாத்தா");

        concepts.add("நிரந்தர உத்தியோகம்");
        concepts.add("கடன்");
        concepts.add("நோய்");
        concepts.add("வம்பு");
        concepts.add("வழக்கு");
        concepts.add("சண்டை");
        concepts.add("சின்ன தாய்மாமா");
        concepts.add("சித்தி");
        concepts.add("அடி வயிறு");
        concepts.add("இரைப்பை");
        concepts.add("ஜீரண மண்டலம்");

        concepts.add("கணவன்");
        concepts.add("மனைவி");
        concepts.add("தொழில்");
        concepts.add("கூட பணிபுரியும் நபர்கள்");
        concepts.add("சிறுநீரகம்");
        concepts.add("முதுகுத்தண்டு");
        concepts.add("தொழில் பயணம்");
        concepts.add("சமுதாய அந்தஸ்து");

        concepts.add("கணவன் மனைவி குடும்ப உறுப்பினர்கள்");
        concepts.add("மார்பக உறுப்புகள்");
        concepts.add("திடீர் அதிர்ஷ்டம்");
        concepts.add("லாட்டரி");
        concepts.add("விபத்து");
        concepts.add("கண்டம்");
        concepts.add("அவமானம்");
        concepts.add("கெட்ட பெயர்");
        concepts.add("போராட்டம்");
        concepts.add("தடை");
        concepts.add("தாமதம்");

        concepts.add("அப்பா");
        concepts.add("குரு");
        concepts.add("கணவன் மற்றும் மனைவியின் இளைய சகோதரன், சகோதரி");
        concepts.add("ஆன்மீக வாழ்க்கை");
        concepts.add("ஆன்மீக யாத்திரை");
        concepts.add("தெய்வ அனுகூலம்");
        concepts.add("தொடைப்பகுதி");

        concepts.add("சொந்த தொழில்");
        concepts.add("தொண்டு நிறுவனம்");
        concepts.add("மாமியார்");
        concepts.add("முழங்கால்");

        concepts.add("மூத்த சகோதரர்");
        concepts.add("மூத்த சகோதரி நண்பர்கள்");
        concepts.add("அதிக ஆசை");
        concepts.add("சேமிப்பு");
        concepts.add("லாபம்");
        concepts.add("கணுக்கால் பகுதி");

        concepts.add("தூக்கம்");
        concepts.add("வெளிநாட்டு பயணம்");
        concepts.add("விரயம்");
        concepts.add("அப்பாவின் அம்மா");
        concepts.add("அம்மாவின் அப்பா");
        concepts.add("கால் பாதம்");

        listView.setAdapter(customAdapter);
    }
}