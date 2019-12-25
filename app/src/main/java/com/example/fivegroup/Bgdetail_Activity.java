package com.example.fivegroup;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Bgdetail_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_detail);

        //加入自訂的ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("經歷背景");

        setSupportActionBar(toolbar);

//        TextView title = findViewById(R.id.dTitle);
        TextView context = findViewById(R.id.context);

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {

//            String str1 = bundle.getString("title");
            String str2 = bundle.getString("bg").replaceAll("\\\\n", "\n");

//            title.setText(str1);
            context.setText(str2);
        }

    }

}
