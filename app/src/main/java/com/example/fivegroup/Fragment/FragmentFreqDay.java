package com.example.fivegroup.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.example.fivegroup.R;

public class FragmentFreqDay extends CommonFragment{
    private EditText et_x;
    public static final String FREQ_DAY="FREQ_DAY";
    public static final String FREQ_DAY_X="FREQ_DAY_X";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_freq_day, container, false);

        this.et_x = root.findViewById(R.id.et_x);
        if(getArguments()!=null && getArguments().containsKey(FragmentFreqDay.FREQ_DAY_X)){
            this.et_x.setText(Integer.toString(getArguments().getInt(FragmentFreqDay.FREQ_DAY_X)));
        }
        return root;
    }

    @Override
    public Bundle getBundleResult(){
        Bundle bundleData = new Bundle();
        bundleData.putString(CommonFragment.CMD,FragmentFreqDay.FREQ_DAY);
        bundleData.putInt(FragmentFreqDay.FREQ_DAY_X, Integer.parseInt(this.et_x.getText().toString()));
        return bundleData;
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
        return message;
    }

}
