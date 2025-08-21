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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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

public class BottomSheetDialog_For_Premium_video_details extends BottomSheetDialogFragment {

    View view;
    Context context;
    String name="",description="";


    public BottomSheetDialog_For_Premium_video_details(Context context1, String sname, String sdesc) {

        context= context1;
        name = sname;
        description = sdesc;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.bottomsheet_premium_descritption,container,false);

        ImageView close = view.findViewById(R.id.imageView8);
        TextView class_name = view.findViewById(R.id.textView12);
        TextView class_desc = view.findViewById(R.id.textView15);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        class_name.setText(name);
        class_desc.setText(description);

        return view;
    }
}


