package com.example.appbookticketmovie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbookticketmovie.Models.Cinema;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.CinemaService;
import com.example.appbookticketmovie.Services.FilmService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DayListAdapter extends RecyclerView.Adapter<DayListAdapter.ViewHolder>{
    ArrayList<Date> data;
    Context context;
    private String currDate = null;
    private CinemaListAdapter cinemaListAdapter;
    private FilmService filmService;
    private Intent seat;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView day;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.tvDay);
        }
    }

    @NonNull
    @Override
    public DayListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_day, parent, false);
        return new DayListAdapter.ViewHolder(view);
    }

    public DayListAdapter(Context context, ArrayList<Date> data, CinemaListAdapter cinemaListAdapter, Intent seat) {
        this.data = data;
        this.context = context;
        this.cinemaListAdapter = cinemaListAdapter;
        this.seat = seat;
        filmService = new FilmService();
        if(!data.isEmpty()){
            String currentDay = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(data.get(0));
            this.currDate = currentDay;
        }else{
            String currentDay = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            this.currDate = currentDay;
        }
        cinemaListAdapter.updateDate(this.currDate);
    }

    @Override
    public void onBindViewHolder(@NonNull DayListAdapter.ViewHolder holder, int position) {
        Date day = data.get(position);
        String currentDay = new SimpleDateFormat("dd", Locale.getDefault()).format(day);

        holder.day.setText(currentDay);
        TextView view = holder.day;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dayClicked = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(day);

                currDate = dayClicked;
                cinemaListAdapter.updateDate(dayClicked);
                cinemaListAdapter.notifyDataSetChanged();

                int dayInWeek = day.getDay();
                if(dayInWeek == 0 || dayInWeek == 6){
                    cinemaListAdapter.updateExtraPrice(20000.0);
                }else{
                    cinemaListAdapter.updateExtraPrice(0.0);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public String getCurrDate(){
        return this.currDate;
    }
    public void updateBackground(){

    }
    public void updateIntent(Intent seat){
        this.seat = seat;
    }


}