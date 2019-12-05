package com.example.fivegroup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.fivegroup.Fragment.CommonFragment;
import com.example.fivegroup.Fragment.FragmentDurCont;
import com.example.fivegroup.Fragment.FragmentDurEnddate;
import com.example.fivegroup.Fragment.FragmentFreqActivePause;
import com.example.fivegroup.Fragment.FragmentFreqDay;
import com.example.fivegroup.Fragment.FragmentFreqHour;
import com.example.fivegroup.Fragment.FragmentFreqTime;
import com.example.fivegroup.Fragment.FragmentFreqWeeks;
import com.example.fivegroup.Fragment.IFragmentCallBack;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.HashMap;

public class AlarmActivity extends AppCompatActivity /*implements IFragmentCallBack*/ {
    private static Context CONTEXT;

    //DB
    private static String DATABASE_TABLE = "notification";
    private static String[] DATABASE_TABLE_FREQUENCY = new String[]{"no_freq_day","no_freq_time","no_freq_hour","no_freq_week","no_freq_active_pause"};
    private static String[] DATABASE_TABLE_DURATION = new String[]{"no_dur_enddate","no_dur_cont"};
    private DBhelper_Activity dbhelper;
    private SQLiteDatabase db;

    //View
    private EditText et_title;
    private EditText et_startdate;
    private Spinner sp_frequency;//下拉式選單
    private Spinner sp_duration;
    private Switch sw_isActive;//開關鈕
    private Button btn_setting;
    private Button btn_cancle;
    private Button btn_delete;
    private TextView tv_info;

    //Data
    private Calendar calendar=Calendar.getInstance();
    private int _ID;//提醒流水號
    private int statusCode=0;//0=insert;1=update;2=delete
    private Class[] freqClassArr= new Class[]{FragmentFreqDay.class, FragmentFreqTime.class,FragmentFreqHour.class, FragmentFreqWeeks.class, FragmentFreqActivePause.class};
    private Class[] durClassArr= new Class[]{FragmentDurEnddate.class, FragmentDurCont.class};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        setContext(this);

        //DB
        dbhelper = new DBhelper_Activity(this);
        db = dbhelper.getWritableDatabase();

        //init View
        et_title = findViewById(R.id.et_title);
        et_startdate = findViewById(R.id.et_startdate);
        sp_frequency = findViewById(R.id.sp_frequency);
        sp_duration=findViewById(R.id.sp_duration);
        sw_isActive = findViewById(R.id.sw_isActive);
        btn_setting=findViewById(R.id.btn_setting);
        btn_cancle=findViewById(R.id.btn_cancle);
        btn_delete=findViewById(R.id.btn_delete);
        btn_delete.setVisibility(View.GONE);
        tv_info=findViewById(R.id.tv_info);

        //設置清單選項
        sp_frequency.setAdapter(ArrayAdapter.createFromResource(this, R.array.notification_frequency, android.R.layout.simple_spinner_dropdown_item));
        sp_duration.setAdapter(ArrayAdapter.createFromResource(this, R.array.notification_duration, android.R.layout.simple_spinner_dropdown_item));

        //init Event
        sp_frequency.setOnItemSelectedListener(frequencySelectedHandler);
        sp_duration.setOnItemSelectedListener(durationSelectedHandler);
        sw_isActive.setOnCheckedChangeListener(isActiveCheckedHandler);
        btn_setting.setOnClickListener(settingClickHandler);
        btn_cancle.setOnClickListener(cancleClickHandler);
        btn_delete.setOnClickListener(deleteClickHandler);

        //init View Value
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            statusCode=1;
            this._ID = bundle.getInt("_ID");
            String no_title = bundle.getString("no_title");
            String no_startdate = bundle.getString("no_startdate");
            int no_frequency = bundle.getInt("no_frequency");
            int no_duration = bundle.getInt("no_duration");
            int no_active = bundle.getInt("no_active");

            et_title.setText(no_title);
            et_startdate.setText(no_startdate);
            sp_frequency.setSelection(no_frequency);
            sp_duration.setSelection(no_duration);
            sw_isActive.setChecked((no_active==0)?false:true);
            btn_setting.setText("修改");
            btn_delete.setVisibility(View.VISIBLE);




        }
    }

    AdapterView.OnItemSelectedListener frequencySelectedHandler = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //System.out.println(adapterView.getSelectedItem());
            try {
//                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("f_fragment_freq");
//                    getSupportFragmentManager().beginTransaction()
//                            .remove(fragment)
//                            .commit();
                getSupportFragmentManager().beginTransaction()
                        //.add(R.id.container, FragmentFreqDay.newInstance() , "f_fragment")
                        //.add(R.id.container,  (FragmentFreqDay)FragmentFreqDay.class.getDeclaredMethod("newInstance", null/*方法需要的參數型態*/).invoke(null/*呼叫此方法的物件*/,null/*參數值*/), "f_fragment")
                        .replace(R.id.fl_freq,  (CommonFragment)freqClassArr[i].getDeclaredMethod("newInstance", null/*方法需要的參數型態*/).invoke(null/*呼叫此方法的物件*/,null/*參數值*/), "f_fragment_freq")
                        .commit();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    AdapterView.OnItemSelectedListener durationSelectedHandler = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_dur,  (CommonFragment)durClassArr[i].getDeclaredMethod("newInstance", null/*方法需要的參數型態*/).invoke(null/*呼叫此方法的物件*/,null/*參數值*/), "f_fragment_dur")
                        .commit();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    CompoundButton.OnCheckedChangeListener isActiveCheckedHandler = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(compoundButton.isChecked()){
                SingleToast.show(AlarmActivity.this,"啟用鬧鈴",Toast.LENGTH_SHORT);
            }else{
                SingleToast.show(AlarmActivity.this,"停用鬧鈴",Toast.LENGTH_SHORT);
            }
        }
    };

    View.OnClickListener settingClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            new TimePickerDialog(AlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    long currentTimeMillis = System.currentTimeMillis();
                    calendar.setTimeInMillis(currentTimeMillis);
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

//                        //設置時鐘，到點做事
//                        Intent intent = new Intent(AlarmActivity.this, AlarmReceiver.class);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, intent, 0);
//                        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//
//                        //顯示info
//                        tv_info.setText("設定鬧鐘時間為"+format(hourOfDay)+":"+format(minute));

                    //DB
                    setDB();
                    goBackActivity();
                }
            }, hour, minute, true).show();
        }
    };

    View.OnClickListener cancleClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(AlarmActivity.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            tv_info.setText("鬧鈴已取消！");
        }
    };

    View.OnClickListener deleteClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            statusCode=2;
            //彈出確認視窗
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(true);
            builder.setTitle("刪除鬧鈴");
            builder.setMessage("是否確定要刪除此鬧鈴？");
            builder.setPositiveButton("確定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setDB();
                            goBackActivity();
                        }
                    });
            //builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    private String format(int time){
        String str = ""+time;
        if(str.length() == 1){
            str = "0"+str;
        }
        return str;
    }



    //DB------------------------
    private void getNotificationID(){
        String sql = "SELECT * FROM " + DATABASE_TABLE + " ORDER BY _ID DESC LIMIT 0,1 ";//取得最後(最新)一筆資料的_ID
        Cursor c = db.rawQuery(sql,null);
        //if(c.getCount()==0)throw new Exception("查無任何資料!");
        if(c.getCount()>0){
            c.moveToFirst();
            this._ID = c.getInt(0);
            c.close();
        }
    }


    //Ava Test~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~未完成
    private void getDBFreq(int freqIndex, int noId){
        CommonFragment f_fragment_freq = (CommonFragment)getSupportFragmentManager().findFragmentByTag("f_fragment_freq");
        HashMap result = new HashMap();
        String tableName = DATABASE_TABLE_FREQUENCY[freqIndex];
        String sql = "SELECT * FROM " + tableName + " WHERE _ID= "+noId;
        Cursor c = db.rawQuery(sql,null);
        //if(c.getCount()==0)throw new Exception("查無任何資料!");
        if(c.getCount()>0){
            c.moveToFirst();
            switch (tableName){
                case "no_freq_day":
                    break;
                case "no_freq_time":
                    break;
                case "no_freq_hour":
                    break;
                case "no_freq_week":
                    break;
                case "no_freq_active_pause":
                    break;
            }
            c.close();
        }
        f_fragment_freq.setResult(result);
    }

    private void getDBDur(int durIndex, int noId){
        CommonFragment f_fragment_dur = (CommonFragment)getSupportFragmentManager().findFragmentByTag("f_fragment_dur");
        HashMap result = new HashMap();
        String tableName = DATABASE_TABLE_DURATION[durIndex];
        String sql = "SELECT * FROM " + tableName + " WHERE _ID= "+noId;
        Cursor c = db.rawQuery(sql,null);
        //if(c.getCount()==0)throw new Exception("查無任何資料!");
        if(c.getCount()>0){
            c.moveToFirst();
            switch (tableName){
                case "no_dur_enddate":
                    String durEnddate = c.getString(1);
                    result.put("no_dur_enddate",durEnddate);
                    break;
                case "no_dur_cont":
                    int durDay = c.getInt(1);
                    result.put("no_dur_day",durDay);
                    break;
            }
            c.close();
        }
        f_fragment_dur.setResult(result);
    }

    private void setDB(){
        String no_title = et_title.getText().toString();
        String no_startdate = et_startdate.getText().toString();
        int no_frequency = sp_frequency.getSelectedItemPosition();
        int no_duration = sp_duration.getSelectedItemPosition();
        int no_active = (sw_isActive.isChecked())?1:0;

        ContentValues cv = new ContentValues();
        cv.put("no_title",no_title);
        cv.put("no_startdate",no_startdate);
        cv.put("no_frequency",no_frequency);
        cv.put("no_duration",no_duration);
        cv.put("no_active",no_active);

        if(statusCode==0){
            db.insert(DATABASE_TABLE,null,cv);
            setDBFreq();
            setDBDur();
        }else if(statusCode==1){
            db.update(DATABASE_TABLE,cv,"_ID" +"="+this._ID,null);
        }else if(statusCode==2){
            db.delete(DATABASE_TABLE,"_ID" +"="+this._ID,null);
        }
    }

    private void setDBFreq(){
        int no_frequency = sp_frequency.getSelectedItemPosition();
        CommonFragment f_fragment_freq = (CommonFragment)getSupportFragmentManager().findFragmentByTag("f_fragment_freq");
        HashMap resultFreq = f_fragment_freq.getResult();
        ContentValues cvFreq = new ContentValues();
        switch (resultFreq.get("CMD").toString()){
            case "FREQ_DAY":
                int x1 = (int) resultFreq.get("X");
                cvFreq.put("no_xdays",x1);
                break;
            case "FREQ_TIME":
                int x2 = (int) resultFreq.get("X");
                cvFreq.put("no_daily_xtimes",x2);
                break;
            case "FREQ_HOUR":
                int x3 = (int) resultFreq.get("X");
                String firstDate = (String)resultFreq.get("FIRST_DATE");
                String prevDate = (String)resultFreq.get("PREV_DATE");
                cvFreq.put("no_sethour",x3);
                cvFreq.put("no_freq_starttime",firstDate);
                cvFreq.put("no_freq_lasttime",prevDate);
                break;
            case "FREQ_WEEKS":
                int week1 = (int) resultFreq.get("WEEK1");
                int week2 = (int) resultFreq.get("WEEK2");
                int week3 = (int) resultFreq.get("WEEK3");
                int week4 = (int) resultFreq.get("WEEK4");
                int week5 = (int) resultFreq.get("WEEK5");
                int week6 = (int) resultFreq.get("WEEK6");
                int week7 = (int) resultFreq.get("WEEK7");
                cvFreq.put("no_mon",week1);
                cvFreq.put("no_tue",week2);
                cvFreq.put("no_wed",week3);
                cvFreq.put("no_thu",week4);
                cvFreq.put("no_fri",week5);
                cvFreq.put("no_sat",week6);
                cvFreq.put("no_sun",week7);
                break;
            case "FREQ_ACTIVEPAUSE":
                int x4 = (int) resultFreq.get("X");
                int y = (int) resultFreq.get("Y");
                int z = (int) resultFreq.get("Z");
                cvFreq.put("no_freq_active",x4);
                cvFreq.put("no_freq_pause",y);
                cvFreq.put("no_cycle_xdays",z);
                break;
        }
        //System.out.println(f_fragment_dur instanceof );
        if(statusCode==0){
            this.getNotificationID();
            cvFreq.put("_ID",this._ID);
            db.insert(DATABASE_TABLE_FREQUENCY[no_frequency],null,cvFreq);
        }else if(statusCode==1){
            db.update(DATABASE_TABLE_FREQUENCY[no_frequency],cvFreq,"_ID" +"="+this._ID,null);
        }else if(statusCode==2){
            db.delete(DATABASE_TABLE_FREQUENCY[no_frequency],"_ID" +"="+this._ID,null);
        }
    }

    private void setDBDur(){
        int no_duration = sp_duration.getSelectedItemPosition();
        CommonFragment f_fragment_dur = (CommonFragment)getSupportFragmentManager().findFragmentByTag("f_fragment_dur");
        HashMap resultDur = f_fragment_dur.getResult();
        ContentValues cvDur = new ContentValues();
        switch (resultDur.get("CMD").toString()){
            case "DUR_ENDDATE":
                String enddate = (String)resultDur.get("ENDDATE");
                cvDur.put("no_dur_enddate",enddate);
                break;
            case "DUR_CONT":
                int x = (int) resultDur.get("X");
                cvDur.put("no_dur_day",x);
                break;
        }
        if(statusCode==0){
            this.getNotificationID();
            cvDur.put("_ID",this._ID);
            db.insert(DATABASE_TABLE_DURATION[no_duration],null,cvDur);
        }else if(statusCode==1){
            db.update(DATABASE_TABLE_DURATION[no_duration],cvDur,"_ID" +"="+this._ID,null);
        }else if(statusCode==2){
            db.delete(DATABASE_TABLE_DURATION[no_duration],"_ID" +"="+this._ID,null);
        }
    }

    private void goBackActivity(){
        finish();
    }
//    @Override
//    public void sendFragmentResult(HashMap result) {
//        System.out.println("--------sendFragmentResult--------");
//
//        Boolean[] resultFreqWeeks = (Boolean[])result.get("FREQ_WEEKS");
//        if(resultFreqWeeks!=null){
//            for(int i=0; i<resultFreqWeeks.length; i++){
//                System.out.println(resultFreqWeeks[i]);
//            }
//        }
//    }

    //set get----------
    public static Context getContext() {
        return CONTEXT;
    }

    public static void setContext(Context context) {
        AlarmActivity.CONTEXT = context;
    }

}
