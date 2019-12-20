package com.example.fivegroup.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

public class CommonFragment extends Fragment {
    protected Activity context;
    public static final String CMD="CMD";
//    protected final long dayMillis = 24*60*60*1000;
//    protected final long hourMillis = 60*60*1000;
//    protected final long weekMillis = 7*dayMillis;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }

    public Bundle getBundleResult(){
        return null;
    }

    public String checkValidity(){
        return "";
    }

}
