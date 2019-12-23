package com.example.fivegroup;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
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
import com.example.fivegroup.Fragment.FragmentDurCont;
import com.example.fivegroup.Fragment.FragmentDurEnddate;
import com.example.fivegroup.Fragment.FragmentFreqActivePause;
import com.example.fivegroup.Fragment.FragmentFreqDay;
import com.example.fivegroup.Fragment.FragmentFreqHour;
import com.example.fivegroup.Fragment.FragmentFreqTime;
import com.example.fivegroup.Fragment.FragmentFreqWeeks;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmReceiver extends BroadcastReceiver {
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
    private Bundle durBundle;
    private String startDateStr;
    private long currentAlarmTime;
    private int requestCode;//=alarmID
    private int noId;
    private CommonNotification commonNotification = new CommonNotification();

    @Override
    public void onReceive(Context context, Intent intent) {
        this.vContext = context;
        this.title = intent.getStringExtra("TITLE");
        this.freqBundle = intent.getBundleExtra("FREQ_BUNDLE");
        this.durBundle = intent.getBundleExtra("DUR_BUNDLE");
        this.startDateStr = intent.getStringExtra("START_DATE");
        this.currentAlarmTime = intent.getLongExtra("CURRENT_ALARM_TIME",0);
        this.requestCode = intent.getIntExtra("REQUEST_CODE",0);
        this.noId = intent.getIntExtra("NOID",0);

        //DB
        this.dbhelper = new DBhelper_Activity(this.vContext);
        this.db = this.dbhelper.getWritableDatabase();
        this.notificationDB = new NotificationDB(this.db);
        boolean isActive = this.notificationDB.checkAlarmIsActive(requestCode);
        if(!isActive)return;

        SingleToast.show(this.vContext,"鬧鈴時間到了："+this.title, Toast.LENGTH_LONG);

//        //設置震動
//        this.myVibrator = (Vibrator)vContext.getSystemService(Service.VIBRATOR_SERVICE);
//        this.timer = new Timer();
//        TimerTask tt = new TimerTask() {
//            @Override
//            public void run() {
//                myVibrator.vibrate(1000);
//            }
//        };
//        this.timer.schedule(tt,100,2000);
//        this.confirmVibrate();
//
//        //設置鈴聲
//        this.playSound(context);

        String cmdFreq = this.freqBundle.getString(CommonFragment.CMD);
        //update DB
        Bundle bundleData;
        switch (cmdFreq){
            case FragmentFreqHour.FREQ_HOUR:
                bundleData = new Bundle();
                bundleData.putString(CommonFragment.CMD,FragmentFreqHour.FREQ_HOUR);
                bundleData.putInt(FragmentFreqHour.FREQ_HOUR_X, this.freqBundle.getInt(FragmentFreqHour.FREQ_HOUR_X));
                bundleData.putString(FragmentFreqHour.FREQ_HOUR_FIRSTDATE, this.freqBundle.getString(FragmentFreqHour.FREQ_HOUR_FIRSTDATE));
                bundleData.putString(FragmentFreqHour.FREQ_HOUR_PREVDATE, this.commonNotification.turnDate2String(this.currentAlarmTime));
                this.notificationDB.setDBFrequency_Update(this.noId,2,bundleData);
                break;
            case FragmentFreqActivePause.FREQ_ACTIVEPAUSE:
                int z = this.commonNotification.getFreqActivePauseZValue(this.freqBundle,this.startDateStr);
                bundleData = new Bundle();
                bundleData.putString(CommonFragment.CMD,FragmentFreqActivePause.FREQ_ACTIVEPAUSE);
                bundleData.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X, this.freqBundle.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X));
                bundleData.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y, this.freqBundle.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y));
                bundleData.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z, z);
                this.notificationDB.setDBFrequency_Update(this.noId,4,bundleData);
                break;
        }

        if(this.currentAlarmTime!=0){
            long calcNextTime = commonNotification.calcNextTime(this.currentAlarmTime,this.freqBundle);
            String nextAlarmTimeFormat = this.commonNotification.turnDate2String(calcNextTime);

            Date startTimeDate = this.commonNotification.turnString2Date(this.startDateStr+" 23:59");
            boolean isPassDur = commonNotification.checkInDuration(this.durBundle, startTimeDate,new Date(calcNextTime));
            if(isPassDur){
                this.notificationDB.setDBAlarm_Create(this.noId,nextAlarmTimeFormat);
                ArrayList<Integer> alarmIdArr = this.notificationDB.getDBAlarmIdArrFromNoId(this.noId);
                if(alarmIdArr!=null){
                    int lastAlarmID = alarmIdArr.get(0);
                    this.commonNotification.startAlarmReceiver(this.vContext,this.noId,lastAlarmID,this.title,this.freqBundle,this.durBundle,this.startDateStr,calcNextTime);
                }
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
        dialog.setCancelable(false);
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

}
