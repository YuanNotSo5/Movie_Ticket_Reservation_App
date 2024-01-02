package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appbookticketmovie.Adapter.CinemaListAdapter;
import com.example.appbookticketmovie.Adapter.DayListAdapter;
import com.example.appbookticketmovie.HomeActivities.BookMapSeat;
import com.example.appbookticketmovie.Models.Cinema;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.CinemaService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.android.SphericalUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class ChooseCinemaActivity extends AppCompatActivity {
    RecyclerView recyclerViewCinema, recyclerViewSchedule, recyclerViewDay;
    DayListAdapter dayListAdapter;
    CinemaListAdapter cinemaListAdapter;
    private Map<Double, Cinema> cinemaList = new TreeMap();
    private ArrayList<Date> dayList = new ArrayList<>();
    private CinemaService cinemaService;
    private long idFilm;
    private Double extra_price = 0.0;
    private boolean backToDetailsRequested = false;

    private LatLng currLocation = null;

    private FusedLocationProviderClient flpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cinema);

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                Toast.makeText(this, "PLEASE UPDATE YOUR DEVICE SDK", Toast.LENGTH_SHORT).show();
                finish();
                System.exit(0);
            }
        }

        getSupportActionBar().hide();
        idFilm = getIntent().getLongExtra("idFilm",0);

        Intent seat = new Intent(this, BookMapSeat.class);
        System.out.println("idFilm:" + idFilm);
        seat.putExtra("idFilm", idFilm);

        cinemaService = new CinemaService();

        recyclerViewCinema = findViewById(R.id.recyclerViewCinema);
        recyclerViewCinema.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cinemaListAdapter = new CinemaListAdapter(this, cinemaList, idFilm, seat, extra_price);
        recyclerViewCinema.setAdapter(cinemaListAdapter);

        recyclerViewDay = findViewById(R.id.recyclerViewDay);
        recyclerViewDay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        dayListAdapter = new DayListAdapter(this, dayList, cinemaListAdapter, seat);
        recyclerViewDay.setAdapter(dayListAdapter);

        getDays();
        checkGPS();
    }

    public void getCinema(){
        cinemaService.getAllCinema(new CinemaService.getCinemas() {
            @Override
            public void getCinemas(ArrayList<Cinema> cinemas) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Cinema cinema : cinemas) {
                            cinemaList.put(Double.valueOf(String.valueOf(cinemaList.size())), cinema);
                        }
                        cinemaListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Cinemas Error:", e.getMessage());
            }
        });
    }
    public void getDays(){
        Calendar calendar = Calendar.getInstance();
        Date day = calendar.getTime();
        dayList.add(day);


        for(int i = 1; i < 7; i++){
            calendar.add(Calendar.DATE, 1);
            dayList.add(calendar.getTime());
        }
        dayListAdapter.notifyDataSetChanged();
    }

    public void checkGPS(){
        LocationManager locationManager;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Location l = new Location("");

            flpClient = LocationServices.getFusedLocationProviderClient(this);
            flpClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        System.out.println("IT WORKING");
                        LatLng curr = new LatLng(location.getLatitude(), location.getLongitude());
                        currLocation = curr;

                        if(currLocation != null){
                            System.out.println("YES IT NOT NULL");
                            l.setLatitude(currLocation.latitude);
                            l.setLongitude(currLocation.longitude);

                            cinemaService.getAllCinema(new CinemaService.getCinemas() {
                                @Override
                                public void getCinemas(ArrayList<Cinema> cinemas) {
                                    if(!cinemas.isEmpty()){
                                        Map<Double, Cinema> cinemaMap = new TreeMap();

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                for (Cinema cinema : cinemas) {
                                                    Location l2 = new Location("");
                                                    System.out.println("cinema: " + cinema);
                                                    l2.setLatitude(cinema.getLocation().getLatitude());
                                                    l2.setLongitude(cinema.getLocation().getLongitude());

                                                    LatLng location = new LatLng(cinema.getLocation().getLatitude(), cinema.getLocation().getLongitude());

                                                    //Location.distanceTo tính khoảng cách bằng Euclidean distance

                                                    //Tính khoảng cách giữa 2 điểm trên bề mặt hình cầu bằng công thức Haversine
                                                    double distance = SphericalUtil.computeDistanceBetween(currLocation, location);
                                                    cinemaMap.put(distance/1000.0, cinema);

                                                    System.out.println("Distance:"+distance);
                                                }
                                                cinemaListAdapter.updateCinemas(cinemaMap);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.d("Cinemas Error:", e.getMessage());
                                }
                            });
                        }else{
                            System.out.println("IT NULL WTF");

                            getCinema();
                        }

                    }else{
                        System.out.println("IT NOT WORKING");
                    }

                }
            });


        }
        else{
            getCinema();
        }
    }

    public void getCurrentLocation(){
        //Lấy vị trí hiện tại của thiết bị
        flpClient = LocationServices.getFusedLocationProviderClient(this);
        flpClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    System.out.println("IT WORKING");
                    LatLng curr = new LatLng(location.getLatitude(), location.getLongitude());
                    currLocation = curr;
                }else{
                    System.out.println("IT NOT WORKING");
                }

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }
    }

}