package com.example.fivegroup;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fivegroup.AccessDB.NotificationDB;
import com.example.fivegroup.Fragment.CommonFragment;
import com.example.fivegroup.Fragment.FragmentDurCont;
import com.example.fivegroup.Fragment.FragmentDurEnddate;
import com.example.fivegroup.Fragment.FragmentFreqActivePause;
import com.example.fivegroup.Fragment.FragmentFreqDay;
import com.example.fivegroup.Fragment.FragmentFreqHour;
import com.example.fivegroup.Fragment.FragmentFreqTime;
import com.example.fivegroup.Fragment.FragmentFreqWeeks;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotificationDetailActivity extends AppCompatActivity{
    private static Context CONTEXT;

    //DB
    private DBhelper_Activity dbhelper;
    private SQLiteDatabase db;
    private NotificationDB notificationDB;

    //View
    private TextView tv_message;
    private EditText et_title;
    private TextView tv_startdate;
    private Button btn_chooseStartDate;
    private LinearLayout alarmTimeGroup;
    private TextView tv_alarmTime;
    private Button btn_chooseTime;
    private Spinner sp_frequency;//下拉式選單
    private Spinner sp_duration;
    private Switch sw_isActive;//開關鈕
    private Button btn_setting;
    private Button btn_delete;

    //Data
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Calendar calendar=Calendar.getInstance();//System.out.println(this.calendar.get(Calendar.DAY_OF_WEEK));//6 [星期日=1，星期一=2，星期二=3，星期三=4，星期四=5，星期五=6，星期六=7]
    private int _ID;//提醒流水號
    private int statusCode=0;//0=insert;1=update;2=delete
    private CommonFragment[] freqClassArr= new CommonFragment[]{new FragmentFreqDay(), new FragmentFreqTime(),new FragmentFreqHour(), new FragmentFreqWeeks(), new FragmentFreqActivePause()};
    private CommonFragment[] durClassArr= new CommonFragment[]{ new FragmentDurEnddate(), new FragmentDurCont()};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificationdetail);

        setContext(this);
        //DB
        this.dbhelper = new DBhelper_Activity(this);
        this.db = this.dbhelper.getWritableDatabase();
        this.notificationDB = new NotificationDB(this.db);

//        this.notificationDB.clearTotalNotificationData();

        //init View
        this.tv_message = findViewById(R.id.tv_message);
        this.et_title = findViewById(R.id.et_title);
        this.btn_chooseStartDate = findViewById(R.id.btn_chooseStartDate);
        this.tv_startdate = findViewById(R.id.tv_startdate);
        this.alarmTimeGroup = findViewById(R.id.alarmTimeGroup);
        this.tv_alarmTime = findViewById(R.id.tv_alarmTime);
        this.btn_chooseTime = findViewById(R.id.btn_chooseTime);
        this.sp_frequency = findViewById(R.id.sp_frequency);
        this.sp_duration=findViewById(R.id.sp_duration);
        this.sw_isActive = findViewById(R.id.sw_isActive);
        this.btn_setting=findViewById(R.id.btn_setting);
        this.btn_delete=findViewById(R.id.btn_delete);
        this.btn_delete.setVisibility(View.GONE);

        //設置清單選項
        this.sp_frequency.setAdapter(ArrayAdapter.createFromResource(this, R.array.notification_frequency, android.R.layout.simple_spinner_dropdown_item));
        this.sp_duration.setAdapter(ArrayAdapter.createFromResource(this, R.array.notification_duration, android.R.layout.simple_spinner_dropdown_item));

        //init Event
        this.btn_chooseStartDate.setOnClickListener(this.chooseStartDateClickHandler);
        this.btn_chooseTime.setOnClickListener(this.chooseTimeClickHandler);
        this.sp_frequency.setOnItemSelectedListener(this.frequencySelectedHandler);
        this.sp_duration.setOnItemSelectedListener(this.durationSelectedHandler);
        this.sw_isActive.setOnCheckedChangeListener(this.isActiveCheckedHandler);
        this.btn_setting.setOnClickListener(this.settingClickHandler);
        this.btn_delete.setOnClickListener(this.deleteClickHandler);

        //init View Value
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.statusCode=1;
            this._ID = bundle.getInt("_ID");
            String no_title = bundle.getString("no_title");
            String no_startdate = bundle.getString("no_startdate");
            int tableFreqIndex = bundle.getInt("no_frequency");
            int tableDurIndex = bundle.getInt("no_duration");
            int no_active = bundle.getInt("no_active");

            this.et_title.setText(no_title);
            this.tv_startdate.setText(no_startdate);
            this.sp_frequency.setSelection(tableFreqIndex);
            this.sp_duration.setSelection(tableDurIndex);
            this.sw_isActive.setChecked((no_active==0)?false:true);
            this.btn_setting.setText("修改");
            this.btn_delete.setVisibility(View.VISIBLE);
        }

        Locale.setDefault(Locale.TAIWAN);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            int tableFreqIndex = bundle.getInt("no_frequency");
            int tableDurIndex = bundle.getInt("no_duration");
            //freq
            CommonFragment f_fragment_freq = this.freqClassArr[tableFreqIndex];;
            f_fragment_freq.setArguments(this.notificationDB.getDBFrequency(this._ID,tableFreqIndex));
            //dur
            CommonFragment f_fragment_dur = this.durClassArr[tableDurIndex];
            f_fragment_dur.setArguments(this.notificationDB.getDBDuration(this._ID,tableDurIndex));
            //alarm
            ArrayList<Integer> alarmIdArr = this.notificationDB.getDBAlarmIdArrFromNoId(this._ID);
            if(alarmIdArr!=null){
                Bundle alarmData = this.notificationDB.getDBAlarm(alarmIdArr.get(0));
                String dateFormat = alarmData.getString("TIME");
                String[] dataArr = dateFormat.split(" ");
                this.tv_alarmTime.setText(dataArr[1]);
            }
//            //todo 查詢SQLite版本
//            String sql = "SELECT sqlite_version()";
//            Cursor c = db.rawQuery(sql,null);
////            System.out.println("sqlite version=>");
//            if(c.getCount()>0){
//                c.moveToFirst();
////                System.out.println(c.getString(0));//3.8.10.2
//                c.close();
//            }else{
//                Log.w("Warn==>","NotificationDetailActivity.onStart()=> sqlite version is no data!!!");
//            }
        }
    }

    View.OnClickListener chooseStartDateClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(NotificationDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);

                    String strDateFormat = turnDate2String(calendar.getTime());
                    String[] dataArr = strDateFormat.split(" ");
                    tv_startdate.setText(dataArr[0]);
                }
            }, year, month, day).show();
        }
    };

    View.OnClickListener chooseTimeClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            new TimePickerDialog(NotificationDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    long currentTimeMillis = System.currentTimeMillis();
                    calendar.setTimeInMillis(currentTimeMillis);
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    String strDateFormat = turnDate2String(calendar.getTime());
                    String[] dataArr = strDateFormat.split(" ");
                    tv_alarmTime.setText(dataArr[1]);
                }
            }, hour, minute, true).show();
        }
    };

    AdapterView.OnItemSelectedListener frequencySelectedHandler = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //System.out.println(adapterView.getSelectedItem());
//                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("f_fragment_freq");
//                    getSupportFragmentManager().beginTransaction()
//                            .remove(fragment)
//                            .commit();
            getSupportFragmentManager().beginTransaction()
                    //.add(R.id.container, FragmentFreqDay.newInstance() , "f_fragment")
                    //.add(R.id.fl_freq,  (CommonFragment)freqClassArr[i].getDeclaredMethod("newInstance", null/*方法需要的參數型態*/).invoke(null/*呼叫此方法的物件*/,null/*參數值*/), "f_fragment_freq")
                    //.replace(R.id.fl_freq,  (CommonFragment)freqClassArr[i].getDeclaredMethod("newInstance").invoke(null/*呼叫此方法的物件*/,(Object[])null/*參數值*/), "f_fragment_freq")
                    .replace(R.id.fl_freq, freqClassArr[i], "f_fragment_freq")
                    .commit();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    AdapterView.OnItemSelectedListener durationSelectedHandler = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fl_dur,  (CommonFragment)durClassArr[i].getDeclaredMethod("newInstance").invoke(null/*呼叫此方法的物件*/,(Object[])null/*參數值*/), "f_fragment_dur")
                    .replace(R.id.fl_dur, durClassArr[i], "f_fragment_dur")
                    .commit();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    CompoundButton.OnCheckedChangeListener isActiveCheckedHandler = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(statusCode!=0)return;
            if(compoundButton.isChecked()){
                SingleToast.show(NotificationDetailActivity.this,"啟用鬧鈴",Toast.LENGTH_SHORT);
            }else{
                SingleToast.show(NotificationDetailActivity.this,"停用鬧鈴",Toast.LENGTH_SHORT);
            }
        }
    };

    View.OnClickListener settingClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Get Params
            String no_title = et_title.getText().toString();
            String no_startdate = tv_startdate.getText().toString();
            String alarmTime = tv_alarmTime.getText().toString();
            int tableFreqIndex = sp_frequency.getSelectedItemPosition();
            int tableDurIndex = sp_duration.getSelectedItemPosition();
            boolean isActive = sw_isActive.isChecked();
            int no_active = (isActive)?1:0;

            //check Data
            if(!checkData(no_title,no_startdate,alarmTime,tableFreqIndex,tableDurIndex))return;

            Bundle resultFreq = freqClassArr[tableFreqIndex].getBundleResult();
            Bundle resultDur = durClassArr[tableDurIndex].getBundleResult();
            String cmdFreq =resultFreq.getString(CommonFragment.CMD);
            String cmdDur =resultDur.getString(CommonFragment.CMD);

            //update z value
            if(cmdFreq.equals(FragmentFreqActivePause.FREQ_ACTIVEPAUSE)){
                int x = resultFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X);
                int y = resultFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y);
                Date data = turnString2Date(no_startdate+" 00:00");
                int z;
                if(System.currentTimeMillis()>=data.getTime()){
                    z = (int)Math.ceil((System.currentTimeMillis() - data.getTime())/AlarmReceiver.dayMillis);
                    z = z%(x+y);
                    z++;
                }else{
                    z=0;
                }
                resultFreq.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z, z);
            }

            ArrayList<Integer> alarmIdArr;
            if(statusCode==0){
                //DB
                notificationDB.setDBNotification_Create(no_title,no_startdate,tableFreqIndex,tableDurIndex,no_active);
                _ID = notificationDB.getDBNotificationID_Last();
                notificationDB.setDBFrequency_Create(_ID,tableFreqIndex,resultFreq);
                notificationDB.setDBDuration_Create(_ID,tableDurIndex,resultDur);
            }else if(statusCode==1){
                //清除所有時間點活動
                alarmIdArr = notificationDB.getDBAlarmIdArrFromNoId(_ID);//取得所有alarmID值
                if(alarmIdArr!=null){
                    for(int i=0; i<alarmIdArr.size(); i++){
                        cancleAlarmReceiver(alarmIdArr.get(i));
                    }
                }

                //DB
                notificationDB.setDBNotification_Update(_ID,no_title,no_startdate,tableFreqIndex,tableDurIndex,no_active);
                if(notificationDB.checkHasFrequency(_ID,tableFreqIndex)){
                    notificationDB.setDBFrequency_Update(_ID,tableFreqIndex,resultFreq);
                }else{
                    notificationDB.setDBFrequency_Create(_ID,tableFreqIndex,resultFreq);
                }
                if(notificationDB.checkHasDuration(_ID,tableDurIndex)){
                    notificationDB.setDBDuration_Update(_ID,tableDurIndex,resultDur);
                }else{
                    notificationDB.setDBDuration_Create(_ID,tableDurIndex,resultDur);
                }
                notificationDB.setDBAlarm_DeleteFromNoId(_ID);
            }
            Date startDate = turnString2Date(no_startdate+" "+alarmTime);
            calendar.setTime(startDate);
            ArrayList<Long> currentAlarmTimeList = getTotalAlarmTimeInFreq(no_startdate,resultFreq, calendar.getTimeInMillis());

            long currentAlarmTime;
            String currentAlarmTimeFormat;
            //設定no_freq_time的所有新增多時間點
            if(cmdFreq.equals(FragmentFreqTime.FREQ_TIME)){
                for(int i=0; i<currentAlarmTimeList.size(); i++){
                    currentAlarmTime = currentAlarmTimeList.get(i);
                    calendar.setTimeInMillis(currentAlarmTime);
                    calendar.set(Calendar.YEAR, 1911);
                    calendar.set(Calendar.MONTH, 1);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    currentAlarmTimeFormat= turnDate2String(calendar.getTime());
                    notificationDB.setDBAlarm_Create(_ID,currentAlarmTimeFormat);
                }
            }

            //設置新的時間點活動
            if(isActive){
                Date newAlarmDate;
                boolean isPassDur;
                for(int i=0; i<currentAlarmTimeList.size(); i++) {
                    currentAlarmTime = currentAlarmTimeList.get(i);
                    while(currentAlarmTime<System.currentTimeMillis()){//如果時間點已超過，則不重新響鈴，直到找到符合頻率的下一個時間點為止
                        currentAlarmTime = calcNextTime(currentAlarmTime,resultFreq);
                    }
                    calendar.setTimeInMillis(currentAlarmTime);
                    currentAlarmTimeFormat= turnDate2String(calendar.getTime());
                    newAlarmDate = calendar.getTime();
                    startDate = turnString2Date(no_startdate+" 23:59");
                    isPassDur = checkInDuration(resultDur,startDate,newAlarmDate);
                    if(isPassDur){
                        notificationDB.setDBAlarm_Create(_ID, currentAlarmTimeFormat);
                        alarmIdArr = notificationDB.getDBAlarmIdArrFromNoId(_ID);//重取新的alarmID值
                        if(alarmIdArr!=null){
                            int lastAlarmID = alarmIdArr.get(0);
                            startAlarmReceiver(_ID, lastAlarmID, no_title, resultFreq, resultDur, no_startdate, currentAlarmTime);
                        }
                    }
                }
            }
            goBackActivity();
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
                            notificationDB.setDBNotification_Delete(_ID);
                            notificationDB.setDBFrequency_Delete(_ID);
                            notificationDB.setDBDuration_Delete(_ID);
                            notificationDB.setDBAlarm_DeleteFromNoId(_ID);
                            goBackActivity();
                        }
                    });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    private void goBackActivity(){
        finish();
    }

    //Util-------------------------------
    private boolean checkData(String no_title,String no_startdate,String alarmTime,int freqIndex,int durIndex){
        String checkMessage;
        if(no_title.equals("")){
            this.tv_message.setText("請輸入標題");
            return false;
        }
        if(no_startdate.equals("")){
            this.tv_message.setText("請輸入開始日期");
            return false;
        }
        if(alarmTime.equals("")){
            this.tv_message.setText("請輸入提醒時間");
            return false;
        }
        checkMessage = this.freqClassArr[freqIndex].checkValidity();
        if(!checkMessage.equals("")){
            this.tv_message.setText(checkMessage);
            return false;
        }

        checkMessage = this.durClassArr[durIndex].checkValidity();
        if(!checkMessage.equals("")){
            this.tv_message.setText(checkMessage);
            return false;
        }
        return true;
    }

    //設置鬧鐘，到點做事
    private void startAlarmReceiver(int noId, int lastAlarmID,String no_title,Bundle resultFreq,Bundle resultDur,String startDateStr,long currentAlarmTime){
        Log.d("ClassName=>","NotificationDetailActivity.class");
        Log.d("Method=>","startAlarmReceiver()");
        Log.d("Message=>","noId:"+noId+"; lastAlarmID:"+lastAlarmID+"; no_title:"+no_title+"; resultFreq:"+resultFreq.toString()+"; resultDur:"+resultDur.toString()+"; startDateStr:"+startDateStr+"; currentAlarmTime 2 Date:"+new Date(currentAlarmTime));
        Intent intent = new Intent(NotificationDetailActivity.this, AlarmReceiver.class);
        intent.putExtra("TITLE", no_title);
        intent.putExtra("FREQ_BUNDLE", resultFreq);
        intent.putExtra("DUR_BUNDLE", resultDur);
        intent.putExtra("START_DATE", startDateStr);
        intent.putExtra("CURRENT_ALARM_TIME", currentAlarmTime);
        intent.putExtra("REQUEST_CODE",lastAlarmID);
        intent.putExtra("NOID",noId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationDetailActivity.this, intent.getIntExtra("REQUEST_CODE",0)/*get new Data*/, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, currentAlarmTime, pendingIntent);
    }

    //找到指定的鬧鐘，並刪除
    private void cancleAlarmReceiver(int lastAlarmID){
        Intent intent = new Intent(NotificationDetailActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotificationDetailActivity.this, lastAlarmID, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * 依頻率取得所有提醒時間點
     * @param resultFreq
     * @param currentTimeInMillis
     * @return
     */
    private ArrayList<Long> getTotalAlarmTimeInFreq(String no_startdate,Bundle resultFreq, long currentTimeInMillis){
        ArrayList<Long> currentAlarmTimeList = new ArrayList<>();
        String cmdFreq = resultFreq.getString(CommonFragment.CMD);
        long currentAlarmTime;
        switch (cmdFreq){
            case FragmentFreqDay.FREQ_DAY:
                currentAlarmTime = currentTimeInMillis;
                currentAlarmTimeList.add(currentAlarmTime);
                break;
            case FragmentFreqTime.FREQ_TIME:
                if(resultFreq.containsKey(FragmentFreqTime.FREQ_TIME_LIST)){
                    ArrayList<String> newTimeList = resultFreq.getStringArrayList(FragmentFreqTime.FREQ_TIME_LIST);
                    Date date;
                    for(int i=0; i<newTimeList.size(); i++){
                        date = turnString2Date(no_startdate+" "+newTimeList.get(i));
                        currentAlarmTime = date.getTime();
                        currentAlarmTimeList.add(currentAlarmTime);
                    }
                }
                currentAlarmTime = currentTimeInMillis;
                currentAlarmTimeList.add(currentAlarmTime);
                break;
            case FragmentFreqHour.FREQ_HOUR:
                currentAlarmTime = currentTimeInMillis;
                currentAlarmTimeList.add(currentAlarmTime);
                break;
            case FragmentFreqWeeks.FREQ_WEEKS:
                int dayOfWeek;
                boolean isActive;
                for(int i=0; i<7; i++){
                    currentAlarmTime = currentTimeInMillis;
                    currentAlarmTime = currentAlarmTime + (i*AlarmReceiver.dayMillis);
                    this.calendar.setTimeInMillis(currentAlarmTime);
                    dayOfWeek = this.calendar.get(Calendar.DAY_OF_WEEK);//dayOfWeek [星期日=1，星期一=2，星期二=3，星期三=4，星期四=5，星期五=6，星期六=7]
                    isActive = (resultFreq.getInt("FREQ_WEEKS_WEEK"+dayOfWeek)==1)?true:false;
                    if(isActive){
                        currentAlarmTimeList.add(currentAlarmTime);
                    }
                }
                break;
            case FragmentFreqActivePause.FREQ_ACTIVEPAUSE:
                int x = resultFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X);
                int y = resultFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y);
                int z = resultFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z);
                currentAlarmTime = currentTimeInMillis;
                if(!(z<=x)){//如果不在持續天數中，則計算暫停天數之後的新時間
                    currentAlarmTime += (y+1)*AlarmReceiver.dayMillis;
                }
                currentAlarmTimeList.add(currentAlarmTime);
                break;
        }
        return currentAlarmTimeList;
    }

    /**
     * 判斷是否符合週期範圍
     */
    private boolean checkInDuration(Bundle resultDur, Date startDate, Date newAlarmDate) {
        String cmd = resultDur.getString(CommonFragment.CMD);
        Date durDate;
        durDate = new Date();
        switch (cmd) {
            case FragmentDurEnddate.DUR_ENDDATE:
                String endDAte = resultDur.getString(FragmentDurEnddate.DUR_ENDDATE_ENDDATE);
                endDAte += " 23:59";
                durDate = this.turnString2Date(endDAte);
                break;
            case FragmentDurCont.DUR_CONT:
                int x = resultDur.getInt(FragmentDurCont.DUR_CONT_X);
                durDate.setTime(startDate.getTime() + (x * AlarmReceiver.dayMillis));
                break;
        }
        if (newAlarmDate.before(durDate)) {
            return true;
        }
        return false;
    }

    private Date turnString2Date(String dateStr){
        Date date;
        try {
            date= this.ft.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String turnDate2String(Date date){
        return this.ft.format(date);
    }

    public long calcNextTime(long currentAlarmTime, Bundle bundleData){
        String cmd = (String)bundleData.get(CommonFragment.CMD);
        long calcMillis=0;
        switch (cmd){
            case FragmentFreqDay.FREQ_DAY:
                int everyXDays = bundleData.getInt(FragmentFreqDay.FREQ_DAY_X);
                calcMillis = everyXDays*AlarmReceiver.dayMillis;
                break;
            case FragmentFreqTime.FREQ_TIME:
                calcMillis = AlarmReceiver.dayMillis;
                break;
            case FragmentFreqHour.FREQ_HOUR:
                int spaceHour = bundleData.getInt(FragmentFreqHour.FREQ_HOUR_X);
                calcMillis = spaceHour*AlarmReceiver.hourMillis;
                break;
            case FragmentFreqWeeks.FREQ_WEEKS:
                calcMillis = 7*AlarmReceiver.dayMillis;
                break;
            case FragmentFreqActivePause.FREQ_ACTIVEPAUSE:
                int x = bundleData.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X);
                int y = bundleData.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y);
                int z = bundleData.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z);
                z=z+1;//循環第幾天
                if(z>(x+y))z=1;
                if(z<=x){//如果還在持續天數中
                    calcMillis = AlarmReceiver.dayMillis;
                }else{
                    calcMillis = (y+1)*AlarmReceiver.dayMillis;
                }
                break;
        }
        return currentAlarmTime + calcMillis;
    }

    //set get----------
    public static Context getContext() {
        return CONTEXT;
    }

    public static void setContext(Context context) {
        NotificationDetailActivity.CONTEXT = context;
    }

}
