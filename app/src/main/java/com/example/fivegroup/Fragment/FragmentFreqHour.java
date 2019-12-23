package com.example.fivegroup.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import com.example.fivegroup.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentFreqHour extends CommonFragment {
    private Context context;
    private EditText et_x;
    private TextView tv_firstDate;
    private TextView tv_prevDate;
    private Button btnFirstDate_Date;
    private Button btnFirstDate_Time;
    private Button btnPrevDate_Date;
    private Button btnPrevDate_Time;
    public static final String FREQ_HOUR="FREQ_HOUR";
    public static final String FREQ_HOUR_X="FREQ_HOUR_X";
    public static final String FREQ_HOUR_FIRSTDATE="FREQ_HOUR_FIRSTDATE";
    public static final String FREQ_HOUR_PREVDATE="FREQ_HOUR_PREVDATE";

    private Calendar calendar=Calendar.getInstance();
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String firstDate_Date="";
    private String firstDate_Time="";
    private String prevDate_Date="";
    private String prevDate_Time="";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_freq_hour, container, false);
        this.context = root.getContext();
        this.et_x = root.findViewById(R.id.et_x);
        this.tv_firstDate = root.findViewById(R.id.tv_firstDate);
        this.tv_prevDate = root.findViewById(R.id.tv_prevDate);
        this.btnFirstDate_Date = root.findViewById(R.id.btnFirstDate_Date);
        this.btnFirstDate_Time = root.findViewById(R.id.btnFirstDate_Time);
        this.btnPrevDate_Date = root.findViewById(R.id.btnPrevDate_Date);
        this.btnPrevDate_Time = root.findViewById(R.id.btnPrevDate_Time);
        if(getArguments()!=null && getArguments().containsKey(FragmentFreqHour.FREQ_HOUR_X) && getArguments().containsKey(FragmentFreqHour.FREQ_HOUR_FIRSTDATE) && getArguments().containsKey(FragmentFreqHour.FREQ_HOUR_PREVDATE)){
            this.et_x.setText(Integer.toString(getArguments().getInt(FragmentFreqHour.FREQ_HOUR_X)));
            this.updateFirstDateText(getArguments().getString(FragmentFreqHour.FREQ_HOUR_FIRSTDATE));
            this.updatePrevDateText(getArguments().getString(FragmentFreqHour.FREQ_HOUR_PREVDATE));
        }

        this.btnFirstDate_Date.setOnClickListener(this.chooseFirstDateClickHandler);
        this.btnFirstDate_Time.setOnClickListener(this.chooseFirstTimeClickHandler);
        this.btnPrevDate_Date.setOnClickListener(this.choosePrevDateClickHandler);
        this.btnPrevDate_Time.setOnClickListener(this.choosePrevTimeClickHandler);

        this.calendar.setTimeInMillis(System.currentTimeMillis());

        return root;
    }

    @Override
    public Bundle getBundleResult(){
        Bundle bundleData = new Bundle();
        bundleData.putString(CommonFragment.CMD,FragmentFreqHour.FREQ_HOUR);
        bundleData.putInt(FragmentFreqHour.FREQ_HOUR_X, Integer.parseInt(this.et_x.getText().toString()));
        if(!this.tv_firstDate.getText().toString().matches("")){
            bundleData.putString(FragmentFreqHour.FREQ_HOUR_FIRSTDATE, this.tv_firstDate.getText().toString());
        }
        if(!this.tv_prevDate.getText().toString().matches("")){
            bundleData.putString(FragmentFreqHour.FREQ_HOUR_PREVDATE, this.tv_prevDate.getText().toString());
        }
        return bundleData;
    }

    View.OnClickListener chooseFirstDateClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);

                    String strDateFormat = ft.format(calendar.getTime());
                    String[] dataArr = strDateFormat.split(" ");
                    firstDate_Date = dataArr[0];
                    updateFirstDateText(firstDate_Date+" "+firstDate_Time);
                }
            }, year, month, day).show();
        }
    };

    View.OnClickListener chooseFirstTimeClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    String strDateFormat = ft.format(calendar.getTime());
                    String[] dataArr = strDateFormat.split(" ");
                    firstDate_Time = dataArr[1];
                    updateFirstDateText(firstDate_Date+" "+firstDate_Time);
                }
            }, hour, minute, true).show();
        }
    };

    View.OnClickListener choosePrevDateClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);

                    String strDateFormat = ft.format(calendar.getTime());
                    String[] dataArr = strDateFormat.split(" ");
                    prevDate_Date = dataArr[0];
                    updatePrevDateText(prevDate_Date+" "+prevDate_Time);
                }
            }, year, month, day).show();
        }
    };

    View.OnClickListener choosePrevTimeClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    String strDateFormat = ft.format(calendar.getTime());
                    String[] dataArr = strDateFormat.split(" ");
                    prevDate_Time = dataArr[1];
                    updatePrevDateText(prevDate_Date+" "+prevDate_Time);
                }
            }, hour, minute, true).show();
        }
    };

    private void updateFirstDateText(String text){
        this.tv_firstDate.setText(text);
    }

    private void updatePrevDateText(String text){
        this.tv_prevDate.setText(text);
    }

    @Override
    public String checkValidity(){
        String message="";
        String xStr = this.et_x.getText().toString();
        if(xStr.equals("")){
            message = "請輸入頻率x值";
            return message;
        }
        int xInt = Integer.parseInt(xStr);
        if(xInt<=0){
            message = "X值必須大於0";
            return message;
        }
        if(!this.tv_firstDate.getText().toString().matches("")){
            if(this.firstDate_Date=="" || this.firstDate_Time=="")
            message = "欲輸入首次服藥時間，請填寫完整的日期與時間";
            return message;
        }
//        if(!this.tv_prevDate.getText().toString().matches("")){
//            if(this.prevDate_Date=="" || this.prevDate_Time=="")
//                message = "欲輸入上一次服藥時間，請填寫完整的日期與時間";
//            return message;
//        }
        return message;
    }
}
