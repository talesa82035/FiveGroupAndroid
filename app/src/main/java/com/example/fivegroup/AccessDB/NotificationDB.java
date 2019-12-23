package com.example.fivegroup.AccessDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.ArrayList;
import java.util.Date;

public class NotificationDB {
    private static final String DATABASE_TABLE = "notification";
    private static final String DATABASE_TABLE_ALARM = "no_alarm";
    private static final String[] DATABASE_TABLE_FREQUENCY = new String[]{"no_freq_day", "no_freq_time", "no_freq_hour", "no_freq_week", "no_freq_active_pause"};
    private static final String[] DATABASE_TABLE_DURATION = new String[]{"no_dur_enddate", "no_dur_cont"};
    private SQLiteDatabase db;
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public NotificationDB(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 查詢SQLite版本
     */
    public String getSQLiteVersion(){
        String verison="";
        String sql = "SELECT sqlite_version()";
        Cursor c = db.rawQuery(sql,null);
        if(c.getCount()>0){
            c.moveToFirst();
            verison = c.getString(0);//3.8.10.2
            c.close();
        }else{
            Log.w("Warn==>","NotificationDB.getSQLiteVersion()=> sqlite version is no data!!!");
        }
        return verison;
    }

    //Notification-------------------------------------------------------------------------
    public void setDBNotification_Create(String no_title, String no_startdate, int no_frequency, int no_duration, int no_active) {
        ContentValues cv = new ContentValues();
        cv.put("no_title", no_title);
        cv.put("no_startdate", no_startdate);
        cv.put("no_frequency", no_frequency);
        cv.put("no_duration", no_duration);
        cv.put("no_active", no_active);
        this.db.insert(DATABASE_TABLE, null, cv);
    }

    public void setDBNotification_Update(int noId, String no_title, String no_startdate, int no_frequency, int no_duration, int no_active) {
        if (noId == -1 || no_title == null || no_startdate == null || no_frequency == -1 || no_duration == -1 || no_active == -1) {
            Log.w("Warn==>", "NotificationDB.setDBNotification_Update()=> run insert or update have no data!!");
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put("no_title", no_title);
        cv.put("no_startdate", no_startdate);
        cv.put("no_frequency", no_frequency);
        cv.put("no_duration", no_duration);
        cv.put("no_active", no_active);
        this.db.update(this.DATABASE_TABLE, cv, "_ID" + "=" + noId, null);
    }

    public void setDBNotification_Delete(int noId) {
        this.db.delete(this.DATABASE_TABLE, "_ID" + "=" + noId, null);
    }

    public int getDBNotificationID_Last() {
        int noId = -1;
        String sql = "SELECT * FROM " + this.DATABASE_TABLE + " ORDER BY _ID DESC LIMIT 0,1 ";//取得最後(最新)一筆資料的_ID
        Cursor c = this.db.rawQuery(sql, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            noId = c.getInt(0);
            c.close();
        } else {
            Log.w("Warn==>", "NotificationDB.getDBNotificationID_Last()=> is no data!!!");
        }
        return noId;
    }

    //Frequency------------------------------------------------------------------------------
    public boolean checkHasFrequency(int noId, int tableFreqIndex){
        String tableName = this.DATABASE_TABLE_FREQUENCY[tableFreqIndex];
        String sql = "SELECT * FROM " + tableName + " WHERE _ID= " + noId;
        Cursor c = this.db.rawQuery(sql, null);
        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setDBFrequency_Create(int noId, int tableFreqIndex, Bundle bundleFreq) {
        ContentValues cvFreq = new ContentValues();
        switch (bundleFreq.getString(CommonFragment.CMD)) {
            case FragmentFreqDay.FREQ_DAY:
                cvFreq.put("no_xdays", bundleFreq.getInt(FragmentFreqDay.FREQ_DAY_X));
                break;
            case FragmentFreqTime.FREQ_TIME:
//                cvFreq.put("no_daily_xtimes", bundleFreq.getInt(FragmentFreqTime.FREQ_TIME_X));
                break;
            case FragmentFreqHour.FREQ_HOUR:
                cvFreq.put("no_sethour", bundleFreq.getInt(FragmentFreqHour.FREQ_HOUR_X));
                cvFreq.put("no_freq_starttime", bundleFreq.getString(FragmentFreqHour.FREQ_HOUR_FIRSTDATE));//todo check空值怎麼存
                cvFreq.put("no_freq_lasttime", bundleFreq.getString(FragmentFreqHour.FREQ_HOUR_PREVDATE));
                break;
            case FragmentFreqWeeks.FREQ_WEEKS:
                cvFreq.put("no_mon", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK1));
                cvFreq.put("no_tue", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK2));
                cvFreq.put("no_wed", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK3));
                cvFreq.put("no_thu", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK4));
                cvFreq.put("no_fri", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK5));
                cvFreq.put("no_sat", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK6));
                cvFreq.put("no_sun", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK7));
                break;
            case FragmentFreqActivePause.FREQ_ACTIVEPAUSE:
                cvFreq.put("no_freq_active", bundleFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X));
                cvFreq.put("no_freq_pause", bundleFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y));
                cvFreq.put("no_cycle_xdays", bundleFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z));
                break;
        }
        cvFreq.put("_ID", noId);
        this.db.insert(this.DATABASE_TABLE_FREQUENCY[tableFreqIndex], null, cvFreq);
    }

    public void setDBFrequency_Update(int noId, int tableFreqIndex, Bundle bundleFreq) {
        ContentValues cvFreq = new ContentValues();
        switch (bundleFreq.getString(CommonFragment.CMD)) {
            case FragmentFreqDay.FREQ_DAY:
                cvFreq.put("no_xdays", bundleFreq.getInt(FragmentFreqDay.FREQ_DAY_X));
                break;
            case FragmentFreqTime.FREQ_TIME:
//                cvFreq.put("no_daily_xtimes", bundleFreq.getInt(FragmentFreqTime.FREQ_TIME_X));
                break;
            case FragmentFreqHour.FREQ_HOUR:
                cvFreq.put("no_sethour", bundleFreq.getInt(FragmentFreqHour.FREQ_HOUR_X));
                cvFreq.put("no_freq_starttime", bundleFreq.getString(FragmentFreqHour.FREQ_HOUR_FIRSTDATE));
                cvFreq.put("no_freq_lasttime", bundleFreq.getString(FragmentFreqHour.FREQ_HOUR_PREVDATE));
                break;
            case FragmentFreqWeeks.FREQ_WEEKS:
                cvFreq.put("no_mon", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK1));
                cvFreq.put("no_tue", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK2));
                cvFreq.put("no_wed", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK3));
                cvFreq.put("no_thu", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK4));
                cvFreq.put("no_fri", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK5));
                cvFreq.put("no_sat", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK6));
                cvFreq.put("no_sun", bundleFreq.getInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK7));
                break;
            case FragmentFreqActivePause.FREQ_ACTIVEPAUSE:
                cvFreq.put("no_freq_active", bundleFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X));
                cvFreq.put("no_freq_pause", bundleFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y));
                cvFreq.put("no_cycle_xdays", bundleFreq.getInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z));
                break;
        }
        this.db.update(this.DATABASE_TABLE_FREQUENCY[tableFreqIndex], cvFreq, "_ID" + "=" + noId, null);
    }

    public void setDBFrequency_Delete(int noId) {
        for (int i = 0; i < this.DATABASE_TABLE_FREQUENCY.length; i++) {
            this.db.delete(this.DATABASE_TABLE_FREQUENCY[i], "_ID" + "=" + noId, null);
        }
    }

    public Bundle getDBFrequency(int noId, int tableFreqIndex) {
        Bundle bundle = new Bundle();
        String tableName = this.DATABASE_TABLE_FREQUENCY[tableFreqIndex];
        String sql = "SELECT * FROM " + tableName + " WHERE _ID= " + noId;
        Cursor c = this.db.rawQuery(sql, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            switch (tableName) {
                case "no_freq_day":
                    bundle.putInt(FragmentFreqDay.FREQ_DAY_X, c.getInt(1));
                    break;
                case "no_freq_time":
                    ArrayList<String> newAlarmTimeList = this.getFrequency_TimeList(noId);
                    for(int i=0; i<newAlarmTimeList.size(); i++){
                        bundle.putStringArrayList(FragmentFreqTime.FREQ_TIME_LIST,newAlarmTimeList);
                    }
//                    bundle.putInt(FragmentFreqTime.FREQ_TIME_X, c.getInt(1));//todo
                    break;
                case "no_freq_hour":
                    bundle.putInt(FragmentFreqHour.FREQ_HOUR_X, c.getInt(1));
                    bundle.putString(FragmentFreqHour.FREQ_HOUR_FIRSTDATE, c.getString(2));
                    bundle.putString(FragmentFreqHour.FREQ_HOUR_PREVDATE, c.getString(3));
                    break;
                case "no_freq_week":
                    bundle.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK1, c.getInt(1));
                    bundle.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK2, c.getInt(2));
                    bundle.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK3, c.getInt(3));
                    bundle.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK4, c.getInt(4));
                    bundle.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK5, c.getInt(5));
                    bundle.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK6, c.getInt(6));
                    bundle.putInt(FragmentFreqWeeks.FREQ_WEEKS_WEEK7, c.getInt(7));
                    break;
                case "no_freq_active_pause":
                    bundle.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_X, c.getInt(1));
                    bundle.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Y, c.getInt(2));
                    bundle.putInt(FragmentFreqActivePause.FREQ_ACTIVEPAUSE_Z, c.getInt(3));
                    break;
            }
            c.close();
        } else {
            Log.w("Warn==>", "NotificationDB.getDBFrequency()=> is no data!!!");
        }
        return bundle;
    }


    /**
     * 取得指定NoId，每日x次的所有時間點
     * @param noId
     * @return
     */
    public ArrayList<String> getFrequency_TimeList(int noId){
        ArrayList<String> newAlarmTimeList;
        ArrayList<Integer> alarmIdArrFromNoId;
        Bundle bundle;
        String time;
        Date data;
        Date endDate;
        try {
            endDate = this.ft.parse("1912-01-01 00:00"); //1911-1-1~1911-12-31
            newAlarmTimeList = new ArrayList<>();
            alarmIdArrFromNoId = this.getDBAlarmIdArrFromNoId(noId);
            for(int i=0; i<alarmIdArrFromNoId.size(); i++){
                bundle = this.getDBAlarm(alarmIdArrFromNoId.get(i));
                time = bundle.getString("TIME");
                data = this.ft.parse(time);
                if(data.before(endDate)){
                    newAlarmTimeList.add(time);
                }
            }
            return newAlarmTimeList;
        } catch (ParseException e) {
            e.printStackTrace();//todo Exception
        }
        return null;
    }

    //Duration-------------------------------------------------------------------------
    public boolean checkHasDuration(int noId, int tableDurIndex){
        String tableName = this.DATABASE_TABLE_DURATION[tableDurIndex];
        String sql = "SELECT * FROM " + tableName + " WHERE _ID= " + noId;
        Cursor c = this.db.rawQuery(sql, null);
        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setDBDuration_Create(int noId, int tableDurIndex, Bundle bundleDur) {
        ContentValues cvDur = new ContentValues();
        switch (bundleDur.getString(CommonFragment.CMD)) {
            case FragmentDurEnddate.DUR_ENDDATE:
                cvDur.put("no_dur_enddate", bundleDur.getString(FragmentDurEnddate.DUR_ENDDATE_ENDDATE));
                break;
            case FragmentDurCont.DUR_CONT:
                cvDur.put("no_dur_day", bundleDur.getInt(FragmentDurCont.DUR_CONT_X));
                break;
        }
        cvDur.put("_ID", noId);
        this.db.insert(this.DATABASE_TABLE_DURATION[tableDurIndex], null, cvDur);
    }

    public void setDBDuration_Update(int noId, int tableDurIndex, Bundle bundleDur) {
        ContentValues cvDur = new ContentValues();
        switch (bundleDur.getString(CommonFragment.CMD)) {
            case FragmentDurEnddate.DUR_ENDDATE:
                cvDur.put("no_dur_enddate", bundleDur.getString(FragmentDurEnddate.DUR_ENDDATE_ENDDATE));
                break;
            case FragmentDurCont.DUR_CONT:
                cvDur.put("no_dur_day", bundleDur.getInt(FragmentDurCont.DUR_CONT_X));
                break;
        }
        this.db.update(this.DATABASE_TABLE_DURATION[tableDurIndex], cvDur, "_ID" + "=" + noId, null);
    }

    public void setDBDuration_Delete(int noId) {
        for (int i = 0; i < this.DATABASE_TABLE_DURATION.length; i++) {
            this.db.delete(this.DATABASE_TABLE_DURATION[i], "_ID" + "=" + noId, null);
        }
    }

    public Bundle getDBDuration(int noId, int tableDurIndex) {
        Bundle bundle = new Bundle();
        String tableName = this.DATABASE_TABLE_DURATION[tableDurIndex];
        String sql = "SELECT * FROM " + tableName + " WHERE _ID= " + noId;
        Cursor c = this.db.rawQuery(sql, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            switch (tableName) {
                case "no_dur_enddate":
                    bundle.putString(FragmentDurEnddate.DUR_ENDDATE_ENDDATE, c.getString(1));
                    break;
                case "no_dur_cont":
                    bundle.putInt(FragmentDurCont.DUR_CONT_X, c.getInt(1));
                    break;
            }
            c.close();
        } else {
            Log.w("Warn==>", "NotificationDB.getDBDuration()=> is no data!!!");
        }
        return bundle;
    }

    //Alarm-------------------------------------------------------------------------
    public void setDBAlarm_Create(int noId, String dateTime/*2019-12-13 01:02*/) {//todo 新增，會加很多筆的資料
        ContentValues cvDur = new ContentValues();
        cvDur.put("_ID", noId);
        cvDur.put("set_time", dateTime);
        this.db.insert(this.DATABASE_TABLE_ALARM, null, cvDur);
    }

    public void setDBAlarm_DeleteFromNoId(int noId) {//清除某一鬧鐘的所有時間點
        this.db.delete(this.DATABASE_TABLE_ALARM, "_ID" + "=" + noId, null);
    }

//    public void setDBAlarm_DeleteFromAlarmId(int _alarmID) {//清除某一鬧鐘的某一時間點
////        this.db.delete(this.DATABASE_TABLE_ALARM, "_alarmID" + "=" + _alarmID, null);
////    }

    public ArrayList<Integer> getDBAlarmIdArrFromNoId(int noId){
        ArrayList<Integer> alarmIdArr=null;
        String sql = "SELECT * FROM " + this.DATABASE_TABLE_ALARM + " WHERE _ID= " + noId + " ORDER BY _alarmID DESC ";//最新的資料為第0筆，最舊的資料為最後一筆
        Cursor c = this.db.rawQuery(sql, null);
        if (c.getCount() > 0) {
            alarmIdArr = new ArrayList<Integer>();
            while(c.moveToNext()){
                alarmIdArr.add(c.getInt(0));
            }
            c.close();
        } else {
            Log.w("Warn==>", "NotificationDB.getDBAlarmIdArrFromNoId()=> is no data!!!");
        }
        return alarmIdArr;
    }

    public boolean checkAlarmIsActive(int alarmID){
        boolean isActive=false;
        String sql = "SELECT * FROM " + this.DATABASE_TABLE_ALARM + " WHERE _alarmID= " + alarmID;
        Cursor c = this.db.rawQuery(sql, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            int noId = c.getInt(2);
            String sql2 = "SELECT * FROM " + this.DATABASE_TABLE + " WHERE _ID= " + noId;
            Cursor c2 = this.db.rawQuery(sql2, null);
            if (c2.getCount() > 0) {
                c2.moveToFirst();
                int active = c2.getInt(5);
                isActive = (active==1)?true:false;
                c2.close();
            }else{
                Log.w("Warn==>", "NotificationDB.checkAlarmIsActive()=> select noId data row is no have!!!");
            }
            c.close();
        } else {
            Log.w("Warn==>", "NotificationDB.checkAlarmIsActive()=> select alarmID data row is no have!!!");
        }

        return isActive;
    }

//    public int getDBAlarm_Last(int noId){
//        int alarmID = -1;
//        String sql = "SELECT * FROM " + this.DATABASE_TABLE_ALARM + " WHERE _ID= " + noId + " ORDER BY _alarmID DESC LIMIT 0,1 ";//取得最後(最新)一筆資料的_ID
//        Cursor c = this.db.rawQuery(sql, null);
//        if (c.getCount() > 0) {
//            c.moveToFirst();
//            alarmID = c.getInt(0);
//            c.close();
//        } else {
//            Log.w("Warn==>", "NotificationDB.getDBAlarm_Last()=> is no data!!!");
//        }
//        return alarmID;
//    }

    public Bundle getDBAlarm(int alarmID){
        Bundle bundle=null;
        String sql = "SELECT * FROM " + this.DATABASE_TABLE_ALARM + " WHERE _alarmID= " + alarmID;
        Cursor c = db.rawQuery(sql, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            bundle = new Bundle();
            bundle.putString("TIME", c.getString(1));
            c.close();
        } else {
            Log.w("Warn==>", "NotificationDB.getDBAlarm()=> is no data!!!");
        }
        return bundle;
    }

    //Util----------------------------------------------------------------------------------------
    public void clearTotalNotificationData(){
        this.db.delete(this.DATABASE_TABLE, null, null);
        for (int i = 0; i < this.DATABASE_TABLE_FREQUENCY.length; i++) {
            this.db.delete(this.DATABASE_TABLE_FREQUENCY[i], null, null);
        }
        for (int i = 0; i < this.DATABASE_TABLE_DURATION.length; i++) {
            this.db.delete(this.DATABASE_TABLE_DURATION[i], null, null);
        }
        this.db.delete(this.DATABASE_TABLE_ALARM, null, null);
    }

}
