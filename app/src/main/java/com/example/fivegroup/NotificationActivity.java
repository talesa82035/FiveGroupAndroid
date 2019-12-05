package com.example.fivegroup;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.HashMap;

public class NotificationActivity extends AppCompatActivity {
    //DB
    private static String DATABASE_TABLE = "notification";
    private DBhelper_Activity dbhelper;
    private SQLiteDatabase db;

    //View
    private Button addAlarm;
    private ListView listDataView;

    //Data
    ArrayList<HashMap> dbResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //加入自訂的ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("用藥提醒");
        setSupportActionBar(toolbar);

        addAlarm = findViewById(R.id.addAlarm);
        addAlarm.setOnClickListener(new addAlarmClickHandler());
        listDataView = findViewById(R.id.listDataView);
        listDataView.setOnItemClickListener(new detailClickHandler());
    }


    @Override
    public void onStart() {
        super.onStart();
        System.out.println("--NotificationActivity onStart--");
        Cursor cursor = this.getDBData();
        this.parseData(cursor);
        cursor.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("--NotificationActivity onResume--");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("--NotificationActivity onPause--");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("--NotificationActivity onStop--");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("--NotificationActivity onDestroy--");
    }


    private class addAlarmClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(NotificationActivity.this, AlarmActivity.class);
            startActivity(intent);
        }
    }

    private class detailClickHandler implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            HashMap dbResult = dbResultList.get(i);

            Intent intent = new Intent(NotificationActivity.this, AlarmActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("_ID", (int)dbResult.get("_ID"));
            bundle.putString("no_title", (String)dbResult.get("no_title"));
            bundle.putString("no_startdate", (String)dbResult.get("no_startdate"));
            bundle.putInt("no_frequency", (int)dbResult.get("no_frequency"));
            bundle.putInt("no_duration", (int)dbResult.get("no_duration"));
            bundle.putInt("no_active", (int)dbResult.get("no_active"));

            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private Cursor getDBData(){
        dbhelper = new DBhelper_Activity(this);
        db = dbhelper.getWritableDatabase();

        String sql = "SELECT * FROM " + DATABASE_TABLE;
        return db.rawQuery(sql,null);
    }

    private void parseData(Cursor cursor){
        dbResultList = new ArrayList<HashMap>();
        String[] notificationHomeStr = new String[cursor.getCount()];
        HashMap dataMap;
        while(cursor.moveToNext()){
            System.out.println(cursor.getPosition());
            int _ID = cursor.getInt(0);
            String no_title = cursor.getString(1);
            String no_startdate = cursor.getString(2);
            int no_frequency = cursor.getInt(3);
            int no_duration = cursor.getInt(4);
            int no_active = cursor.getInt(5);

            dataMap = new HashMap();
            dataMap.put("_ID",_ID);
            dataMap.put("no_title",no_title);
            dataMap.put("no_startdate",no_startdate);
            dataMap.put("no_frequency",no_frequency);
            dataMap.put("no_duration",no_duration);
            dataMap.put("no_active",no_active);
            dbResultList.add(dataMap);

            int cursorIndex = cursor.getPosition();
            notificationHomeStr[cursorIndex] = "標題:"+no_title+" 開始日期:"+no_startdate+" 是否啟用:"+no_active;
//            System.out.println(_ID);
//            System.out.println(no_title);
//            System.out.println(no_startdate);
//            System.out.println(no_frequency);
//            System.out.println(no_duration);
//            System.out.println(no_active);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notificationHomeStr);
        listDataView.setAdapter(adapter);
    }

}
