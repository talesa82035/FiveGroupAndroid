package com.example.fivegroup;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MenstrualCycleActivity extends AppCompatActivity {
    private TextView tv_menstrualDate;
    private TextView tv_dangerousDate;
    private TextView tv_ovulationDate;
    private EditText et_cycle;
    private TextView tv_lastDate;
    private Button btnChooseDate;

    private Calendar calendar=Calendar.getInstance();
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menstrual_cycle);
        this.tv_menstrualDate = findViewById(R.id.tv_menstrualDate);
        this.tv_dangerousDate = findViewById(R.id.tv_dangerousDate);
        this.tv_ovulationDate = findViewById(R.id.tv_ovulationDate);
        this.et_cycle = findViewById(R.id.et_cycle);
        this.tv_lastDate = findViewById(R.id.tv_lastDate);
        this.btnChooseDate = findViewById(R.id.btnChooseDate);

        this.btnChooseDate.setOnClickListener(chooseStartDateClickHandler);
    }

    View.OnClickListener chooseStartDateClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(MenstrualCycleActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);

                    tv_lastDate.setText(ft.format(calendar.getTime()));
//                    String strDateFormat = commonNotification.turnDate2String(calendar.getTime());
//                    String[] dataArr = strDateFormat.split(" ");
//                    tv_startdate.setText(dataArr[0]);
                }
            }, year, month, day).show();
        }
    };


}
