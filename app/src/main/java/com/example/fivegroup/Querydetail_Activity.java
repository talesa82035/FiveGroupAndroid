package com.example.fivegroup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class Querydetail_Activity extends AppCompatActivity {

    private ListView lv;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("建議科別");

        setSupportActionBar(toolbar);

        lv = findViewById(R.id.listViewResult);
        btn = findViewById(R.id.button01);

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {
            ArrayList<String> list = bundle.getStringArrayList("result");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);
        }

        btn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Querydetail_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
