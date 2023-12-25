package com.example.appbookticketmovie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbookticketmovie.Models.Cinema;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.Schedule;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.ScheduleService;

import org.jetbrains.annotations.Async;

import java.util.ArrayList;
import java.util.Date;

public class CinemaListAdapter extends RecyclerView.Adapter<CinemaListAdapter.ViewHolder> {

    private ScheduleService scheduleService;
    private ArrayList<Schedule> schedules;
    private Long idFilm;
    private String date = null;
    private ArrayList<Cinema> data;
    private Context context;
    private Intent seat;

    private Double extra_price;
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ArrayList<Schedule> schedulesList = new ArrayList<>();
        TextView name, address;
        RecyclerView recyclerViewSchedule;
        ScheduleListAdapter scheduleListAdapter = new ScheduleListAdapter(context, schedulesList, date, seat, data, idFilm, extra_price);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvCinemaName);
            address = itemView.findViewById(R.id.tvCinemaAddress);

            recyclerViewSchedule = itemView.findViewById(R.id.recyclerViewSchedule);
            recyclerViewSchedule.setAdapter(scheduleListAdapter);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_cinema, parent, false);
        return new ViewHolder(view);
    }

    public CinemaListAdapter(Context context, ArrayList<Cinema> data, Long idFilm, Intent seat, Double extra_price) {
        this.data = data;
        this.context = context;
        this.idFilm = idFilm;
        this.seat = seat;
        this.extra_price = extra_price;
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaListAdapter.ViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.address.setText(data.get(position).getAddress());

        Long idCinema = data.get(position).getId();
        ArrayList<Schedule> schedulesList = new ArrayList<>();

        holder.recyclerViewSchedule.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));

        getSchedule(idCinema, holder.schedulesList, holder.scheduleListAdapter);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void getSchedule(Long idCinema, ArrayList<Schedule> schedulesList, ScheduleListAdapter scheduleListAdapter) {
        scheduleService = new ScheduleService();
        if (this.date != null) {
//            System.out.println("idCinema: "+idCinema);
//            System.out.println("idFilm: "+idFilm);
//            System.out.println("date: "+date);
            scheduleService.getCinemaSchedules(idCinema, this.idFilm, this.date, new ScheduleService.getSchedules() {
                @Override
                public void getCinemaSchedules(ArrayList<Schedule> schedules) {
                    schedulesList.clear();
                    scheduleListAdapter.notifyDataSetChanged();

                    schedulesList.addAll(schedules);
                    scheduleListAdapter.notifyDataSetChanged();
//                    System.out.println("list: "+ schedulesList);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("Schedule Error: ", e.getMessage());
                }
            });
        }
    }
    public void updateDate(String newDate){
        this.date = newDate;
    }
    public void updateIntent(Intent seat){
        this.seat = seat;
    }

    public void updateExtraPrice(Double extra_price){
        this.extra_price = extra_price;
    }
}
