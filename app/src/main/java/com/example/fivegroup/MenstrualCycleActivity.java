package com.example.fivegroup;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

//https://calculator.warmiehealth.com/
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("月經週期計算");
        setSupportActionBar(toolbar);

        Button help = findViewById(R.id.help1);

        this.tv_menstrualDate = findViewById(R.id.tv_menstrualDate);//下次月經時間：下次月經日等於「本次月經日加上平均週期天數」 經期長度約為5~7天
        this.tv_dangerousDate = findViewById(R.id.tv_dangerousDate);//危險期：排卵日的前5天～後3天
        this.tv_ovulationDate = findViewById(R.id.tv_ovulationDate);//排卵日：下次月經日減掉14天的前一天
        this.et_cycle = findViewById(R.id.et_cycle);
        this.tv_lastDate = findViewById(R.id.tv_lastDate);
        this.btnChooseDate = findViewById(R.id.btnChooseDate);

        this.btnChooseDate.setOnClickListener(chooseStartDateClickHandler);



        help.setOnClickListener(new View.OnClickListener() {          //add define box
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(MenstrualCycleActivity.this).create();
                alertDialog.setTitle("功能解釋");
                alertDialog.setMessage("下次月經時間：下次月經日等於「本次月經日加上平均週期天數」 經期長度約為5~7天\n" +
                        "\n" +
                        "危險期：排卵日的前5天～後3天\n" +
                        "\n" +
                        "排卵日：下次月經日減掉14天的前一天");
                alertDialog.setButton(alertDialog.BUTTON_POSITIVE,"學到了", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                    }
                });
                alertDialog.show();
            }
        });



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
                    calcResult();
                }
            }, year, month, day).show();
        }
    };



    private void calcResult(){
        try {
//        checkValidity();
            int cycleDay = Integer.parseInt(this.et_cycle.getText().toString());
            String date = this.tv_lastDate.getText().toString();
            Date lastDate = this.ft.parse(date);

            String menstrualDate = this.calcMenstrualDate(cycleDay,lastDate);
            this.tv_menstrualDate.setText(menstrualDate);

            String dangerousDate = this.calcDangerousDate(cycleDay,lastDate);
            this.tv_dangerousDate.setText(dangerousDate);

            String ovulationDate = this.calcOvulationDate(cycleDay,lastDate);
            this.tv_ovulationDate.setText(ovulationDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    private String calcMenstrualDate(int cycleDay,Date lastDate){
        this.calendar.setTime(lastDate);
        long lastDateLong = this.calendar.getTimeInMillis();
        Date nextStartDate = new Date(lastDateLong + (cycleDay*CommonNotification.dayMillis));
        Date nextEndDate = new Date(nextStartDate.getTime()+(7*CommonNotification.dayMillis));

        return this.ft.format(nextStartDate)+"~"+this.ft.format(nextEndDate);
    }

    private String calcDangerousDate(int cycleDay,Date lastDate){
        this.calendar.setTime(lastDate);
        long lastDateLong = this.calendar.getTimeInMillis();
        long nextStartDateLong = lastDateLong + (cycleDay*CommonNotification.dayMillis);
        long ovulationDateLong = nextStartDateLong-(14*CommonNotification.dayMillis);

        Date dangerousStartDate = new Date(ovulationDateLong-(5*CommonNotification.dayMillis));
        Date dangerousEndDate = new Date(ovulationDateLong+(3*CommonNotification.dayMillis));
        return this.ft.format(dangerousStartDate)+"~"+this.ft.format(dangerousEndDate);
    }

    private String calcOvulationDate(int cycleDay,Date lastDate){
        this.calendar.setTime(lastDate);
        long lastDateLong = this.calendar.getTimeInMillis();
        long nextStartDateLong = lastDateLong + (cycleDay*CommonNotification.dayMillis);
        Date ovulationDate = new Date(nextStartDateLong-(15*CommonNotification.dayMillis));

        return this.ft.format(ovulationDate);
    }

//    private void checkValidity(){
//        if(this.et_cycle.getText().toString().matches("")){
//
//        }
//    }


}
