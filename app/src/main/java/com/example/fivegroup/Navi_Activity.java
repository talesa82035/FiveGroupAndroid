package com.example.fivegroup;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class Navi_Activity extends AppCompatActivity  implements LocationListener{

    private Button btnNavi, btnDial;
    private String num, addr;
    private LatLng destination_lngLat;
    private double lat,lng;
    private LocationManager lc;
    private Location currentLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        lc = (LocationManager) getSystemService(LOCATION_SERVICE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("導航或撥號");
        setSupportActionBar(toolbar);

        findViews();
        setListeners();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            num = bundle.getString("num");
            addr = bundle.getString("addr");
        }

    }

    private void findViews() {
        btnNavi = findViewById(R.id.btnNavi);
        btnDial = findViewById(R.id.btnDial);
    }

    private void setListeners() {
        btnNavi.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                String bestProvider = lc.getBestProvider(new Criteria(),true);
                try{
//                    System.out.println("--------btnNavi onClick------");
//                    System.out.println(bestProvider);
//                    System.out.println(lc);
                    currentLocation = lc.getLastKnownLocation(bestProvider);
//                    System.out.println(currentLocation.toString());
                    if (currentLocation != null){
                        lat = currentLocation.getLatitude();
                        lng = currentLocation.getLongitude();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Navi_Activity.this);
                        builder.setTitle("訊息");
                        builder.setMessage("取得目前 GPS  位置資料失敗!!");
//                        output.setText("取得目前 GPS  位置資料失敗!!  currentLocation  = null");
                    }
                } catch(SecurityException ex) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Navi_Activity.this);
                    builder.setTitle("訊息");
                    builder.setMessage("取得目前 GPS  位置資料失敗!!\n 請檢查存取權限，並開啟GPS");
//                    output.setText("取得目前 GPS  位置資料失敗!!  請檢查存取權限，並開啟GPS");
                }

                getAddress2LngLat_startingPlace_lngLat(addr);
//                if (!("".equals(et_startingPlace.getText().toString()) || "".equals(et_destination.getText().toString()))) {
//                    getAddress2LngLat_startingPlace_lngLat(et_startingPlace.getText().toString());
//                    getAddress2LngLat_startingPlace_lngLat(et_destination.getText().toString());
//                }
            }
        });

        btnDial.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:+" + num));
                startActivity(i);
            }
        });
    }

    private void getAddress2LngLat_startingPlace_lngLat(final String place) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+place+"&key=AIzaSyCeXT8HJRAADsUaLFa_CJKmPJsYzWpgnDs&language=zh-TW";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Success=====>",response.toString());
                try{
                    JSONObject data = ((JSONObject)response.getJSONArray("results").get(0)).getJSONObject("geometry").getJSONObject("location");
                    LatLng result = new LatLng(data.getDouble("lat"),data.getDouble("lng"));
//                    if(place.equals(et_startingPlace.getText().toString())){
//                        startingPlace_lngLat = result;
//                    }else if(place.equals(et_destination.getText().toString())){
                        destination_lngLat = result;
//                    }
                    startNavigation();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Fail=====>",error.toString());
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void startNavigation(){
        if(destination_lngLat==null)return;
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?"
                        + "saddr="+ lat + "," + lng
                        + "&daddr=" + destination_lngLat.latitude + "," + destination_lngLat.longitude
                        +"&avoid=highway"
                        +"&language=zh-TW")
        );
        intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            lat = location.getLatitude();
            lng = location.getLongitude();
            currentLocation = location;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Navi_Activity.this);
            builder.setTitle("訊息");
            builder.setMessage("取得目前 GPS  位置資料失敗!!\n 請檢查存取權限，並開啟GPS");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        int minReflashTime = 1000;
//        float minDistance = 1;
//        try {
//            String bestProvider = lc.getBestProvider(new Criteria(),true);
//            if (bestProvider != null){
//                lc.requestLocationUpdates(bestProvider,minReflashTime,minDistance,this);
//            } else {
//                output.setText("註冊 GPS  requestLocationUpdate 失敗!!  請檢查存取權限，並開啟GPS");
//            }
//
//        } catch (SecurityException ex) {
//            output.setText("註冊 GPS  requestLocationUpdate 失敗!!  請檢查存取權限，並開啟GPS");
//        }
//    }
}
