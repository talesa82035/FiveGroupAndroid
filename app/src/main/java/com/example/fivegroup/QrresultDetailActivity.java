package com.example.fivegroup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class QrresultDetailActivity extends AppCompatActivity {

    String str1;
    TextView qrcodeTextResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrresult_detail);

        qrcodeTextResult = findViewById(R.id.qrcodetextresult);
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null) {

           str1 = bundle.getString("title");


        }

        String url = "http://10.10.3.104/api/IngrediantsAPI?ing_name=" + str1;
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
        String strOut;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            strOut = o.getString("ing_restricted") + "\n";
//            str2 = o.getString("內容");


            qrcodeTextResult.setText(strOut);
        }

    }
}
