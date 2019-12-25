package com.example.fivegroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.NumberFormat;


public class BMI_Activity extends AppCompatActivity {
    EditText h;
    EditText w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("BMI計算");
        setSupportActionBar(toolbar);

        h = (EditText) findViewById(R.id.et1);
        w = (EditText) findViewById(R.id.et2);
        Button submit = findViewById(R.id.button);
        Button btnClear = findViewById(R.id.btnClear);
        Button help = findViewById(R.id.help1);

        submit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!("".equals(h.getText().toString()) || "".equals(w.getText().toString()))) {
                    float fh = Float.parseFloat(h.getEditableText().toString());
                    float fw = Float.parseFloat(w.getEditableText().toString());
                    float fresult;
                    fh = fh / 100;
                    fh = fh * fh;

                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setMaximumFractionDigits(2);
                    fresult = fw / fh;
                    TextView result = (TextView) findViewById(R.id.tv3);
                    result.setText(nf.format(fresult) + "");
                    TextView dia = (TextView) findViewById(R.id.tv4);
                    if (fresult < 18.5) {
                        dia.setText("體重過輕");
                    } else if (18.5 <= fresult && fresult < 24) {
                        dia.setText("正常範圍");
                    } else if (24 <= fresult && fresult < 27) {
                        dia.setText("過重");
                    } else if (27 <= fresult && fresult < 30) {
                        dia.setText("輕度肥胖");
                    } else if (30 <= fresult && fresult < 35) {
                        dia.setText("中度肥胖");
                    } else if (fresult >= 35) {
                        dia.setText("重度肥胖");
                    }

                }
            }
        });

        btnClear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                h.setText("");
                w.setText("");
                TextView result = (TextView) findViewById(R.id.tv3);
                result.setText("");
                TextView dia = (TextView) findViewById(R.id.tv4);
                dia.setText("診斷結果");
            }
        });

        help.setOnClickListener(new View.OnClickListener() {          //add define box
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(BMI_Activity.this).create();
                alertDialog.setTitle("功能解釋");
                alertDialog.setMessage("身體質量指數（BMI）是健康檢查常用的指標，計算式為體重(公斤) / 身高平方(公尺)\n" +
                        "\n"+
                        "體重過輕: BMI ＜ 18.5\n" +
                        "正常範圍: 18.5≦BMI＜24\n" +
                        "體重過重: 24≦BMI＜27");
                alertDialog.setButton(alertDialog.BUTTON_POSITIVE,"知道了", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                    }
                });
                alertDialog.show();
            }
        });


    }

    public void click(View e) {
        Intent it = new Intent(BMI_Activity.this, MainActivity.class);
        startActivity(it);
        finish();
    }

}
