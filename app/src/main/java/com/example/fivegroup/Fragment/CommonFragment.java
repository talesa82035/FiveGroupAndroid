package com.example.fivegroup.Fragment;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

public class CommonFragment extends Fragment {
    protected Activity context;
//    protected IFragmentCallBack iFragmentCallBack;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
//        iFragmentCallBack = (IFragmentCallBack) getActivity();
        //mParam = getArguments().getString(ARG_PARAM);  //获取参数
    }

    public void setResult(HashMap result){}

    //for activity call
    public HashMap getResult(){
        return null;
    }

}
