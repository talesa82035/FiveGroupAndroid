package com.example.fivegroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 搜尋醫院按鈕
        Button button01 = (Button)findViewById(R.id.button01);
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,FindDoctor.class);
                startActivity(intent);
            }
        });
        //公告news
        LayoutInflater newInflater = LayoutInflater.from(MainActivity.this);
        final View v = newInflater.inflate(R.layout.activity_news, null);
        //公告按鈕
        AlertDialog.Builder newsbuilder = new AlertDialog.Builder(MainActivity.this);
        newsbuilder.setTitle("公告")
                .setView(v)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) (v.findViewById(R.id.editText01));
                        Toast.makeText(getApplicationContext(), "你的id是" +
                                editText.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        final AlertDialog newsalert = newsbuilder.create();
        //開啟時顯示公告
        newsalert.show();
        //公告按鈕 呼叫公告
        Button button09 = (Button)findViewById(R.id.button09);
        button09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsalert.show();
            }
        });
    }
}
