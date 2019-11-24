package com.example.fivegroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;

public class WeightRecordsActivity extends AppCompatActivity {
    private static String DATABASE_TABLE = "weight";
    private SQLiteDatabase db;
    private DBhelper_Activity dbhelper;
    private CalendarView calendarView;
    private EditText weight_day,weight_night,weight_note;
    private Button weightrecord;
    private String date,weight_no;
    private int Year,Month,Day;
    private Float weight_d,weight_n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_weight);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("體重紀錄");
        setSupportActionBar(toolbar);

        dbhelper = new DBhelper_Activity(this);
        db = dbhelper.getWritableDatabase();

        calendarView = findViewById(R.id.WeightcalendarView);
        weight_day = findViewById(R.id.weightrecord_day);
        weight_night = findViewById(R.id.weightrecord_night);
        weight_note = findViewById(R.id.weightrecord_note);
        weightrecord = findViewById(R.id.weightrecord);
        weightrecord.setOnClickListener(new weightrecordListener());
        calendarView.setOnDateChangeListener(new CalendarChangeListener());
        Calendar c=Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH)+1;
        Day = c.get(Calendar.DAY_OF_MONTH);
        date = Year+"/"+Month+"/"+Day;
        Toast t = Toast.makeText(WeightRecordsActivity.this,"今天:"+ date,Toast.LENGTH_LONG);
        t.show();
    }
    @Override
    protected void onStop(){
        super.onStop();
        db.close();
    }
    private class weightrecordListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ContentValues cv = new ContentValues();
            if (weight_day.getText()!=null)
                weight_d = Float.valueOf(weight_day.getText().toString());
            if (weight_night.getText()!=null)
                weight_n = Float.valueOf(weight_night.getText().toString());
            if (weight_note.getText()!=null)
                weight_no = weight_note.getText().toString();
            cv.put("date",date);
            cv.put("day",weight_d);
            cv.put("night",weight_n);
            cv.put("note",weight_no);
            Cursor c = getCursor(date);
            int row_count = c.getCount();
            if(row_count ==0)
            {
                db.insert(DATABASE_TABLE,null,cv);
            }else
                {
                    db.update(DATABASE_TABLE,cv,date +"="+ date,null);
                }
            Toast t = Toast.makeText(WeightRecordsActivity.this,"今天:"+date
                    +"\n"+"早上體重"+ weight_d
                    +"\n"+"晚上體重"+ weight_n
                    ,Toast.LENGTH_LONG);
            t.show();

        }
    }
    private class CalendarChangeListener implements CalendarView.OnDateChangeListener{
        @Override
        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
            Year = year;Month=month+1;Day=dayOfMonth;
            date = Year+"/"+Month+"/"+Day;
            Cursor c = getCursor(date);
            Toast t = Toast.makeText(WeightRecordsActivity.this,"今天:"+ date +"\n 資料"+c.getCount(),Toast.LENGTH_LONG);
            t.show();
            int row_count = c.getCount();
            if(row_count >0 ){
                c.moveToFirst();    // 移到第 1 筆資料
                String weight_day_content = c.getString(2);
                String weight_night_content = c.getString(3);
                String weight_note_content = c.getString(4);
                weight_day.setText(weight_day_content);
                weight_night.setText(weight_night_content);
                weight_note.setText(weight_note_content);
                c.close();
            }
            else{
                weight_day.setText("0");
                weight_night.setText("0");
                weight_note.setText("");
                c.close();
            }
        }
    }
    private Cursor getCursor(String today){
        String sql = "SELECT * FROM " + DATABASE_TABLE + " WHERE  date  =  " + today;
        Cursor c = db.rawQuery(sql,null);
        startManagingCursor(c);
        return  c;
    }
}
