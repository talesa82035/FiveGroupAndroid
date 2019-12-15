package com.example.fivegroup;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class BloodsugarRecordsActivity extends AppCompatActivity {
    private static String DATABASE_TABLE = "bloodsugar";
    private SQLiteDatabase db;
    private DBhelper_Activity dbhelper;
    private CalendarView calendarView;
    private EditText Bloodsugarrecord_day_before, Bloodsugarrecord_day_after, Bloodsugarrecord_noon_before,Bloodsugarrecord_noon_after,Bloodsugarrecord_night_before,Bloodsugarrecord_night_after,Bloodsugarrecord_bedtime,Bloodsugarrecord_note;
    private Button bloodsugarrecord;
    private String date, bloodsuger_no;
    private int Year, Month, Day;
    private Float bs_d_b, bs_d_a,bs_n_b,bs_n_a,bs_night_b,bs_night_a,bs_bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_weight);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("血糖紀錄");
        setSupportActionBar(toolbar);

        dbhelper = new DBhelper_Activity(this);
        db = dbhelper.getWritableDatabase();

        calendarView = findViewById(R.id.BloodsugarcalendarView);
        Bloodsugarrecord_day_before = findViewById(R.id.Bloodsugarrecord_day_before);
        Bloodsugarrecord_day_after = findViewById(R.id.Bloodsugarrecord_day_after);
        Bloodsugarrecord_noon_before = findViewById(R.id.Bloodsugarrecord_noon_before);
        Bloodsugarrecord_noon_after = findViewById(R.id.Bloodsugarrecord_noon_after);
        Bloodsugarrecord_night_before = findViewById(R.id.Bloodsugarrecord_night_before);
        Bloodsugarrecord_night_after = findViewById(R.id.Bloodsugarrecord_night_after);
        Bloodsugarrecord_bedtime = findViewById(R.id.Bloodsugarrecord_bedtime);
        Bloodsugarrecord_note = findViewById(R.id.Bloodsugarrecord_note);

        bloodsugarrecord = findViewById(R.id.Bloodsugarrecord);
        bloodsugarrecord.setOnClickListener(new bloodsugarrecordListener());
        calendarView.setOnDateChangeListener(new CalendarChangeListener());
        Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH) + 1;
        Day = c.get(Calendar.DAY_OF_MONTH);
        date = Year + "/" + Month + "/" + Day;
        Toast t = Toast.makeText(BloodsugarRecordsActivity.this, "今天:" + date, Toast.LENGTH_LONG);
        t.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    private class bloodsugarrecordListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ContentValues cv = new ContentValues();
            if (Bloodsugarrecord_day_before.getText() != null)
                bs_d_b = Float.valueOf(Bloodsugarrecord_day_before.getText().toString());
            if (Bloodsugarrecord_day_after.getText() != null)
                bs_d_a = Float.valueOf(Bloodsugarrecord_day_after.getText().toString());
            if (Bloodsugarrecord_noon_before.getText() != null)
                bs_n_b = Float.valueOf(Bloodsugarrecord_noon_before.getText().toString());
            if (Bloodsugarrecord_noon_after.getText() != null)
                bs_n_a = Float.valueOf(Bloodsugarrecord_noon_after.getText().toString());
            if (Bloodsugarrecord_night_before.getText() != null)
                bs_night_b = Float.valueOf(Bloodsugarrecord_night_before.getText().toString());
            if (Bloodsugarrecord_night_after.getText() != null)
                bs_night_a = Float.valueOf(Bloodsugarrecord_night_after.getText().toString());
            if (Bloodsugarrecord_bedtime.getText() != null)
                bs_bd = Float.valueOf(Bloodsugarrecord_bedtime.getText().toString());

            if (Bloodsugarrecord_note.getText() != null)
                bloodsuger_no = Bloodsugarrecord_note.getText().toString();
            cv.put("date", date);
            cv.put("day_before", bs_d_b);
            cv.put("day_after", bs_d_a);
            cv.put("noon_before", bs_n_b);
            cv.put("noon_after", bs_n_a);
            cv.put("night_before", bs_night_b);
            cv.put("night_after", bs_night_a);
            cv.put("bedtime_after", bs_bd);
            cv.put("note", bloodsuger_no);

            Cursor c = getCursor(date);
            int row_count = c.getCount();
            if (row_count == 0) {
                db.insert(DATABASE_TABLE, null, cv);
            } else {
                db.update(DATABASE_TABLE, cv, "date" + "='" + date + "'", null);
            }
            Toast t = Toast.makeText(BloodsugarRecordsActivity.this, "今天:" + date
                            + "\n" + "早上飯前血糖" + bs_d_b
                            + "\n" + "早上飯後血糖" + bs_d_a
                            + "\n" + "中午飯前血糖" + bs_n_b
                            + "\n" + "中午飯後血糖" + bs_n_a
                            + "\n" + "晚上飯前血糖" + bs_night_b
                            + "\n" + "晚上飯後血糖" + bs_night_a
                            + "\n" + "睡前血糖" + bs_bd
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
            Cursor c = getCursor(date);
            Toast t = Toast.makeText(BloodsugarRecordsActivity.this, "今天:" + date + "\n 資料" + c.getCount(), Toast.LENGTH_LONG);
            t.show();
            int row_count = c.getCount();
            if (row_count > 0) {
                c.moveToFirst();    // 移到第 1 筆資料
                Bloodsugarrecord_day_before.setText(c.getString(2));
                Bloodsugarrecord_day_after.setText(c.getString(3));
                Bloodsugarrecord_noon_before.setText(c.getString(4));
                Bloodsugarrecord_noon_after.setText(c.getString(5));
                Bloodsugarrecord_night_before.setText(c.getString(6));
                Bloodsugarrecord_night_after.setText(c.getString(7));
                Bloodsugarrecord_bedtime.setText(c.getString(8));
                Bloodsugarrecord_note.setText(c.getString(9));
                c.close();
            } else {
                Bloodsugarrecord_day_before.setText("0");
                Bloodsugarrecord_day_after.setText("0");
                Bloodsugarrecord_noon_before.setText("0");
                Bloodsugarrecord_noon_after.setText("0");
                Bloodsugarrecord_night_before.setText("0");
                Bloodsugarrecord_night_after.setText("0");
                Bloodsugarrecord_bedtime.setText("0");
                Bloodsugarrecord_note.setText("");
                c.close();
            }
        }
    }

    private Cursor getCursor(String today) {
        String sql = "SELECT * FROM " + DATABASE_TABLE + " WHERE  date  =  '" + today + "'";
        Cursor c = db.rawQuery(sql, null);
//        startManagingCursor(c);
        return c;
    }
}
