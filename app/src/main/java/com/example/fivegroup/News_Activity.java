package com.example.fivegroup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> context = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //加入自訂的ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("食藥新聞");

        setSupportActionBar(toolbar);

        lv = findViewById(R.id.listViewJsonData);
        lv.setOnItemClickListener(new detailListener());

        String url = "https://www.fda.gov.tw/DataAction";
        getData(url);
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
        String str1;
        String str2;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            str1 = "標題:" + o.getString("標題") + "\n\n"
                    + "發布日期:" + o.getString("發布日期");
            str2 = o.getString("內容");

            list.add(str1);
            context.add(str2);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adapter);
    }

    private class detailListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            final String title = list.get(i);
            final String detail = context.get(i).replace("&ensp;","").replace("<br />","");

            Intent intent = new Intent(News_Activity.this, newsDetail_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("detail", detail);

            intent.putExtras(bundle);

            startActivity(intent);
        }


    }
}