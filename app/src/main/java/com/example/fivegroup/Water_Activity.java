package com.example.fivegroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.NumberFormat;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

public class Water_Activity extends AppCompatActivity {
    EditText w;
    EditText age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("每日適當飲水量計算");
        setSupportActionBar(toolbar);

        w = (EditText) findViewById(R.id.et1);
        age = (EditText) findViewById(R.id.et2);

        Button submit = findViewById(R.id.button);
        Button btnClear = findViewById(R.id.btnClear);
        Button help = findViewById(R.id.help1);

        submit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!("".equals(w.getText().toString()) ||"".equals(age.getText().toString()))) {
                    float fw = Float.parseFloat(w.getEditableText().toString());
                    int fage=Integer.parseInt(age.getEditableText().toString());
                    float fresult=0;


                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setMaximumFractionDigits(2);

                    if(fage>=7&&fage<=12){
                        fresult=0;
                        fresult+=fw*50;
                    }
                    if(fage>=13&&fage<=30){
                        fresult=0;
                        fresult+=fw*35;
                    }
                    if(fage>=31&&fage<=65){
                        fresult=0;
                        fresult+=fw*30;
                    }
                    if(fage>65){
                        fresult=0;
                        fresult+=fw*25;
                    }

                    TextView result = (TextView) findViewById(R.id.tv3);
                    if(fage<7){
                        result.setText("SORRY，嬰幼兒每日適當飲水量，請詢問醫師建議!!");
                    }else{
                        result.setText(nf.format(fresult) + "cc");
                    }



                }
            }
        });

        btnClear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                age.setText("");
                w.setText("");
                TextView result = (TextView) findViewById(R.id.tv3);
                result.setText("");
            }
        });


        help.setOnClickListener(new View.OnClickListener() {          //add define box
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(Water_Activity.this).create();
                alertDialog.setTitle("功能解釋");
                alertDialog.setMessage("不同年齡層每日所需飲水量計算公式\n" +
                        "\n"+
                        "體幼兒(2-6歲)\n" +
                        "\n"+
                        "10公斤內，體重*100\n" +
                        "20公斤內，(體重-10)*50+1000\n" +
                        "\n"+
                        "7-12 歲所需飲用水（毫升，mL）為每公斤體重乘以 50至 60\n" +
                        "13至30歲為每公斤體重乘以 35 至 40\n" +
                        "\n"+
                        "31 至 54 歲為每公斤體重乘以 30 至 35\n" +
                        "\n"+
                        "55 至 65 歲為每公斤體重乘以 30\n" +
                        "65 歲以上為每公斤體重乘以 25");

                alertDialog.setButton(alertDialog.BUTTON_POSITIVE,"原來如此!", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                    }
                });
                alertDialog.show();
            }
        });
    }

    public void click(View e) {
        Intent it = new Intent(Water_Activity.this, MainActivity.class);
        startActivity(it);
        finish();
    }



}
