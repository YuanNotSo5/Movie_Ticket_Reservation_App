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
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.CinemaService;
import com.example.appbookticketmovie.Services.FilmService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class DayListAdapter extends RecyclerView.Adapter<DayListAdapter.ViewHolder>{
    ArrayList<Date> data;
    Context context;
    private String currDate = null;
    private CinemaListAdapter cinemaListAdapter;
    private FilmService filmService;
    private Intent seat;

    private Map<Integer, ViewHolder> viewHolderMap;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView day, dateInWk;
        LinearLayout day_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.tvDay);
            day_layout = itemView.findViewById(R.id.day_layout);
            dateInWk = itemView.findViewById(R.id.tvDate);
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
//        if(!data.isEmpty()){
//            String currentDay = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(data.get(0));
//            this.currDate = currentDay;
//        }else{
//            String currentDay = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//            this.currDate = currentDay;
//        }
//        cinemaListAdapter.updateDate(this.currDate);
        viewHolderMap = new TreeMap<>();
    }

    @Override
    public void onBindViewHolder(@NonNull DayListAdapter.ViewHolder holder, int position) {
        viewHolderMap.put(position, holder);
        Date day = data.get(position);
        String currentDay = new SimpleDateFormat("dd", Locale.getDefault()).format(day);

        holder.day.setText(currentDay);
        int dayInWeek = day.getDay();
        switch(dayInWeek){
            case 0:
                holder.dateInWk.setText("Sun");
                break;
            case 1:
                holder.dateInWk.setText("Mon");
                break;
            case 2:
                holder.dateInWk.setText("Tues");
                break;
            case 3:
                holder.dateInWk.setText("Wed");
                break;
            case 4:
                holder.dateInWk.setText("Thur");
                break;
            case 5:
                holder.dateInWk.setText("Fri");
                break;
            case 6:
                holder.dateInWk.setText("Sat");
                break;
            default:
                break;
        }

        holder.day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dayClicked = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(data.get(position));

                currDate = dayClicked;
                cinemaListAdapter.updateDate(dayClicked);
                cinemaListAdapter.notifyDataSetChanged();

                int dayInWeek = day.getDay();
                if(dayInWeek == 0 || dayInWeek == 6){
                    cinemaListAdapter.updateExtraPrice(20000.0);
                }else{
                    cinemaListAdapter.updateExtraPrice(0.0);
                }
//                updateBackground();
//                holder.day_layout.setBackgroundResource(R.drawable.date_background_selected);
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
        for(int i = 0; i < data.size(); i++){
            ViewHolder holder =  viewHolderMap.get(i);
            holder.day_layout.setBackgroundResource(R.drawable.date_background);
        }
    }
    public void updateIntent(Intent seat){
        this.seat = seat;
    }


}
