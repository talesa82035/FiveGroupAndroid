package com.example.fivegroup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBhelper_Activity extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String Database_name = "FiveGroup";
    private SQLiteDatabase db;
    //建構子
    public DBhelper_Activity(Context context) {
        super(context, Database_name, null, VERSION);
        db = this.getWritableDatabase();
    }
    //輔助類建立時運行該方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE_TABLE_NOTIFICATION =
                "create table notification("
                        + "_ID  INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "no_title TEXT,"
                        + "no_startdate TEXT,"
                        + "no_frequency INTEGER,"
                        + "no_duration INTEGER,"
                        + "no_active INTEGER"
                        + ")";
        String DATABASE_CREATE_TABLE_NO_ALARM =
                "create table no_alarm("
                        + "_alarmID  INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "set_time TEXT,"
                        + "_ID INT,"
                        + "FOREIGN KEY(_ID) REFERENCES notification(_ID)"
                        + ")";
        String DATABASE_CREATE_TABLE_NO_FREQ_DAY =
                "create table no_freq_day("
                        + "_ID INT,"
                        + "no_xdays INT,"
                        + "FOREIGN KEY(_ID) REFERENCES notification(_ID)"
                        + ")";
        String DATABASE_CREATE_TABLE_NO_FREQ_TIME =
                "create table no_freq_time("
                        + "_ID INT,"
                        + "no_daily_xtimes INT,"
                        + "FOREIGN KEY(_ID) REFERENCES notification(_ID)"
                        + ")";
        String DATABASE_CREATE_TABLE_NO_FREQ_HOUR =
                "create table no_freq_hour("
                        + "_ID INT,"
                        + "no_sethour INT,"
                        + "no_freq_starttime TEXT,"
                        + "no_freq_lasttime TEXT,"
                        + "FOREIGN KEY(_ID) REFERENCES notification(_ID)"
                        + ")";
        String DATABASE_CREATE_TABLE_NO_FREQ_WEEK =
                "create table no_freq_week("
                        + "_ID INT,"
                        + "no_mon INT,"
                        + "no_tue INT,"
                        + "no_wed INT,"
                        + "no_thu INT,"
                        + "no_fri INT,"
                        + "no_sat INT,"
                        + "no_sun INT,"
                        + "FOREIGN KEY(_ID) REFERENCES notification(_ID)"
                        + ")";
        String DATABASE_CREATE_TABLE_NO_FREQ_ACTIVE_PAUSE =
                "create table no_freq_active_pause("
                        + "_ID INT,"
                        + "no_freq_active INT,"
                        + "no_freq_pause INT,"
                        + "no_cycle_xdays INT,"
                        + "FOREIGN KEY(_ID) REFERENCES notification(_ID)"
                        + ")";
        String DATABASE_CREATE_TABLE_NO_DUR_CONT =
                "create table no_dur_cont("
                        + "_ID INT,"
                        + "no_dur_day INT,"
                        + "FOREIGN KEY(_ID) REFERENCES notification(_ID)"
                        + ")";
        String DATABASE_CREATE_TABLE_NO_DUR_ENDDATE =
                "create table no_dur_enddate("
                        + "_ID INT,"
                        + "no_dur_enddate TEXT,"
                        + "FOREIGN KEY(_ID) REFERENCES notification(_ID)"
                        + ")";
        //上為用藥提醒
        //體重紀錄
        String DATABASE_CREATE_TABLE_WEIGHT =
                "create table weight("
                        + "_ID  INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "date TEXT NOT NULL UNIQUE,"
                        + "day REAL,"
                        + "night REAL,"
                        + "note TEXT"
                        + ")";
        //血糖紀錄
        String DATABASE_CREATE_TABLE_BLOODSUGAR =
                "create table bloodsugar("
                        + "_ID  INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "date TEXT NOT NULL UNIQUE,"
                        + "day_before INT,"
                        + "day_after INT,"
                        + "noon_before INT,"
                        + "noon_after INT,"
                        + "night_before INT,"
                        + "night_after INT,"
                        + "bedtime_after INT,"
                        + "note TEXT"
                        + ")";
        //血壓紀錄
        String DATABASE_CREATE_TABLE_BLOODPRESSURE =
                "create table bloodpressure("
                        + "_ID  INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "date TEXT NOT NULL UNIQUE,"
                        + "day_hight INT,"
                        + "day_low INT,"
                        + "day_pulse INT,"
                        + "noon_hight INT,"
                        + "noon_low INT,"
                        + "noon_pulse INT,"
                        + "night_height INT,"
                        + "night_low INT,"
                        + "night_pulse INT,"
                        + "note TEXT"
                        + ")";

        db.execSQL(DATABASE_CREATE_TABLE_NOTIFICATION);
        db.execSQL(DATABASE_CREATE_TABLE_NO_ALARM);
        db.execSQL(DATABASE_CREATE_TABLE_NO_FREQ_DAY);
        db.execSQL(DATABASE_CREATE_TABLE_NO_FREQ_TIME);
        db.execSQL(DATABASE_CREATE_TABLE_NO_FREQ_HOUR);
        db.execSQL(DATABASE_CREATE_TABLE_NO_FREQ_WEEK);
        db.execSQL(DATABASE_CREATE_TABLE_NO_FREQ_ACTIVE_PAUSE);
        db.execSQL(DATABASE_CREATE_TABLE_NO_DUR_CONT);
        db.execSQL(DATABASE_CREATE_TABLE_NO_DUR_ENDDATE);
        db.execSQL(DATABASE_CREATE_TABLE_WEIGHT);
        db.execSQL(DATABASE_CREATE_TABLE_BLOODSUGAR);
        db.execSQL(DATABASE_CREATE_TABLE_BLOODPRESSURE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
        db.execSQL("DROP TABLE IF EXISTS notification"); //刪除舊有的資料表
        db.execSQL("DROP TABLE IF EXISTS no_alarm");
        db.execSQL("DROP TABLE IF EXISTS no_freq_day");
        db.execSQL("DROP TABLE IF EXISTS no_freq_time");
        db.execSQL("DROP TABLE IF EXISTS no_freq_hour");
        db.execSQL("DROP TABLE IF EXISTS no_freq_week");
        db.execSQL("DROP TABLE IF EXISTS no_freq_active_pause");
        db.execSQL("DROP TABLE IF EXISTS no_dur_cont");
        db.execSQL("DROP TABLE IF EXISTS no_dur_enddate");
        db.execSQL("DROP TABLE IF EXISTS weight");
        db.execSQL("DROP TABLE IF EXISTS bloodsugar");
        db.execSQL("DROP TABLE IF EXISTS bloodpressure");
        onCreate(db);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
    @Override
    public synchronized void close() {
        super.close();
    }
}
