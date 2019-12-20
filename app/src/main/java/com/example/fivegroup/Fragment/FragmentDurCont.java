package com.example.fivegroup.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.example.fivegroup.R;

public class FragmentDurCont extends CommonFragment {
    private EditText et_x;
    public static final String DUR_CONT="DUR_CONT";
    public static final String DUR_CONT_X="DUR_CONT_X";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dur_cont, container, false);

        this.et_x = root.findViewById(R.id.et_x);
        if(getArguments()!=null && getArguments().containsKey(FragmentDurCont.DUR_CONT_X)){
            this.et_x.setText(Integer.toString(getArguments().getInt(FragmentDurCont.DUR_CONT_X)));
        }
        return root;
    }

    @Override
    public Bundle getBundleResult(){
        Bundle bundleData = new Bundle();
        bundleData.putString(CommonFragment.CMD,FragmentDurCont.DUR_CONT);
        bundleData.putInt(FragmentDurCont.DUR_CONT_X, Integer.parseInt(this.et_x.getText().toString()));
        return bundleData;
    }

    @Override
    public String checkValidity(){
        String message="";
        String xStr = this.et_x.getText().toString();
        if(xStr.equals("")){
            message = "請輸入週期x值";
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
