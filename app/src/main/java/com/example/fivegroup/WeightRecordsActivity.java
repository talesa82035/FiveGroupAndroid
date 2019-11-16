package com.example.fivegroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

public class WeightRecordsActivity extends AppCompatActivity {
    private static String DATABASE_TABLE = "weight";
    private SQLiteDatabase db;
    private DBhelper_Activity dbhelper;
    private CalendarView date;
    private EditText day,night,note;
    private Button weightrecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_weight);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("體重紀錄");

        setSupportActionBar(toolbar);

        //目前有問題
        dbhelper = new DBhelper_Activity(this);
        db = dbhelper.getWritableDatabase();

        date = findViewById(R.id.WeightcalendarView);
        day = findViewById(R.id.weightrecord_day);
        night = findViewById(R.id.weightrecord_night);
        note = findViewById(R.id.weightrecord_note);
        weightrecord = findViewById(R.id.weightrecord);
        weightrecord.setOnClickListener(new weightrecordListener());
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
            cv.put("date",date.getDate());
            cv.put("day",day.getText().toString());
            cv.put("night",night.getText().toString());
            cv.put("note",note.getText().toString());
            db.insert(DATABASE_TABLE,null,cv);
        }
    }
}
