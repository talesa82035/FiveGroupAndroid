package com.example.fivegroup.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.fivegroup.R;

import java.util.HashMap;

public class FragmentDurCont extends CommonFragment {
    EditText et_x;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dur_cont, container, false);

        this.et_x = root.findViewById(R.id.et_x);

        return root;
    }

    public static FragmentDurCont newInstance() {
        FragmentDurCont frag = new FragmentDurCont();
        return frag;
    }

    @Override
    public void setResult(HashMap result){
        this.et_x.setText(result.get("no_dur_day").toString());
    }

    @Override
    public HashMap getResult(){
        HashMap result = new HashMap();
        result.put("CMD","DUR_CONT");
        result.put("X", Integer.parseInt(this.et_x.getText().toString()));
        return result;
    }

}
