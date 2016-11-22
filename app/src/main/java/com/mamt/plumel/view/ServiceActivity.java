package com.mamt.plumel.view;


import android.content.Intent;

import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.mamt.plumel.GPSTracker;

import com.mamt.plumel.R;


public class ServiceActivity extends FragmentActivity implements OnMapReadyCallback {

    GPSTracker gps;
    private double latitude;
    private double longitude;
    SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        Button button = (Button) findViewById(R.id.btn_createservice);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Intent intent = new Intent(ServiceActivity.this, CreateServiceActivity.class);
                Bundle b = new Bundle();
                b.putString("latitud", String.valueOf(latitude));
                b.putString("longuitud", String.valueOf(longitude));
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                finish();

            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        // create class object
        gps = new GPSTracker(this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {

            gps.showSettingsAlert();
        }
        //showToast("si entro");
        LatLng sydney = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("plumel!"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }



    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
