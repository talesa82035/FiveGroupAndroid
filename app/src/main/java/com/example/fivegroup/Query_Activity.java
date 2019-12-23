package com.example.fivegroup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class Query_Activity extends AppCompatActivity {

    private Spinner spin1, spin2;
    private Button btn1;
    private ArrayAdapter adpr1, adpr2;
    private String[] part = new String[]{"頭", "眼", "耳", "鼻", "口", "咽喉", "頸部", "心胸肺", "乳房", "背、腰", "腹部", "泌尿及生殖", "四肢"};
    private String[] symptom1 = new String[]{"頭痛、頭暈", "眩暈(天旋地轉)"};
    private String[][] symptom2 = new String[][]{{"頭痛、頭暈", "眩暈(天旋地轉)"}, {"眼睛疲勞、紅、癢、疼痛", "眼睛乾", "凸眼", "飛蚊症"}, {"耳朵痛、耳朵塞住", "耳鳴"},
            {"流鼻血", "流鼻水、鼻塞", "過敏性鼻炎", "打鼾"}, {"口腔潰瘍", "口臭", "口吃", "吞嚥困難", "咳嗽"}, {"喉嚨痛、扁桃腺發炎", "咳血", "喉嚨異物感"},
            {"頸部腫大、甲狀腺腫大、淋巴腺腫大"}, {"氣喘", "胸痛", "心悸", "心窩灼熱感"}, {"乳房脹痛"}, {"腰酸背痛", "肩背痠痛"},
            {"消化不良、胃酸過多", "嘔吐、吐血", "肝硬化", "肝功能異常", "腹痛", "腹脹、腹瀉", "便秘、便血"},
            {"血尿、頻尿、解尿困難", "尿失禁", "小便混濁或起泡", "性病", "陰道分泌物增加"},
            {"手腳麻痺", "關節痠痛", "肌肉壓痛", "肌力減退或喪失", "肌肉抽蓄痙攣", "癲癇"}};

    private Context context;
    private String str1, str2;

    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("該看哪科");

        setSupportActionBar(toolbar);

        context = getApplicationContext();

        findViews();
        setAdaptors();
        setListeners();
    }

    private void findViews() {
        spin1 = findViewById(R.id.part);
        spin2 = findViewById(R.id.sym);
        btn1 = findViewById(R.id.btnquery);

    }

    private void setAdaptors() {
        adpr1 = new ArrayAdapter(context, R.layout.myspinner, part);
        spin1.setAdapter(adpr1);
        spin1.setOnItemSelectedListener(selectListener);
        adpr2 = new ArrayAdapter(context, R.layout.myspinner, symptom1);
        spin2.setAdapter(adpr2);
    }

    private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            int pos = spin1.getSelectedItemPosition();
            adpr2 = new ArrayAdapter(context, R.layout.myspinner, symptom2[pos]);
            spin2.setAdapter(adpr2);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private void setListeners() {
        btn1.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                str1 = spin1.getSelectedItem().toString();
                str2 = spin2.getSelectedItem().toString();
                String url = "http://10.10.3.104/api/SymptomDepartmentAPI" + "?part_name=" + str1 + "&sym_name=" + str2;

                getData(url);

            }
        });
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

    private void parseJSON(JSONArray jsonArray) throws JSONException {
        String str3;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = jsonArray.getJSONObject(i);
            str3 = o.getString("dep_name");

            list.add(str3);
            Intent intent = new Intent(Query_Activity.this, Querydetail_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("result",list);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
