package com.example.fivegroup.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.fivegroup.R;

import java.util.HashMap;

public class FragmentFreqHour extends CommonFragment {
    EditText et_x;
    EditText et_firstDate;
    EditText et_prevDate;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_freq_hour, container, false);

        this.et_x = root.findViewById(R.id.et_x);
        this.et_firstDate = root.findViewById(R.id.et_firstDate);
        this.et_prevDate = root.findViewById(R.id.et_prevDate);

        return root;
    }

    public static FragmentFreqHour newInstance() {
        FragmentFreqHour frag = new FragmentFreqHour();
        return frag;
    }

    @Override
    public HashMap getResult(){
        HashMap result = new HashMap();
        result.put("CMD","FREQ_HOUR");
        result.put("X", Integer.parseInt(this.et_x.getText().toString()));
        result.put("FIRST_DATE", this.et_firstDate.getText().toString());
        result.put("PREV_DATE", this.et_prevDate.getText().toString());
        return result;
    }

}
