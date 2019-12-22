package com.example.fivegroup.Fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.constraintlayout.widget.Constraints;
import com.example.fivegroup.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class FragmentFreqTime extends CommonFragment {
    private View root;
    private Context context;
    private LinearLayout ll_Container;
    private Button btn_addTime;
    public static final String FREQ_TIME="FREQ_TIME";
    public static final String FREQ_TIME_LIST="FREQ_TIME_LIST";
    private  ArrayList<HashMap> newTimeList = new ArrayList<>();
    private int newTimeLinearLayoutIndex=0;
    private Calendar calendar=Calendar.getInstance();
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_freq_time, container, false);
        this.context = root.getContext();

        this.ll_Container = root.findViewById(R.id.ll_Container);
        this.btn_addTime = root.findViewById(R.id.btn_addTime);
        this.btn_addTime.setOnClickListener(this.addNewTimeClickHandler);
        this.calendar.setTimeInMillis(System.currentTimeMillis());
        if(getArguments()!=null && getArguments().containsKey(FragmentFreqTime.FREQ_TIME_LIST)){
            ArrayList<String> newAlarmTimeList = getArguments().getStringArrayList(FragmentFreqTime.FREQ_TIME_LIST);
            for(int i=1; i<newAlarmTimeList.size(); i++){
                this.addNewTime(newAlarmTimeList.get(i).split(" ")[1]);
            }
        }
        return root;
    }

    @Override
    public Bundle getBundleResult(){
        Bundle bundleData = new Bundle();
        bundleData.putString(CommonFragment.CMD,FragmentFreqTime.FREQ_TIME);
        HashMap newTimeObj;
        ArrayList<String> newTimeList = new ArrayList<>();
        for(int i=this.newTimeList.size()-1; i>=0; i--){
            if(this.newTimeList.get(i)!=null){
                newTimeObj = this.newTimeList.get(i);
                newTimeList.add((String) newTimeObj.get("FORMAT_DATE"));
            }
        }
        if(newTimeList.size()>0){
            bundleData.putStringArrayList(FragmentFreqTime.FREQ_TIME_LIST,newTimeList);
        }
        return bundleData;
    }

    View.OnClickListener addNewTimeClickHandler = new View.OnClickListener() {
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
                    String str = ft.format(calendar.getTime());
                    addNewTime(str.split(" ")[1]);
                }
            }, hour, minute, true).show();
        }
    };

    View.OnClickListener removeNewTimeClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            HashMap newTimeObj = newTimeList.get(view.getId());
            removeNewTimer((LinearLayout) newTimeObj.get("LINEARLAYOUT"));
            newTimeList.set(view.getId(),null);//釋放資源
        }
    };

    public void addNewTime(String strDateFormat){
        TextView tv = new TextView(this.context);
        tv.setText("時間點:");
        tv.setLayoutParams(new LinearLayout.LayoutParams(Constraints.LayoutParams.WRAP_CONTENT,Constraints.LayoutParams.WRAP_CONTENT));

        TextView tv2 = new TextView(this.context);
        tv2.setWidth(200);
        tv2.setTextSize(18);
        tv2.setText(strDateFormat);
        tv2.setLayoutParams(new LinearLayout.LayoutParams(Constraints.LayoutParams.WRAP_CONTENT,Constraints.LayoutParams.WRAP_CONTENT));

        Button btnDeleteTime = new Button(this.context);
        btnDeleteTime.setId(this.newTimeLinearLayoutIndex);
        btnDeleteTime.setText("刪除");
        btnDeleteTime.setLayoutParams(new LinearLayout.LayoutParams(Constraints.LayoutParams.MATCH_PARENT,Constraints.LayoutParams.WRAP_CONTENT));
        btnDeleteTime.setOnClickListener(this.removeNewTimeClickHandler);

        LinearLayout linearLayout = new LinearLayout(this.context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(tv);
        linearLayout.addView(tv2);
        linearLayout.addView(btnDeleteTime);

        this.ll_Container.addView(linearLayout);

        HashMap newTimeObj = new HashMap();
//        newTimeObj.put("TV_LABEL",tv);
//        newTimeObj.put("TV_VALUE",tv2);
//        newTimeObj.put("BTN_DELETE_TIME",btnDeleteTime);
        newTimeObj.put("LINEARLAYOUT",linearLayout);
//        newTimeObj.put("CURRENT_ALARM_TIME", calendar.getTimeInMillis());
        newTimeObj.put("FORMAT_DATE", strDateFormat);
        this.newTimeList.add(newTimeObj);
        this.newTimeLinearLayoutIndex++;
    }

    public void removeNewTimer(LinearLayout linearLayout){
        this.ll_Container.removeView(linearLayout);
    }

    @Override
    public String checkValidity(){
        return "";
    }

}
