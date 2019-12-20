package com.example.fivegroup.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.example.fivegroup.R;

public class FragmentFreqWeeks extends CommonFragment {
    private CheckBox cb_week1, cb_week2, cb_week3, cb_week4, cb_week5, cb_week6, cb_week7;
    public static final String FREQ_WEEKS="FREQ_WEEKS";
    public static final String FREQ_WEEKS_WEEK1="FREQ_WEEKS_WEEK1";
    public static final String FREQ_WEEKS_WEEK2="FREQ_WEEKS_WEEK2";
    public static final String FREQ_WEEKS_WEEK3="FREQ_WEEKS_WEEK3";
    public static final String FREQ_WEEKS_WEEK4="FREQ_WEEKS_WEEK4";
    public static final String FREQ_WEEKS_WEEK5="FREQ_WEEKS_WEEK5";
    public static final String FREQ_WEEKS_WEEK6="FREQ_WEEKS_WEEK6";
    public static final String FREQ_WEEKS_WEEK7="FREQ_WEEKS_WEEK7";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_freq_weeks, container, false);
        this.cb_week1 = root.findViewById(R.id.cb_week1);
        this.cb_week2 = root.findViewById(R.id.cb_week2);
        this.cb_week3 = root.findViewById(R.id.cb_week3);
        this.cb_week4 = root.findViewById(R.id.cb_week4);
        this.cb_week5 = root.findViewById(R.id.cb_week5);
        this.cb_week6 = root.findViewById(R.id.cb_week6);
        this.cb_week7 = root.findViewById(R.id.cb_week7);
        if(getArguments()!=null && getArguments().containsKey(FragmentFreqWeeks.FREQ_WEEKS_WEEK1)
                && getArguments().containsKey(FragmentFreqWeeks.FREQ_WEEKS_WEEK2)
                && getArguments().containsKey(FragmentFreqWeeks.FREQ_WEEKS_WEEK3)
                && getArguments().containsKey(FragmentFreqWeeks.FREQ_WEEKS_WEEK4)
                && getArguments().containsKey(FragmentFreqWeeks.FREQ_WEEKS_WEEK5)
                && getArguments().containsKey(FragmentFreqWeeks.FREQ_WEEKS_WEEK6)
                && getArguments().containsKey(FragmentFreqWeeks.FREQ_WEEKS_WEEK7)){
            this.cb_week1.setChecked((getArguments().getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK1)==1)?true:false);
            this.cb_week2.setChecked((getArguments().getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK2)==1)?true:false);
            this.cb_week3.setChecked((getArguments().getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK3)==1)?true:false);
            this.cb_week4.setChecked((getArguments().getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK4)==1)?true:false);
            this.cb_week5.setChecked((getArguments().getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK5)==1)?true:false);
            this.cb_week6.setChecked((getArguments().getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK6)==1)?true:false);
            this.cb_week7.setChecked((getArguments().getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK7)==1)?true:false);
        }

        return root;
    }

    @Override
    public Bundle getBundleResult(){
        Bundle bundleData = new Bundle();
        bundleData.putString(CommonFragment.CMD,FragmentFreqWeeks.FREQ_WEEKS);
        bundleData.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK1, (this.cb_week1.isChecked())?1:0);
        bundleData.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK2, (this.cb_week2.isChecked())?1:0);
        bundleData.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK3, (this.cb_week3.isChecked())?1:0);
        bundleData.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK4, (this.cb_week4.isChecked())?1:0);
        bundleData.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK5, (this.cb_week5.isChecked())?1:0);
        bundleData.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK6, (this.cb_week6.isChecked())?1:0);
        bundleData.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK7, (this.cb_week7.isChecked())?1:0);
        return bundleData;
    }

    @Override
    public String checkValidity(){
        return "";
    }

}
