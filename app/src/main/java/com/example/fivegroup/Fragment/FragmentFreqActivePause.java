package com.example.fivegroup.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fivegroup.R;

public class FragmentFreqActivePause extends CommonFragment {
    private EditText et_x;
    private EditText et_y;
    private TextView tv_z;
    public static final String FREQ_ACTIVEPAUSE="FREQ_ACTIVEPAUSE";
    public static final String FREQ_ACTIVEPAUSE_X="FREQ_ACTIVEPAUSE_X";
    public static final String FREQ_ACTIVEPAUSE_Y="FREQ_ACTIVEPAUSE_Y";
    public static final String FREQ_ACTIVEPAUSE_Z="FREQ_ACTIVEPAUSE_Z";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_freq_active_pause, container, false);

        this.et_x = root.findViewById(R.id.et_x);
        this.et_y = root.findViewById(R.id.et_y);
        this.tv_z = root.findViewById(R.id.tv_z);
        if(getArguments()!=null && getArguments().containsKey(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X) && getArguments().containsKey(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y) && getArguments().containsKey(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z)){
            this.et_x.setText(Integer.toString(getArguments().getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X)));
            this.et_y.setText(Integer.toString(getArguments().getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y)));
            this.tv_z.setText(Integer.toString(getArguments().getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z)));
        }

        return root;
    }

    @Override
    public Bundle getBundleResult(){
        Bundle bundleData = new Bundle();
        bundleData.putString(CommonFragment.CMD,FragmentFreqActivePause.FREQ_ACTIVEPAUSE);
        bundleData.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X, Integer.parseInt(this.et_x.getText().toString()));
        bundleData.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y, Integer.parseInt(this.et_y.getText().toString()));
        if(!this.tv_z.getText().toString().matches("")){
            bundleData.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z, Integer.parseInt(this.tv_z.getText().toString()));
        }
        return bundleData;
    }

    @Override
    public String checkValidity(){
        String message="";
        String xStr = this.et_x.getText().toString();
        String yStr = this.et_y.getText().toString();
//        String zStr = this.tv_z.getText().toString();
        if(xStr.equals("")){
            message = "請輸入頻率x值";
            return message;
        }
        int xInt = Integer.parseInt(xStr);
        if(xInt<=0){
            message = "X值必須大於0";
            return message;
        }
        if(yStr.equals("")){
            message = "請輸入頻率y值";
            return message;
        }
        int yInt = Integer.parseInt(yStr);
        if(yInt<0){
            message = "Y值不能小於0";
            return message;
        }
//        if(zStr.equals("")){
//            message = "請輸入頻率z值";
//            return message;
//        }
//        int zInt = Integer.parseInt(zStr);
//        if(zInt<=0){
//            message = "Z值必須大於0";
//            return message;
//        }
//        if(zInt>(xInt+yInt)){
//            message = "當前第幾天，不得小於0，或是大於持續天數+暫停天數";
//            return message;
//        }
        return message;
    }

}
