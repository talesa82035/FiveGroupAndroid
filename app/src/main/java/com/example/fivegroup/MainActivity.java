package com.example.fivegroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    String url = "http://10.10.3.104/api/AnnouncementAPI?";
    String str1;

    ArrayList<String> list = new ArrayList<>();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("第五組");
        toolbar.setSubtitle("專題實作");
        setSupportActionBar(toolbar);

        lv = findViewById(R.id.listViewJsonData);

        getPermissionsCamera();
        getPermissionsGPS();

        //取得主頁面的元素，並設置點擊事件監聽器
        CardView finddoctor = findViewById(R.id.card01);
        finddoctor.setOnClickListener(new finddoctorListener());
        CardView whichclass = findViewById(R.id.card02);
        whichclass.setOnClickListener(new whichclassListener());
        CardView ingredients = findViewById(R.id.card03);
        ingredients.setOnClickListener(new ingredientsListener());
        CardView records = findViewById(R.id.card04);
        records.setOnClickListener(new recordsListener());
        CardView notification = findViewById(R.id.card05);
        notification.setOnClickListener(new notificationListener());
        CardView tools = findViewById(R.id.card06);
        tools.setOnClickListener(new toolsListener());
        CardView news = findViewById(R.id.card07);
        news.setOnClickListener(new newsListener());
        CardView feedback = findViewById(R.id.card08);
        feedback.setOnClickListener(new feedbackListener());
    }
    //鑄造　AlertDialog 提醒視窗
    protected AlertDialog.Builder announcement(){
        String message=str1;
//        message = "【功能上架一覽表】\n" +
//                "\n" +
//                "2019-12-24\n" +
//                "-實用工具：計算月經週期\n" +
//                "\n" +
//                "2019-12-23\n" +
//                "-實用工具：計算基礎代謝率\n" +
//                "-實用工具：計算飲水量\n" +
//                "-用藥提醒\n" +
//                "\n" +
//                "2019-12-15\n" +
//                "-個人紀錄：血壓\n" +
//                "-個人紀錄：血糖\n" +
//                "\n" +
//                "2019-12-13\n" +
//                "-意見回饋\n" +
//                "\n" +
//                "2019-12-06\n" +
//                "-該看哪科\n" +
//                "\n" +
//                "2019-11-26\n" +
//                "-個人紀錄：體重\n" +
//                "\n" +
//                "2019-11-19\n" +
//                "-實用工具：計算BMI值\n" +
//                "\n" +
//                "2019-11-17\n" +
//                "-最新消息";

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View v = inflater.inflate(R.layout.activity_announcements, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("系統公告")
                .setView(v)
                .setMessage(message)
                .setPositiveButton("確定",null);
        return builder;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.announcement:

                getData(url);
//                announcement().show();
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
            Intent intent = new Intent(MainActivity.this, Search_Activity.class);
            startActivity(intent);
        }
    }
    //跳轉 =>02該看哪科
    private class whichclassListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, Query_Activity.class);
            startActivity(intent);
        }
    }
    //跳轉 =>03食品成分
    private class ingredientsListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, Qrcode_Activity.class);
            startActivity(intent);
        }
    }
    //跳轉 =>04個人紀錄
    private class recordsListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, RecordsActivity.class);
            startActivity(intent);
        }
    }
    //跳轉 =>05用藥提醒
    private class notificationListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        }
    }
    //跳轉 =>06實用工具
    private class toolsListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, Tools_Activity.class);
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
            Uri uri=Uri.parse("http://10.10.3.104/Feedback/Create");
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        }
    }
    //取得相機權限
    public void getPermissionsCamera(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);
        }
    }

    //取得GPS權限
    public void getPermissionsGPS(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    private String getData(String urlString) {
        String result = "";
        //使用JsonArrayRequest類別要求JSON資料。
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("回傳結果", "結果=" + response.toString());
                try {
                    parseJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("回傳結果", "錯誤訊息：" + error.toString());
            }
        });

        Volley.newRequestQueue(this).add(request);
        return result;
    }

    //解析JSON元素
    private void parseJSON(JSONArray jsonArray) throws JSONException {

//        String str2;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            str1 = o.getString("a_content").replaceAll("\\\\n", "\n");
//            str2 = o.getString("內容");

//            list.add(str1);
//            context.add(str2);
        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
//
//        lv.setAdapter(adapter);

        announcement().show();
    }


}
