package com.example.fivegroup.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.fivegroup.R;

import java.util.HashMap;

public class FragmentFreqActivePause extends CommonFragment {
    EditText et_x;
    EditText et_y;
    EditText et_z;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_freq_active_pause, container, false);

        this.et_x = root.findViewById(R.id.et_x);
        this.et_y = root.findViewById(R.id.et_y);
        this.et_z = root.findViewById(R.id.et_z);

        return root;
    }

    public static FragmentFreqActivePause newInstance() {
        FragmentFreqActivePause frag = new FragmentFreqActivePause();
        return frag;
    }

    @Override
    public HashMap getResult(){
        HashMap result = new HashMap();
        result.put("CMD","FREQ_ACTIVEPAUSE");
        result.put("X", Integer.parseInt(this.et_x.getText().toString()));
        result.put("Y", Integer.parseInt(this.et_y.getText().toString()));
        result.put("Z", Integer.parseInt(this.et_z.getText().toString()));
        return result;
    }

}
