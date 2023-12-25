package com.example.appbookticketmovie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbookticketmovie.Models.Cinema;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.Models.Schedule;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.FilmService;
import com.example.appbookticketmovie.Services.ScheduleService;
import com.google.firebase.Timestamp;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {
    private ScheduleService scheduleService;
    private ArrayList<Schedule> data;
    private Context context;
    private long idFilm;
    private FilmItem filmItem;
    private String date = null;
    private Intent seat;
    private ArrayList<Cinema> cinemas;
    private String nameFilm;
    private Double extra_price;
    private FilmService filmService;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView timeStart, timeEnd;
        RecyclerView recyclerViewSchedule;

        LinearLayout scheduleLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeStart = itemView.findViewById(R.id.tvTimeStart);
            timeEnd = itemView.findViewById(R.id.tvTimeEnd);

            scheduleLayout = itemView.findViewById(R.id.tvSchedule);
        }
    }

    @NonNull
    @Override
    public ScheduleListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_schedule, parent, false);
        return new ScheduleListAdapter.ViewHolder(view);
    }

    public ScheduleListAdapter(Context context, ArrayList<Schedule> data , String date, Intent seat, ArrayList<Cinema> cinemas, Long idFilm, Double extra_price) {
        this.data = data;
        this.context = context;
        this.scheduleService = new ScheduleService();
        this.date = date;
        this.seat = seat;
        this.cinemas = cinemas;
        this.idFilm = idFilm;
        this.extra_price = extra_price;
        this.filmService = new FilmService();
        getFilmItem();
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleListAdapter.ViewHolder holder, int position) {
        scheduleService.getTime(this.idFilm, data.get(position), new ScheduleService.getTimeSchedules() {
            @Override
            public void getTimeSchedules(String filmName, String timeStart, String timeEnd) {
                holder.timeStart.setText(timeStart);
                holder.timeEnd.setText(timeEnd);
                nameFilm = filmName;
            }

            @Override
            public void onFailure(String errMess) {
                Log.d("Schedule List Error:", errMess);
            }
        });

        holder.scheduleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cinemaName = cinemas.get(position).getName();
                String cinemaAddress = cinemas.get(position).getAddress();
                Integer idCinema = Integer.valueOf(data.get(position).getIdCinema().toString());

                seat.putExtra("time", holder.timeStart.getText().toString() + " - " + holder.timeEnd.getText().toString());
                seat.putExtra("date", date);
                seat.putExtra("nameFilm", nameFilm);
                seat.putExtra("idRoom", data.get(position).getRoom());
                seat.putExtra("idCinema", idCinema);
                seat.putExtra("addressCinema", cinemaAddress);
                seat.putExtra("idFilm", idFilm);


                System.out.println("cinemas: " + cinemaName);

                Double price = data.get(position).getPrice();
                Double finalPrice = price + extra_price;

                String formattedPrice = new DecimalFormat("0.00").format(finalPrice);
                String price_str = formattedPrice.split("\\.")[0];
                seat.putExtra("price", Long.valueOf(price_str));

                context.startActivity(seat);

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateIntent(Intent seat){
        this.seat = seat;
    }

    public void getFilmItem(){
        filmService.getFilmById(this.idFilm, new FilmService.OnFilmDataReceivedListener() {
            @Override
            public void onFilmDataReceived(ArrayList<FilmItem> listFilms) {
                if(!listFilms.isEmpty()) {
                    FilmItem item = listFilms.get(0);
                    filmItem = item;
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}
