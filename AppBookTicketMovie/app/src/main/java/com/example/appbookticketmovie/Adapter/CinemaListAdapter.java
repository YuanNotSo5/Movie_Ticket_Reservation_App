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

import com.example.appbookticketmovie.HomeActivities.MapActivity;
import com.example.appbookticketmovie.Models.Cinema;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.Schedule;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.ScheduleService;

import org.jetbrains.annotations.Async;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class CinemaListAdapter extends RecyclerView.Adapter<CinemaListAdapter.ViewHolder> {

    private ScheduleService scheduleService;
    private ArrayList<Schedule> schedules;
    private Long idFilm;
    private String date = null;
    private String prev_date = null;
    private Map<Double, Cinema> cinemas;
    private ArrayList<Cinema> data;
    private ArrayList<Double> distances;
    private Context context;
    private Intent seat;
    private Long idCinemaTmp;
    private CinemaListAdapter.ViewHolder holderTmp;
    private Double extra_price;
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ArrayList<Schedule> schedulesList = new ArrayList<>();
        TextView name, address, distance, map;
        RecyclerView recyclerViewSchedule;
        ScheduleListAdapter scheduleListAdapter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvCinemaName);
            address = itemView.findViewById(R.id.tvCinemaAddress);
            distance = itemView.findViewById(R.id.tvCinemaDistance);
            map = itemView.findViewById(R.id.tvMap);

            recyclerViewSchedule = itemView.findViewById(R.id.recyclerViewSchedule);
            scheduleListAdapter = new ScheduleListAdapter(context, schedulesList, date, seat, data, idFilm, extra_price);
            recyclerViewSchedule.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
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

    public CinemaListAdapter(Context context, Map<Double, Cinema> cinemas, Long idFilm, Intent seat, Double extra_price) {
        this.cinemas = cinemas;
        this.context = context;
        this.idFilm = idFilm;
        this.seat = seat;
        this.extra_price = extra_price;
        this.scheduleService = new ScheduleService();
        this.data = new ArrayList<>(cinemas.values());
        this.distances = new ArrayList<>(cinemas.keySet());
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaListAdapter.ViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.address.setText(data.get(position).getAddress());

        double rounDistance = Math.round(distances.get(position)* Math.pow(10, 2)) / Math.pow(10, 2);
        holder.distance.setText(String.valueOf(rounDistance) + " km");
        Long idCinema = data.get(position).getId();

        if(this.date != null){
            if (!this.date.equals(this.prev_date)) {
                getSchedule(idCinema, holder.schedulesList, holder.scheduleListAdapter, holder.recyclerViewSchedule);
            }
        }

        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cinemaMap = new Intent(context, MapActivity.class);
                cinemaMap.putExtra("cinemaName", data.get(position).getName());
                cinemaMap.putExtra("cinemaAddress", data.get(position).getAddress());
                cinemaMap.putExtra("Lat", data.get(position).getLocation().getLatitude());
                cinemaMap.putExtra("Lng", data.get(position).getLocation().getLongitude());

                context.startActivity(cinemaMap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateDate(String newDate){
//        this.prev_date = this.date;
        this.date = newDate;
        seat.removeExtra("date");
        seat.putExtra("date", date);
        notifyDataSetChanged();
    }
    public void getSchedule(Long idCinema, ArrayList<Schedule> schedulesList, ScheduleListAdapter scheduleListAdapter, RecyclerView recyclerViewSchedule) {
        if (this.date != null) {
            System.out.println("idCinema: "+idCinema);
            System.out.println("idFilm: "+idFilm);
            System.out.println("date: "+date);
            scheduleService.getCinemaSchedules(idCinema, idFilm, this.date, new ScheduleService.getSchedules() {
                @Override
                public void getCinemaSchedules(ArrayList<Schedule> schedules) {
                    schedulesList.clear();
                    scheduleListAdapter.notifyDataSetChanged();

                    prev_date = date;

                    if(schedules.isEmpty()){
                        recyclerViewSchedule.setVisibility(View.GONE);
                        notifyDataSetChanged();
                    }
                    else{
                        schedulesList.addAll(schedules);
                        scheduleListAdapter.notifyDataSetChanged();

                        recyclerViewSchedule.setVisibility(View.VISIBLE);
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("Schedule Error: ", e.getMessage());
                }
            });
        }
    }

    public void updateCinemas(Map<Double, Cinema> cinemas){
        this.cinemas.clear();
        notifyDataSetChanged();

        this.cinemas.putAll(cinemas);
        this.data = new ArrayList<>(cinemas.values());
        this.distances = new ArrayList<>(cinemas.keySet());
        notifyDataSetChanged();
    }
    public void updateIntent(Intent seat){
        this.seat = seat;
    }

    public void updateExtraPrice(Double extra_price){
        this.extra_price = extra_price;
    }
}
