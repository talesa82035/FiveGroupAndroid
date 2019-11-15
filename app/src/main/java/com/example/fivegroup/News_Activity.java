package com.example.fivegroup;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class News_Activity extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //加入自訂的ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("食藥署新聞");

        setSupportActionBar(toolbar);

        lv = findViewById(R.id.listViewJsonData);

        String url = "https://www.fda.gov.tw/DataAction";
        getData(url);
    }

    private String getData(String urlString) {
        String result = "";
        //使用JsonArrayRequest類別要求JSON資料。
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("回傳結果", "結果=" + response.toString());
                try {
                    parseJSON(response);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("回傳結果","錯誤訊息："+ error.toString());
            }
        });

        Volley.newRequestQueue(this).add(request);
        return result;
    }

    //解析JSON元素
    private void parseJSON(JSONArray jsonArray)throws JSONException{

        ArrayList<String> list = new ArrayList();

        for(int i = 0; i<jsonArray.length(); i++){
            JSONObject o=jsonArray.getJSONObject(i);
            String str="標題:"+o.getString("標題")+"\n\n"
                    +"發布日期:"+o.getString("發布日期");
            list.add(str);
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);

        lv.setAdapter(adapter);
    }

}