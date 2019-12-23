package com.example.fivegroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class Searchdetail_Activity extends AppCompatActivity {

    private ListView lv;

    ArrayList<String> list_num;
    ArrayList<String> list_addr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("醫院清單");

        setSupportActionBar(toolbar);
        lv = findViewById(R.id.listViewHospital);
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            ArrayList<String> list = bundle.getStringArrayList("result");
            list_num = bundle.getStringArrayList("num");
            list_addr = bundle.getStringArrayList("addr");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);
        }
        lv.setOnItemClickListener(new detailListener());
    }

    private class detailListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            final String num = list_num.get(i);
            final String addr = list_addr.get(i);

            Intent intent = new Intent(Searchdetail_Activity.this, Navi_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("num", num);
            bundle.putString("addr", addr);

            intent.putExtras(bundle);

            startActivity(intent);
        }
    }


}
