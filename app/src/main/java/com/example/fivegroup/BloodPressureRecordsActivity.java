package com.example.fivegroup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BloodPressureRecordsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_bloodpressure);
        //加入自訂的ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("血壓紀錄");
        setSupportActionBar(toolbar);
    }
}
