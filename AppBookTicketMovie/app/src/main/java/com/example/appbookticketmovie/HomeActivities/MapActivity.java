package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbookticketmovie.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener{

    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient flpClient;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private ListView listView;
    private LatLng currLocation = null;

    private LatLng cinemaLagLng = null;

    private String cinemaName;

    private String cinemaAddress;

    TextView address;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("Lat",0);
        double lng = intent.getDoubleExtra("Lng",0);

        cinemaName = intent.getStringExtra("cinemaName");
        cinemaAddress = intent.getStringExtra("cinemaAddress");
        cinemaLagLng = new LatLng(lat, lng);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);

        btn = findViewById(R.id.button6);
        address = findViewById(R.id.textView19);
        address.setText(cinemaAddress);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        googleMap.setTrafficEnabled(true);

        //Nút MyLocation để di chuyển camera đến vị trí hiện tại
        googleMap.setOnMyLocationButtonClickListener(MapActivity.this);

        if(cinemaLagLng != null){
            googleMap.addMarker(new MarkerOptions().position(cinemaLagLng).title(cinemaName));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(cinemaLagLng));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(cinemaLagLng));
            }
        });

        checkGPS();
    }

    public void getCurrentLocation(){
        //Lấy vị trí hiện tại của thiết bị
        flpClient = LocationServices.getFusedLocationProviderClient(this);
        flpClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng curr = new LatLng(location.getLatitude(), location.getLongitude());
                currLocation = curr;

                //Di chuyển camera
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Loading to your location", Toast.LENGTH_SHORT).show();
        getCurrentLocation();
        return false;
    }

    public void checkGPS(){
        LocationManager locationManager;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            googleMap.setMyLocationEnabled(true);
        }
        else{
            googleMap.setMyLocationEnabled(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
}