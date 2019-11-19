package com.example.fivegroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

public class BMI_Activity extends AppCompatActivity {
    EditText h;
    EditText w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        h = (EditText) findViewById(R.id.et1);
        w = (EditText) findViewById(R.id.et2);
        Button submit = findViewById(R.id.button);
        Button btnClear = findViewById(R.id.btnClear);

        submit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!("".equals(h.getText().toString()) || "".equals(w.getText().toString()))){
                    float fh = Float.parseFloat(h.getEditableText().toString());
                    float fw = Float.parseFloat(w.getEditableText().toString());
                    float fresult;
                    fh = fh/100;
                    fh = fh*fh;

                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setMaximumFractionDigits(2);
                    fresult = fw/fh;
                    TextView result = (TextView) findViewById(R.id.tv3);
                    result.setText(nf.format(fresult)+"");
                    TextView dia = (TextView) findViewById(R.id.tv4);
                    if(fresult<18.5){
                        dia.setText("體重過輕");
                    }else if(18.5<=fresult && fresult<24){
                        dia.setText("正常範圍");
                    }else if(24<=fresult && fresult<27){
                        dia.setText("過重");
                    }else if(27<=fresult && fresult<30){
                        dia.setText("輕度肥胖");
                    }else if(30<=fresult && fresult<35){
                        dia.setText("中度肥胖");
                    }else if(fresult>=35){
                        dia.setText("重度肥胖");
                    }

                }
            }
        });

        btnClear.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                h.setText("");
                w.setText("");
                TextView result = (TextView) findViewById(R.id.tv3);
                result.setText("結果：");
                TextView dia = (TextView) findViewById(R.id.tv4);
                dia.setText("診斷");
            }
        });
    }

    public void click(View e){
        Intent it = new Intent(BMI_Activity.this,MainActivity.class);
        startActivity(it);
        finish();
    }

}
