package com.example.fivegroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Tools_Activity extends AppCompatActivity {
    private Button btn_menstrualCycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("實用工具");
        setSupportActionBar(toolbar);

        this.btn_menstrualCycle=findViewById(R.id.btn_menstrualCycle);
        this.btn_menstrualCycle.setOnClickListener(onMenstrualCycleClickHandler);

        //前往BMI計算
        Button bmi = findViewById(R.id.btn1);
        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tools_Activity.this, BMI_Activity.class);
                startActivity(intent);
            }
        });
    }

    View.OnClickListener onMenstrualCycleClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Tools_Activity.this, MenstrualCycleActivity.class);
            startActivity(intent);
        }
    };

}
