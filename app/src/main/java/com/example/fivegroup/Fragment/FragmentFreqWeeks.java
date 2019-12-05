package com.example.fivegroup.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.fivegroup.R;

import java.util.HashMap;

public class FragmentFreqWeeks extends CommonFragment {
    CheckBox cb_week1, cb_week2, cb_week3, cb_week4, cb_week5, cb_week6, cb_week7;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_freq_weeks, container, false);
        cb_week1 = root.findViewById(R.id.cb_week1);
        cb_week2 = root.findViewById(R.id.cb_week2);
        cb_week3 = root.findViewById(R.id.cb_week3);
        cb_week4 = root.findViewById(R.id.cb_week4);
        cb_week5 = root.findViewById(R.id.cb_week5);
        cb_week6 = root.findViewById(R.id.cb_week6);
        cb_week7 = root.findViewById(R.id.cb_week7);

//        cb_week1.setOnCheckedChangeListener(checkBoxCheckedHandler);
//        cb_week2.setOnCheckedChangeListener(checkBoxCheckedHandler);
//        cb_week3.setOnCheckedChangeListener(checkBoxCheckedHandler);
//        cb_week4.setOnCheckedChangeListener(checkBoxCheckedHandler);
//        cb_week5.setOnCheckedChangeListener(checkBoxCheckedHandler);
//        cb_week6.setOnCheckedChangeListener(checkBoxCheckedHandler);
//        cb_week7.setOnCheckedChangeListener(checkBoxCheckedHandler);
        return root;
    }

    public static FragmentFreqWeeks newInstance() {
        FragmentFreqWeeks frag = new FragmentFreqWeeks();
        return frag;
    }

    @Override
    public HashMap getResult(){
        HashMap result = new HashMap();
        result.put("CMD","FREQ_WEEKS");
        result.put("WEEK1", (this.cb_week1.isChecked())?1:0);
        result.put("WEEK2", (this.cb_week2.isChecked())?1:0);
        result.put("WEEK3", (this.cb_week3.isChecked())?1:0);
        result.put("WEEK4", (this.cb_week4.isChecked())?1:0);
        result.put("WEEK5", (this.cb_week5.isChecked())?1:0);
        result.put("WEEK6", (this.cb_week6.isChecked())?1:0);
        result.put("WEEK7", (this.cb_week7.isChecked())?1:0);
        return result;
    }

//    CheckBox.OnCheckedChangeListener checkBoxCheckedHandler = new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CompoundButton compoundButton, boolean b/*selected*/) {
//            HashMap callBackResult = new HashMap();
//            Boolean[] booleans = new Boolean[]{false, false, false, false, false, false, false};
//            if (cb_week1.isChecked()) {
//                booleans[0] = true;
//            }
//            if (cb_week2.isChecked()) {
//                booleans[1] = true;
//            }
//            if (cb_week3.isChecked()) {
//                booleans[2] = true;
//            }
//            if (cb_week4.isChecked()) {
//                booleans[3] = true;
//            }
//            if (cb_week5.isChecked()) {
//                booleans[4] = true;
//            }
//            if (cb_week6.isChecked()) {
//                booleans[5] = true;
//            }
//            if (cb_week7.isChecked()) {
//                booleans[6] = true;
//            }
//            callBackResult.put("FREQ_WEEKS", booleans);
//            iFragmentCallBack.sendFragmentResult(callBackResult);
//        }
//    };

}
