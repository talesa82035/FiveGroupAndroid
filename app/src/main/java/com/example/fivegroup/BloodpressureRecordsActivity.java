package com.example.fivegroup;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class BloodpressureRecordsActivity extends AppCompatActivity {
    private static String DATABASE_TABLE = "bloodpressure";
    private SQLiteDatabase db;
    private DBhelper_Activity dbhelper;
    private CalendarView calendarView;
    private EditText Bloodpressurerecord_day_hight, Bloodpressurerecord_day_low, Bloodpressurerecord_day_pulse,Bloodpressurerecord_noon_hight,Bloodpressurerecord_noon_low,Bloodpressurerecord_noon_pulse,Bloodpressurerecord_night_low,Bloodpressurerecord_night_hight,Bloodpressurerecord_night_pulse,Bloodpressurerecord_note;
    private Button Bloodpressurerecord;
    private String date, bloodpressure_no;
    private int Year, Month, Day,bp_d_h,bp_d_l,bp_d_p,bp_n_h,bp_n_l,bp_n_p,bp_night_h,bp_night_l,bp_night_p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_bloodpressure);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("血壓紀錄");
        setSupportActionBar(toolbar);

        dbhelper = new DBhelper_Activity(this);
        db = dbhelper.getWritableDatabase();

        findElement();

        Toast t = Toast.makeText(BloodpressureRecordsActivity.this, "今天:" + getToday(), Toast.LENGTH_LONG);
        t.show();

        changeValue(getToday());
    }
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    private class BloodpressurerecordListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ContentValues cv = new ContentValues();
            if (Bloodpressurerecord_day_hight.getText().toString().matches(""))
            {
                bp_d_h = 0;
            }
            else {
                bp_d_h = Integer.valueOf(Bloodpressurerecord_day_hight.getText().toString());
            }

            if (Bloodpressurerecord_day_low.getText().toString().matches(""))
            {
                bp_d_l = 0;
            }
            else {
                bp_d_l = Integer.valueOf(Bloodpressurerecord_day_low.getText().toString());
            }
            if (Bloodpressurerecord_day_pulse.getText().toString().matches(""))
            {
                bp_d_p = 0;
            }
            else {
                bp_d_p = Integer.valueOf(Bloodpressurerecord_day_pulse.getText().toString());
            }

            if (Bloodpressurerecord_noon_hight.getText().toString().matches(""))
            {
                bp_n_h = 0;
            }
            else {
                bp_n_h = Integer.valueOf(Bloodpressurerecord_noon_hight.getText().toString());
            }

            if (Bloodpressurerecord_noon_low.getText().toString().matches(""))
            {
                bp_n_l = 0;
            }
            else {
                bp_n_l = Integer.valueOf(Bloodpressurerecord_noon_low.getText().toString());
            }

            if (Bloodpressurerecord_noon_pulse.getText().toString().matches(""))
            {
                bp_n_p = 0;
            }
            else {
                bp_n_p = Integer.valueOf(Bloodpressurerecord_noon_pulse.getText().toString());
            }

            if (Bloodpressurerecord_night_hight.getText().toString().matches(""))
            {
                bp_night_h = 0;
            }
            else {
                bp_night_h = Integer.valueOf(Bloodpressurerecord_night_hight.getText().toString());
            }

            if (Bloodpressurerecord_night_low.getText().toString().matches(""))
            {
                bp_night_l = 0;
            }
            else {
                bp_night_l = Integer.valueOf(Bloodpressurerecord_night_low.getText().toString());
            }

            if (Bloodpressurerecord_night_pulse.getText().toString().matches(""))
            {
                bp_night_p = 0;
            }
            else {
                bp_night_p = Integer.valueOf(Bloodpressurerecord_night_pulse.getText().toString());
            }

            cv.put("date", date);
            cv.put("day_hight", bp_d_h);
            cv.put("day_low", bp_d_l);
            cv.put("day_pulse", bp_d_p);
            cv.put("noon_hight", bp_n_h);
            cv.put("noon_low", bp_n_l);
            cv.put("noon_pulse", bp_n_p);
            cv.put("night_height", bp_night_h);
            cv.put("night_low", bp_night_l);
            cv.put("night_pulse", bp_night_p);
            cv.put("note", bloodpressure_no);

            Cursor c = getCursor(date);
            int row_count = c.getCount();
            if (row_count == 0) {
                db.insert(DATABASE_TABLE, null, cv);
            } else {
                db.update(DATABASE_TABLE, cv, "date" + "='" + date + "'", null);
            }
            Toast t = Toast.makeText(BloodpressureRecordsActivity.this, "今天:" + date
                            + "\n" + "早上最高壓" + bp_d_h
                            + "\n" + "早上最低壓" + bp_d_l
                            + "\n" + "早上脈搏" + bp_d_p
                            + "\n" + "中午最高壓" + bp_n_h
                            + "\n" + "中午最低壓" + bp_n_l
                            + "\n" + "中午脈搏" + bp_n_p
                            + "\n" + "晚上最高壓" + bp_night_h
                            + "\n" + "晚上最低壓" + bp_night_l
                            + "\n" + "晚上脈搏" + bp_night_p
                    , Toast.LENGTH_LONG);
            t.show();
        }
    }

    private class CalendarChangeListener implements CalendarView.OnDateChangeListener {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
            Year = year;
            Month = month + 1;
            Day = dayOfMonth;
            date = Year + "/" + Month + "/" + Day;
            changeValue(date);
        }
    }

    private Cursor getCursor(String today) {
        String sql = "SELECT * FROM " + DATABASE_TABLE + " WHERE  date  =  '" + today + "'";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    private void findElement()
    {
        Bloodpressurerecord_day_hight = findViewById(R.id.Bloodpressurerecord_day_hight);
        Bloodpressurerecord_day_low = findViewById(R.id.Bloodpressurerecord_day_low);
        Bloodpressurerecord_day_pulse = findViewById(R.id.Bloodpressurerecord_day_pulse);
        Bloodpressurerecord_noon_hight = findViewById(R.id.Bloodpressurerecord_noon_hight);
        Bloodpressurerecord_noon_low = findViewById(R.id.Bloodpressurerecord_noon_low);
        Bloodpressurerecord_noon_pulse = findViewById(R.id.Bloodpressurerecord_noon_pulse);
        Bloodpressurerecord_night_low = findViewById(R.id.Bloodpressurerecord_night_low);
        Bloodpressurerecord_night_hight = findViewById(R.id.Bloodpressurerecord_night_hight);
        Bloodpressurerecord_night_pulse = findViewById(R.id.Bloodpressurerecord_night_pulse);
        Bloodpressurerecord_note = findViewById(R.id.Bloodpressurerecord_note);
        Bloodpressurerecord = findViewById(R.id.Bloodpressurerecord);
        Bloodpressurerecord.setOnClickListener(new BloodpressurerecordListener());
        calendarView = findViewById(R.id.BloodpressurecalendarView);
        calendarView.setOnDateChangeListener(new CalendarChangeListener());

        Bloodpressurerecord_day_hight.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        Bloodpressurerecord_day_low.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        Bloodpressurerecord_day_pulse.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        Bloodpressurerecord_noon_hight.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        Bloodpressurerecord_noon_low.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        Bloodpressurerecord_noon_pulse.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        Bloodpressurerecord_night_low.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        Bloodpressurerecord_night_hight.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        Bloodpressurerecord_night_pulse.setInputType(EditorInfo.TYPE_CLASS_PHONE);
    }
    private String getToday()
    {
        Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH) + 1;
        Day = c.get(Calendar.DAY_OF_MONTH);
        date = Year + "/" + Month + "/" + Day;
        return date;
    }
    public void changeValue(String date)
    {
        Cursor c = getCursor(date);
        Toast t = Toast.makeText(BloodpressureRecordsActivity.this, "今天:" + date + "\n 資料" + c.getCount(), Toast.LENGTH_LONG);
        t.show();
        int row_count = c.getCount();
        if (row_count > 0) {
            c.moveToFirst();    // 移到第 1 筆資料
            Bloodpressurerecord_day_hight.setText(c.getString(2));
            Bloodpressurerecord_day_low.setText(c.getString(3));
            Bloodpressurerecord_day_pulse.setText(c.getString(4));
            Bloodpressurerecord_noon_hight.setText(c.getString(5));
            Bloodpressurerecord_noon_low.setText(c.getString(6));
            Bloodpressurerecord_noon_pulse.setText(c.getString(7));
            Bloodpressurerecord_night_hight.setText(c.getString(8));
            Bloodpressurerecord_night_low.setText(c.getString(9));
            Bloodpressurerecord_night_pulse.setText(c.getString(10));
            if(c.getString(9)!=null)
            {
                Bloodpressurerecord_note.setText(c.getString(11));
            }
            else
            {
                Bloodpressurerecord_note.setText("");
            }
            c.close();
        } else {
            Bloodpressurerecord_day_hight.setText("0");
            Bloodpressurerecord_day_low.setText("0");
            Bloodpressurerecord_day_pulse.setText("0");
            Bloodpressurerecord_noon_hight.setText("0");
            Bloodpressurerecord_noon_low.setText("0");
            Bloodpressurerecord_noon_pulse.setText("0");
            Bloodpressurerecord_night_hight.setText("0");
            Bloodpressurerecord_night_low.setText("0");
            Bloodpressurerecord_night_pulse.setText("0");
            Bloodpressurerecord_note.setText("");
            c.close();
        }
    }
}
