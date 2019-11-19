package com.example.fivegroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Tools_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("實用工具");
        setSupportActionBar(toolbar);

        //前往BMI計算
        Button bmi = findViewById(R.id.button2);
        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tools_Activity.this, BMI_Activity.class);
                startActivity(intent);
            }
        });
    }

}
