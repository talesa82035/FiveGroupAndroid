package com.example.fivegroup.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.fivegroup.R;

import java.util.HashMap;

public class FragmentDurEnddate extends CommonFragment {
    EditText et_endDate;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dur_enddate, container, false);
        this.et_endDate = root.findViewById(R.id.et_endDate);

        return root;
    }

    public static FragmentDurEnddate newInstance() {
        FragmentDurEnddate frag = new FragmentDurEnddate();
        return frag;
    }

    @Override
    public void setResult(HashMap result){
        this.et_endDate.setText((String)result.get("no_dur_enddate"));
    }

    @Override
    public HashMap getResult(){
        HashMap result = new HashMap();
        result.put("CMD","DUR_ENDDATE");
        result.put("ENDDATE", this.et_endDate.getText().toString());
        return result;
    }

}
