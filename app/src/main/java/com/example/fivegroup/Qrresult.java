package com.example.fivegroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
//import com.google.android.gms.vision.barcode.Barcode;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.util.ArrayList;

public class Qrresult extends AppCompatActivity {

    TextView result;
    private ListView lv;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> context = new ArrayList<>();
    //ListView listView;
    //ListAdapter listAdapter;
    //String[] Bararry = {};

    //String srt = "AAA:BBB:CCC:DDD";
    //String [] tokens = srt.split(":");
    //for (String token:tokens) {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrresult);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("掃描結果");

        result = (TextView) findViewById(R.id.qrcodetextresult);

        Barcode barcode = getIntent().getParcelableExtra("barcode");
        String[] tokens = (barcode.displayValue).split(":");
//        StringBuffer outStr = new StringBuffer();
//        String[] tokens = {"己二酸","己二烯酸"};
        for (int i=0 ; i < tokens.length; i++ )
            list.add(tokens[i]);
//            outStr.append(tokens[i]+"\n");
//        result.setText(outStr);
//        final Barcode barcode = getIntent().getParcelableExtra("barcode");
//        Barcode srt = barcode;

//        String barcode = ((EditText)findViewById(R.id.testString)).getText().toString();
//        String[] tokens = barcode.split(":");
//        StringBuffer outStr = new StringBuffer("");
//        for (int i=0 ; i < tokens.length; i++ )
//            outStr.append(tokens[i]+"\n");
//        result.setText(outStr);
//        result.setAutoLinkMask(1);
//        result.setMovementMethod(LinkMovementMethod.getInstance());


        lv = findViewById(R.id.listview_qrresult);
        lv.setOnItemClickListener(new detailListener());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adapter);

        //listView = (ListView)findViewById(R.id.qrcodetextresult);
        //final Barcode barcode = getIntent().getParcelableExtra("barcode");
        //String srt  = barcode;
        //String [] tokens = srt.split(":");
        //listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tokens);
        //listView.setAdapter(listAdapter);

    }


    private class detailListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            final String title = list.get(i);
//            final String detail = context.get(i).replace("&ensp;","").replace("<br />","");

            Intent intent = new Intent(Qrresult.this, QrresultDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
//            bundle.putString("detail", detail);

            intent.putExtras(bundle);

            startActivity(intent);
        }


    }
}