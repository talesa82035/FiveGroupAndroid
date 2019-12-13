package com.example.fivegroup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
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

public class Search_Activity extends AppCompatActivity {

    private Spinner spin1, spin2;
    private String url = "http://10.10.3.104/api/SchedulesAPI?";
    private String str1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("看診查詢");

        setSupportActionBar(toolbar);

        findViews();
        setListeners();
    }

    private void setListeners() {

        spin2.setOnClickListener(new Spinner.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        spin1.setOnClickListener(new Spinner.OnClickListener() {

            @Override
            public void onClick(View view) {
                str1 = "city=0";
                url = url + str1;
                getCityData(url);
            }
        });

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str1 = "city=" + spin1.getSelectedItem().toString();
                url = url + str1;
                getCountyData(url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                str1 = null;
            }
        });
    }

    private void findViews() {
        spin1 = findViewById(R.id.spinner1);
        spin2 = findViewById(R.id.spinner2);

    }

    private void getCityData(String urlString) {
//        String result = "";
        //使用JsonArrayRequest類別要求JSON資料。
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("回傳結果", "結果=" + response.toString());
                try {
                    parseJSON1(response);
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
//        return result;
    }

    private void parseJSON1(JSONArray jsonArray) throws JSONException {

        String city;
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            city = o.getString("city_Name");

            list.add(city);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        spin1.setAdapter(adapter);
    }

    private void getCountyData(String urlString) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("回傳結果", "結果=" + response.toString());
                try {
                    parseJSON2(response);
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
    }

    private void parseJSON2(JSONArray jsonArray) throws JSONException {

        String city;
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            city = o.getString("city_Name");

            list.add(city);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        spin2.setAdapter(adapter);
    }
}

