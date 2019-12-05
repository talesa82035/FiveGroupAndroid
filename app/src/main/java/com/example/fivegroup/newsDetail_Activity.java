package com.example.fivegroup;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class newsDetail_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);

        //加入自訂的ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("食藥署新聞");

        setSupportActionBar(toolbar);

        TextView title = findViewById(R.id.dTitle);
        TextView context = findViewById(R.id.context);

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {

            String str1 = bundle.getString("title");
            String str2 = bundle.getString("detail");

            title.setText(str1);
            context.setText(str2);
        }

    }

}
