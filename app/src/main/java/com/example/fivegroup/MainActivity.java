package com.example.fivegroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("第五組");
        toolbar.setSubtitle("專題實作");
        setSupportActionBar(toolbar);



        //取得主頁面的元素，並設置點擊事件監聽器
        CardView news = findViewById(R.id.card07);
        news.setOnClickListener(new newsListener());
    }
    //鑄造Alertdialog 提醒視窗
    protected AlertDialog.Builder announcement(){
        LayoutInflater announcementInflater = LayoutInflater.from(MainActivity.this);
        final View v = announcementInflater.inflate(R.layout.activity_announcements, null);
        AlertDialog.Builder announcement = new AlertDialog.Builder(this);
        announcement.setTitle("系統公告")
                .setView(v)
                .setMessage("好的開始\n成功的一半")
                .setPositiveButton("確定",null);
        return announcement;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.announcement:
                announcement().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //跳轉 =>01看診查詢
    private class finddoctorListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, News_Activity.class);
            startActivity(intent);
        }
    }
    //跳轉 =>02該看哪科
    private class whichclassListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, News_Activity.class);
            startActivity(intent);
        }
    }
    //跳轉 =>03食品成分
    private class ingredientsListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, News_Activity.class);
            startActivity(intent);
        }
    }
    //跳轉 =>04個人紀錄
    private class recordsListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, News_Activity.class);
            startActivity(intent);
        }
    }
    //跳轉 =>05用藥提醒
    private class notificationListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, News_Activity.class);
            startActivity(intent);
        }
    }
    //跳轉 =>06實用工具
    private class toolsListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, News_Activity.class);
            startActivity(intent);
        }
    }
    //跳轉 =>07最新消息
    private class newsListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, News_Activity.class);
            startActivity(intent);
        }
    }
    //跳轉 =>08意見回饋
    private class feedbackListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, News_Activity.class);
            startActivity(intent);
        }
    }
}
