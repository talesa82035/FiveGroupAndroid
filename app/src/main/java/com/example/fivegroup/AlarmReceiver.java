package com.example.fivegroup;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import android.os.Vibrator;
import com.example.fivegroup.AccessDB.NotificationDB;
import com.example.fivegroup.Fragment.CommonFragment;
import com.example.fivegroup.Fragment.FragmentFreqActivePause;
import com.example.fivegroup.Fragment.FragmentFreqDay;
import com.example.fivegroup.Fragment.FragmentFreqHour;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import static android.content.Context.ALARM_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    public static final long dayMillis = 24*60*60*1000;
    public static final long hourMillis = 60*60*1000;
    public static final long weekMillis = 7*dayMillis;

    //DB
    private DBhelper_Activity dbhelper;
    private SQLiteDatabase db;
    private NotificationDB notificationDB;

    //Data
    private Context vContext;
    private Vibrator myVibrator;
    private Timer timer;
    private String title;
    private Bundle freqBundle;
    private long currentAlarmTime;
    private int requestCode;//=alarmID
    private int noId;
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public void onReceive(Context context, Intent intent) {
        this.vContext = context;
        this.title = intent.getStringExtra("TITLE");
        this.freqBundle = intent.getBundleExtra("FREQ_BUNDLE");
        this.currentAlarmTime = intent.getLongExtra("CURRENT_ALARM_TIME",0);
        this.requestCode = intent.getIntExtra("REQUEST_CODE",0);
        this.noId = intent.getIntExtra("NOID",0);

        //DB
        this.dbhelper = new DBhelper_Activity(this.vContext);
        this.db = this.dbhelper.getWritableDatabase();
        this.notificationDB = new NotificationDB(this.db);
        boolean isActive = this.notificationDB.checkAlarmIsActive(requestCode);
        if(!isActive)return;

        SingleToast.show(this.vContext,"鬧鈴時間到了："+this.title+"--"+this.currentAlarmTime+"--"+this.requestCode + "--"+isActive, Toast.LENGTH_LONG);

        //設置震動
//        this.myVibrator = (Vibrator)vContext.getSystemService(Service.VIBRATOR_SERVICE);
//        timer = new Timer();
//        TimerTask tt = new TimerTask() {
//            @Override
//            public void run() {
//                myVibrator.vibrate(1000);
//            }
//        };
//        timer.schedule(tt,100,2000);
//        this.confirmVibrate();
//
//        //設置鈴聲
//        this.playSound(context);

        if(this.currentAlarmTime!=0){
            long calcNextTime = this.calcNextTime(this.currentAlarmTime,this.freqBundle);
            String nextAlarmTimeFormat = this.ft.format(calcNextTime);
            notificationDB.setDBAlarm_Create(this.noId,nextAlarmTimeFormat);
            ArrayList<Integer> alarmIdArr = notificationDB.getDBAlarmIdArrFromNoId(this.noId);
            if(alarmIdArr!=null){
                int lastAlarmID = alarmIdArr.get(0);
                this.startNextAlarmReceiver(this.noId,lastAlarmID,this.title,this.freqBundle,calcNextTime);
            }
        }else{
            Log.w("Warn==>","AlarmReceiver.onReceive()=> CURRENT_ALARM_TIME is no data!!!");
        }
    }

    private void confirmVibrate(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.vContext);
        builder.setCancelable(true);
        builder.setTitle("關閉震動及響鈴");
        builder.setPositiveButton("關閉",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timer.cancel();
                        myVibrator.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.show();
    }

    public static int playSound(final Context context) {
        NotificationManager mgr = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification nt = new Notification();
        nt.defaults = Notification.DEFAULT_SOUND;
        int soundId = new Random(System.currentTimeMillis())
                .nextInt(Integer.MAX_VALUE);
        mgr.notify(soundId, nt);
        return soundId;
    }

    /**
     * 計算下一個鬧鈴時間
     * @param currentAlarmTime
     * @param bundleData
     * @return
     */
    public long calcNextTime(long currentAlarmTime, Bundle bundleData){
        String cmd = (String)bundleData.get(CommonFragment.CMD);
        long calcMillis=0;
        switch (cmd){
            case "FREQ_DAY"://no_freq_day
                int everyXDays = bundleData.getInt(FragmentFreqDay.FREQ_DAY_X);
                calcMillis = everyXDays*this.dayMillis;
                break;
            case "FREQ_TIME"://no_freq_time
                calcMillis = this.dayMillis;
                break;
            case "FREQ_HOUR"://no_freq_hour
                int spaceHour = bundleData.getInt(FragmentFreqHour.FREQ_HOUR_X);
                calcMillis = spaceHour*this.hourMillis;
                //todo 此處也要把上一次服藥時間的回傳值填回
                break;
            case "FREQ_WEEKS"://no_freq_week
                calcMillis = 7*this.dayMillis;
                break;
            case "FREQ_ACTIVEPAUSE"://no_freq_active_pause
                int x = bundleData.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X);
                int y = bundleData.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y);
                int z = bundleData.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z);
                z=z+1;//循環第幾天
                if(z>(x+y))z=1;//todo 此處要去呼叫setDB，把值存回去週期值，不然會怪怪的!!
                if(z<=x){//如果還在持續天數中
                    calcMillis = this.dayMillis;
                }else{
                    calcMillis = (y+1)*this.dayMillis;
                }
                break;
        }
        //return currentAlarmTime + calcMillis;
        return currentAlarmTime + (30*1000);//todo
    }

    public void startNextAlarmReceiver(int noId, int lastAlarmID,String title,Bundle freqBundle,long calcNextTime){
        Intent intent = new Intent(this.vContext, AlarmReceiver.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("FREQ_BUNDLE", freqBundle);
        intent.putExtra("CURRENT_ALARM_TIME", calcNextTime);
        intent.putExtra("REQUEST_CODE",lastAlarmID);
        intent.putExtra("NOID",noId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.vContext, intent.getIntExtra("REQUEST_CODE",0)/*get new Data*/, intent, 0);
        AlarmManager alarmManager = (AlarmManager) this.vContext.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calcNextTime, pendingIntent);
    }

}
