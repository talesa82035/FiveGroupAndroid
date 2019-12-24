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

    private Button btnShowMap, btnNavi, btnDial;
    private String num, addr;
    private LatLng destination_lngLat;
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
        btnShowMap = findViewById(R.id.btnShowMap);
        btnNavi = findViewById(R.id.btnNavi);
        btnDial = findViewById(R.id.btnDial);
    }

    private void setListeners() {
        btnShowMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                double lat,lng;
                String label = "目前位置";
                String geoString,queryString;

                lat = currentLocation.getLatitude();
                lng = currentLocation.getLongitude();
                geoString = "geo:" + lat +","+ lng;
                queryString = lat + "," + lng + "(" + label + ")";
                queryString = Uri.encode(queryString);
                geoString = geoString + "?q=" + queryString + "?z=16";

                Intent intent  = new Intent(Intent.ACTION_VIEW, Uri.parse(geoString));
                startActivity(intent);
            }
        });

        btnNavi.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDestinationAddress2LngLat(addr);
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

    private void getDestinationAddress2LngLat(String destinationPlace) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+destinationPlace+"&key=AIzaSyCeXT8HJRAADsUaLFa_CJKmPJsYzWpgnDs&language=zh-TW";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Success=====>",response.toString());
                JSONObject data;
                LatLng result;
                try{
                    data = ((JSONObject)response.getJSONArray("results").get(0)).getJSONObject("geometry").getJSONObject("location");
                    result = new LatLng(data.getDouble("lat"),data.getDouble("lng"));
                    destination_lngLat = result;
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

    /**
     *  開導導航
     */
    private void startNavigation(){
        if(destination_lngLat==null)return;
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?"
                        + "saddr="+ currentLocation.getLatitude() + "," + currentLocation.getLongitude()
                        + "&daddr=" + destination_lngLat.latitude + "," + destination_lngLat.longitude
                        +"&avoid=highway"
                        +"&language=zh-TW")
        );
        intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
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

    @Override
    protected void onResume() {
        super.onResume();

        int minReflashTime = 1000;
        float minDistance = 1;
        try {
            String bestProvider = this.lc.getBestProvider(new Criteria(),true);
            if (bestProvider != null) {
                this.lc.requestLocationUpdates(bestProvider, minReflashTime, minDistance, this);
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }
}
