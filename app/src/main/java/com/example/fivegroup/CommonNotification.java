package com.example.fivegroup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class CommonNotification {
    public static final long dayMillis = 24*60*60*1000;
    public static final long hourMillis = 60*60*1000;
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public CommonNotification(){
    }

    public String turnDate2String(Date date){
        return this.ft.format(date);
    }

    public String turnDate2String(Long timeInMillis){
        return this.ft.format(timeInMillis);
    }

    public Date turnString2Date(String dateStr){
        Date date;
        try {
            date= this.ft.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 設置鬧鐘，到點做事
     * @param context
     * @param noId
     * @param lastAlarmID
     * @param no_title
     * @param resultFreq
     * @param resultDur
     * @param startDateStr
     * @param currentAlarmTime
     */
    public void startAlarmReceiver(Context context, int noId, int lastAlarmID, String no_title, Bundle resultFreq, Bundle resultDur, String startDateStr, long currentAlarmTime){
        this.showLogDebug("CommonNotification.class",
                "startAlarmReceiver()",
                "noId:"+noId+"; lastAlarmID:"+lastAlarmID+"; no_title:"+no_title+"; resultFreq:"+resultFreq.toString()+"; resultDur:"+resultDur.toString()+"; startDateStr:"+startDateStr+"; currentAlarmTime 2 Date:"+new Date(currentAlarmTime));
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("TITLE", no_title);
        intent.putExtra("FREQ_BUNDLE", resultFreq);
        intent.putExtra("DUR_BUNDLE", resultDur);
        intent.putExtra("START_DATE", startDateStr);
        intent.putExtra("CURRENT_ALARM_TIME", currentAlarmTime);
        intent.putExtra("REQUEST_CODE",lastAlarmID);
        intent.putExtra("NOID",noId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, intent.getIntExtra("REQUEST_CODE",0)/*get new Data*/, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, currentAlarmTime, pendingIntent);
    }

    /**
     * 計算下一個鬧鈴時間
     * @param currentAlarmTime
     * @param bundleFreqData
     * @return
     */
    public long calcNextTime(long currentAlarmTime, Bundle bundleFreqData){
        String cmd = (String)bundleFreqData.get(CommonFragment.CMD);
        long calcMillis=0;
        switch (cmd){
            case FragmentFreqDay.FREQ_DAY:
                int everyXDays = bundleFreqData.getInt(FragmentFreqDay.FREQ_DAY_X);
                calcMillis = everyXDays*CommonNotification.dayMillis;
                break;
            case FragmentFreqTime.FREQ_TIME:
                calcMillis = CommonNotification.dayMillis;
                break;
            case FragmentFreqHour.FREQ_HOUR:
                int spaceHour = bundleFreqData.getInt(FragmentFreqHour.FREQ_HOUR_X);
                calcMillis = spaceHour*CommonNotification.hourMillis;
                break;
            case FragmentFreqWeeks.FREQ_WEEKS:
                calcMillis = 7*CommonNotification.dayMillis;
                break;
            case FragmentFreqActivePause.FREQ_ACTIVEPAUSE:
                int x = bundleFreqData.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X);
                int y = bundleFreqData.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y);
                int z = bundleFreqData.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z);
                z=z+1;//循環第幾天
                if(z>(x+y))z=1;
                if(z<=x){//如果還在持續天數中
                    calcMillis = CommonNotification.dayMillis;
                }else{
                    calcMillis = (y+1)*CommonNotification.dayMillis;
                }
                break;
        }
        return currentAlarmTime + calcMillis;
    }

    /**
     * 判斷是否符合週期範圍
     */
    public boolean checkInDuration(Bundle resultDur, Date startDate, Date newAlarmDate) {
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
                durDate.setTime(startDate.getTime() + (x * CommonNotification.dayMillis));
                break;
        }
        this.showLogDebug("CommonNotification.class",
                "checkInDuration()",
                "currentDate:"+newAlarmDate.toString()+"; EndDate:"+durDate.toString()+"; isPassDur:"+newAlarmDate.before(durDate));
        if (newAlarmDate.before(durDate)) {
            return true;
        }
        return false;
    }

    /**
     * 取得下一個頻率週期值
     * @param resultFreq
     * @param startDate
     * @return
     */
    public int getFreqActivePauseZValue(Bundle resultFreq, String startDate){
        String cmdFreq = resultFreq.getString(CommonFragment.CMD);
        if(cmdFreq.equals(FragmentFreqActivePause.FREQ_ACTIVEPAUSE)){
            int x = resultFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X);
            int y = resultFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y);
            Date data = this.turnString2Date(startDate+" 00:00");
            int z;
            if(System.currentTimeMillis()>=data.getTime()){
                z = (int)Math.ceil((System.currentTimeMillis() - data.getTime())/CommonNotification.dayMillis);
                z = z%(x+y);
                z++;
            }else{
                z=0;
            }
            return z;
        }
        return -1;
    }

    public void showLogDebug(String className, String MethodName, String message){
        Log.d("ClassName=>",className);
        Log.d("Method=>",MethodName);
        Log.d("Message=>",message);
    }

}
