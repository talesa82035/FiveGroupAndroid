package com.example.fivegroup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;

public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback {
    EditText et_startingPlace;
    EditText et_destination;

    LatLng startingPlace_lngLat;
    LatLng destination_lngLat;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("看診搜尋");
        toolbar.setSubtitle("導航");
        setSupportActionBar(toolbar);

        et_startingPlace = (EditText) findViewById(R.id.et_startingPlace);
        et_destination = (EditText) findViewById(R.id.et_destination);
        Button btnStartNavigation = findViewById(R.id.btnStartNavigation);
        Button btnCallPhone = findViewById(R.id.btnCallPhone);

        btnStartNavigation.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!("".equals(et_startingPlace.getText().toString()) || "".equals(et_destination.getText().toString()))) {
                    getAddress2LngLat_startingPlace_lngLat(et_startingPlace.getText().toString());
                    getAddress2LngLat_startingPlace_lngLat(et_destination.getText().toString());
                }
            }
        });

        btnCallPhone.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:+073357885"));
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
                    if(place.equals(et_startingPlace.getText().toString())){
                        startingPlace_lngLat = result;
                    }else if(place.equals(et_destination.getText().toString())){
                        destination_lngLat = result;
                    }
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
        if(startingPlace_lngLat==null || destination_lngLat==null)return;
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?"
                        + "saddr="+ startingPlace_lngLat.latitude+ "," + startingPlace_lngLat.longitude
                        + "&daddr=" + destination_lngLat.latitude + "," + destination_lngLat.longitude
                        +"&avoid=highway"
                        +"&language=zh-TW")
        );
        intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(22.5949041/*緯度-90~90*/, 120.3066113/*經度-180~180*/);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Test~~~這裡是高雄夢時代"));
        mMap.getUiSettings().setZoomControlsEnabled(true);// 右下角的放大縮小功能
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));// 放大地圖到 16 倍大
    }
}
