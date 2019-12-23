package com.example.fivegroup.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import com.example.fivegroup.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentDurEnddate extends CommonFragment {
    private Context context;
    private TextView tv_endDate;
    private Button btnChooseEnddate;
    public static final String DUR_ENDDATE="DUR_ENDDATE";
    public static final String DUR_ENDDATE_ENDDATE="DUR_ENDDATE_ENDDATE";
    private Calendar calendar=Calendar.getInstance();//System.out.println(this.calendar.get(Calendar.DAY_OF_WEEK));//6 [星期日=1，星期一=2，星期二=3，星期三=4，星期四=5，星期五=6，星期六=7]
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dur_enddate, container, false);
        this.context = root.getContext();
        this.tv_endDate = root.findViewById(R.id.tv_endDate);
        this.btnChooseEnddate = root.findViewById(R.id.btnChooseEnddate);
        if(getArguments()!=null && getArguments().containsKey(FragmentDurEnddate.DUR_ENDDATE_ENDDATE)){
            this.tv_endDate.setText(getArguments().getString(FragmentDurEnddate.DUR_ENDDATE_ENDDATE));
        }
        this.btnChooseEnddate.setOnClickListener(this.onChooseEnddateClickHandler);
        return root;
    }

    @Override
    public Bundle getBundleResult(){
        Bundle bundleData = new Bundle();
        bundleData.putString(CommonFragment.CMD,FragmentDurEnddate.DUR_ENDDATE);
        bundleData.putString(FragmentDurEnddate.DUR_ENDDATE_ENDDATE, this.tv_endDate.getText().toString());
        return bundleData;
    }

    @Override
    public String checkValidity(){
        return "";
    }

    View.OnClickListener onChooseEnddateClickHandler = new View.OnClickListener() {
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
                    tv_endDate.setText(dataArr[0]);
                }
            }, year, month, day).show();
        }
    };

}
