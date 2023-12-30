package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.appbookticketmovie.Adapter.CinemaListAdapter;
import com.example.appbookticketmovie.Adapter.DayListAdapter;
import com.example.appbookticketmovie.HomeActivities.BookMapSeat;
import com.example.appbookticketmovie.Models.Cinema;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.CinemaService;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChooseCinemaActivity extends AppCompatActivity {
    RecyclerView recyclerViewCinema, recyclerViewSchedule, recyclerViewDay;
    DayListAdapter dayListAdapter;
    CinemaListAdapter cinemaListAdapter;
    private ArrayList<Cinema> cinemaList = new ArrayList<>();
    private ArrayList<Date> dayList = new ArrayList<>();
    private CinemaService cinemaService;
    private long idFilm;
    private Double extra_price = 0.0;

    private boolean backToDetailsRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cinema);
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
        getCinema();
    }

    public void getCinema(){
        cinemaService.getAllCinema(new CinemaService.getCinemas() {
            @Override
            public void getCinemas(ArrayList<Cinema> cinemas) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Cinema cinema : cinemas) {
                            cinemaList.add(cinema);
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


}