package com.example.fivegroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        //加入自訂的ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("個人紀錄");
        setSupportActionBar(toolbar);

        //前往體重紀錄
        Button weightrecord = findViewById(R.id.weightrecordbutton);
        weightrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordsActivity.this, WeightRecordsActivity.class);
                startActivity(intent);
            }
        });
        //前往血壓紀錄
        Button bloodpressurerecord = findViewById(R.id.bloodpressurerecordbutton);
        bloodpressurerecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordsActivity.this, BloodpressureRecordsActivity.class);
                startActivity(intent);
            }
        });
        //前往血糖紀錄
        Button bloodsugarrerecord = findViewById(R.id.bloodsugarrecordbutton);
        bloodsugarrerecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordsActivity.this, BloodsugarRecordsActivity.class);
                startActivity(intent);
            }
        });

    }
}
