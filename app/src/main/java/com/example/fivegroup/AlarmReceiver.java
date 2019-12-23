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
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import static android.content.Context.ALARM_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    public static final long dayMillis = 24*60*60*1000;
    public static final long hourMillis = 60*60*1000;

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
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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

        String cmdFreq = this.freqBundle.getString(CommonFragment.CMD);
        //update DB
        Bundle bundleData;
        switch (cmdFreq){
            case FragmentFreqHour.FREQ_HOUR:
                bundleData = new Bundle();
                bundleData.putString(CommonFragment.CMD,FragmentFreqHour.FREQ_HOUR);
                bundleData.putInt(FragmentFreqHour.FREQ_HOUR_X, this.freqBundle.getInt(FragmentFreqHour.FREQ_HOUR_X));
                bundleData.putString(FragmentFreqHour.FREQ_HOUR_FIRSTDATE, this.freqBundle.getString(FragmentFreqHour.FREQ_HOUR_FIRSTDATE));
                bundleData.putString(FragmentFreqHour.FREQ_HOUR_PREVDATE, this.ft.format(this.currentAlarmTime));
                notificationDB.setDBFrequency_Update(noId,2,bundleData);
                break;
            case FragmentFreqActivePause.FREQ_ACTIVEPAUSE:
                int x = this.freqBundle.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X);
                int y = this.freqBundle.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y);
                Date data = turnString2Date(this.startDateStr);
                int z;
                if(System.currentTimeMillis()>=data.getTime()){
                    z = (int)Math.ceil((System.currentTimeMillis() - data.getTime())/AlarmReceiver.dayMillis);
                    z = z%(x+y);
                    z++;
                }else{
                    z=0;
                }
                bundleData = new Bundle();
                bundleData.putString(CommonFragment.CMD,FragmentFreqActivePause.FREQ_ACTIVEPAUSE);
                bundleData.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X, this.freqBundle.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X));
                bundleData.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y, this.freqBundle.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y));
                bundleData.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z, z);
                notificationDB.setDBFrequency_Update(noId,4,bundleData);
                break;
        }

        if(this.currentAlarmTime!=0){
            long calcNextTime = this.calcNextTime(this.currentAlarmTime,this.freqBundle);
            String nextAlarmTimeFormat = this.ft.format(calcNextTime);

            this.startDateStr += " 23:59";
            Date startTimeDate = this.turnString2Date(this.startDateStr);
            boolean isPassDur = checkInDuration(this.durBundle, startTimeDate,new Date(calcNextTime));
            if(isPassDur){
                notificationDB.setDBAlarm_Create(this.noId,nextAlarmTimeFormat);
                ArrayList<Integer> alarmIdArr = notificationDB.getDBAlarmIdArrFromNoId(this.noId);
                if(alarmIdArr!=null){
                    int lastAlarmID = alarmIdArr.get(0);
                    this.startNextAlarmReceiver(this.noId,lastAlarmID,this.title,this.freqBundle,this.durBundle,this.startDateStr,calcNextTime);
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
            case FragmentFreqDay.FREQ_DAY:
                int everyXDays = bundleData.getInt(FragmentFreqDay.FREQ_DAY_X);
                calcMillis = everyXDays*this.dayMillis;
                break;
            case FragmentFreqTime.FREQ_TIME:
                calcMillis = this.dayMillis;
                break;
            case FragmentFreqHour.FREQ_HOUR:
                int spaceHour = bundleData.getInt(FragmentFreqHour.FREQ_HOUR_X);
                calcMillis = spaceHour*this.hourMillis;
                break;
            case FragmentFreqWeeks.FREQ_WEEKS:
                calcMillis = 7*this.dayMillis;
                break;
            case FragmentFreqActivePause.FREQ_ACTIVEPAUSE:
                int x = bundleData.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X);
                int y = bundleData.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y);
                int z = bundleData.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z);
                z=z+1;//循環第幾天
                if(z>(x+y))z=1;
                if(z<=x){//如果還在持續天數中
                    calcMillis = this.dayMillis;
                }else{
                    calcMillis = (y+1)*this.dayMillis;
                }
                break;
        }
        return currentAlarmTime + calcMillis;
//        return currentAlarmTime + (30*1000);//todo
    }

    public void startNextAlarmReceiver(int noId, int lastAlarmID,String title,Bundle freqBundle, Bundle durBundle, String startDateStr,long calcNextTime){
        Intent intent = new Intent(this.vContext, AlarmReceiver.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("FREQ_BUNDLE", freqBundle);
        intent.putExtra("DUR_BUNDLE", durBundle);
        intent.putExtra("START_DATE", startDateStr);
        intent.putExtra("CURRENT_ALARM_TIME", calcNextTime);
        intent.putExtra("REQUEST_CODE",lastAlarmID);
        intent.putExtra("NOID",noId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.vContext, intent.getIntExtra("REQUEST_CODE",0)/*get new Data*/, intent, 0);
        AlarmManager alarmManager = (AlarmManager) this.vContext.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calcNextTime, pendingIntent);
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
        SingleToast.show(this.vContext,"AlarmReceiver checkInDuration=>"+this.title+" "+newAlarmDate.toString()+" "+durDate.toString()+" "+newAlarmDate.before(durDate), Toast.LENGTH_LONG);
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

}
