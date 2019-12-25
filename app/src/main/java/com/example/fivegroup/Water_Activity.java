package com.example.fivegroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.NumberFormat;

public class Water_Activity extends AppCompatActivity {
    EditText w;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("每日適當飲水量計算");
        setSupportActionBar(toolbar);

        w = (EditText) findViewById(R.id.et1);


        final RadioGroup age=(RadioGroup) findViewById(R.id.age);
        Button submit = findViewById(R.id.button);
        Button btnClear = findViewById(R.id.btnClear);

        submit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!("".equals(w.getText().toString()))) {
                    float fw = Float.parseFloat(w.getEditableText().toString());
                    float fresult=0;


                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setMaximumFractionDigits(2);


                    switch (age.getCheckedRadioButtonId()){
                        case R.id.a1:
                            fresult+=fw*50;
                            break;
                        case R.id.a2:
                            fresult+=fw*35;
                            break;
                        case R.id.a3:
                            fresult+=fw*30;
                            break;
                        case R.id.a4:
                            fresult+=fw*25;
                            break;
                    }

                   if(age.getCheckedRadioButtonId()==R.id.a0){
                        if(fw<10){
                            fresult+=fw*100;
                        }
                        if(fw>=10&&fw<=20){
                            fresult+=1000+((fw-10)*50);
                        }if(fw>20){
                            fresult+=1500+((fw-20)*20);
                        }
                    }


                    TextView result = (TextView) findViewById(R.id.tv3);
                    result.setText(nf.format(fresult) + "cc");




                }
            }
        });

        btnClear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                w.setText("");
                TextView result = (TextView) findViewById(R.id.tv3);
                result.setText("");
            }
        });
    }

    public void click(View e) {
        Intent it = new Intent(Water_Activity.this, MainActivity.class);
        startActivity(it);
        finish();
    }


    }


