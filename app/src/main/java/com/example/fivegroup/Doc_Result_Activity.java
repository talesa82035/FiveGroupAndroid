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

import com.example.fivegroup.R;

import java.util.ArrayList;

public class Doc_Result_Activity extends AppCompatActivity {

    private ListView lv;

    ArrayList<String> list_bg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_result);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("醫師清單");

        setSupportActionBar(toolbar);

        lv = findViewById(R.id.listViewDoctor);
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            ArrayList<String> list = bundle.getStringArrayList("result");
            list_bg = bundle.getStringArrayList("bg");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);
        }
        lv.setOnItemClickListener(new detailListener());
    }

    private class detailListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final String bg = list_bg.get(i);

            Intent intent = new Intent(Doc_Result_Activity.this, Bgdetail_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("bg", bg);

            intent.putExtras(bundle);

            startActivity(intent);
        }
    }
}
