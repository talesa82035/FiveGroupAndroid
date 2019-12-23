package com.example.fivegroup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    private Spinner spin1, spin2, spin3;
    private Button btn1, btn2;
    private EditText et1, et2;
    private String location_url = "http://10.10.3.104/api/SchedulesAPI?";
    private String hospital_url = "http://10.10.3.104/api/HospitalsAPI?";
    private String dep_url = "http://10.10.3.104/api/SymptomDepartmentAPI?";
    private String url1, url2, url3, str1, str_city, str3, str_dist, str_dep, str_hos, str_doc, cityname, hos_url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("看診查詢");

        setSupportActionBar(toolbar);

        str1 = "0";
        str_city = "臺北市";
        str3 = "dep_id";
        str_dist = "";
        str_dep = "";
        cityname = "city=" + str1;

        url1 = location_url + cityname;
        url2 = location_url + "cityName=" + str_city;
        url3 = dep_url + str3;
        getCityData(url1);
        getCountyData(url2);
        getDepartData(url3);

        findViews();
        setListeners();
    }

    private void setListeners() {

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_city = spin1.getSelectedItem().toString();
                url2 = location_url + "cityName=" + str_city;
                getCountyData(url2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn1.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
//                str_dist = spin2.getSelectedItem().toString();
//                str_dep = spin3.getSelectedItem().toString();
//                str_hos = et1.getText().toString();
//                str_doc = et2.getText().toString();
                hos_url = hospital_url + "city_name=高雄市&district_name=苓雅區&dep_name=牙科" +
                        "&hos_name&doc_name&hos_eng_name&date";

                getResult(hos_url);
            }
        });

    }


    private void findViews() {
        spin1 = findViewById(R.id.spinner1);
        spin2 = findViewById(R.id.spinner2);
        spin3 = findViewById(R.id.spinner4);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        et1 = findViewById(R.id.editText2);
        et2 = findViewById(R.id.editText3);

    }

    private void getCityData(String urlString) {

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

        String county;
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            county = o.getString("district_name");

            list.add(county);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        spin2.setAdapter(adapter);
    }

    private void getDepartData(String urlString) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("回傳結果", "結果=" + response.toString());
                try {
                    parseJSON3(response);
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

    private void parseJSON3(JSONArray jsonArray) throws JSONException {

        String department;
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            department = o.getString("dep_name");

            list.add(department);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        spin3.setAdapter(adapter);
    }

    private void getResult(String urlString) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("回傳結果", "結果=" + response.toString());
                try {
                    parseJSON4(response);
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

    private void parseJSON4(JSONArray jsonArray) throws JSONException {

        String hospital;
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            hospital = o.getString("hos_name");

            list.add(hospital);

            Intent intent = new Intent(Search_Activity.this, Searchdetail_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("result",list);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }


}

