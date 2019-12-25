package com.example.fivegroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.NumberFormat;

public class BMR_Activity extends AppCompatActivity {

    EditText h;
    EditText w;
    EditText age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("基礎代謝率計算");
        setSupportActionBar(toolbar);

        h = (EditText) findViewById(R.id.et1);
        w = (EditText) findViewById(R.id.et2);
        age = (EditText) findViewById(R.id.et3);

        final RadioGroup rg=(RadioGroup) findViewById(R.id.rgGender);
        Button submit = findViewById(R.id.button);
        Button btnClear = findViewById(R.id.btnClear);


        submit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!("".equals(h.getText().toString())||"".equals(w.getText().toString()) ||"".equals(age.getText().toString()))) {
                    float fh = Float.parseFloat(h.getEditableText().toString());
                    float fw = Float.parseFloat(w.getEditableText().toString());
                    int fage=Integer.parseInt(age.getEditableText().toString());
                    float fresult=0;


                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setMaximumFractionDigits(2);

                    switch (rg.getCheckedRadioButtonId()){
                        case R.id.boy:
                            fresult+=66+(13.7*fw)+(5.0*fh)-(6.8*fage);
                            break;
                        case R.id.girl:
                            fresult+=665+(9.6*fw)+(1.8*fh)-(4.7*fage);
                            break;
                    }

                    TextView result = (TextView) findViewById(R.id.tv4);
                    result.setText(nf.format(fresult) + "kcal");


                }
            }
        });

        btnClear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                age.setText("");
                w.setText("");
                h.setText("");
                TextView result = (TextView) findViewById(R.id.tv4);
                result.setText("");
            }
        });
    }

    public void click(View e) {
        Intent it = new Intent(BMR_Activity.this, MainActivity.class);
        startActivity(it);
        finish();
    }



}
