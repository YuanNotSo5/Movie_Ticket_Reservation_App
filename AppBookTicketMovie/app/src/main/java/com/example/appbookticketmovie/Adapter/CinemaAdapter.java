package com.example.appbookticketmovie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.appbookticketmovie.HomeActivities.DetailActivity;
import com.example.appbookticketmovie.Models.Cinema;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.ListFilm;
import com.example.appbookticketmovie.R;

import java.util.ArrayList;

public class CinemaAdapter extends RecyclerView.Adapter <CinemaAdapter.ViewHolder> {
    ArrayList<Cinema> items;
    Context context;

    public CinemaAdapter(ArrayList<Cinema> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CinemaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_item_search,parent, false);
        return new CinemaAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameFilmItemTxt.setText(items.get(position).getName());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(context)
                .load("https://firebasestorage.googleapis.com/v0/b/android-movie-ticket-booking.appspot.com/o/Logo%2Fcgvlogo.jpg?alt=media&token=a104a570-97e5-4e0e-973f-2a21bdd7d54d")
                .apply(requestOptions)
                .into(holder.imgMovie);

        holder.cateItemTxt.setText(items.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView nameFilmItemTxt, timeItemTxt, rateItemTxt, cateItemTxt;
        ImageView imgMovie;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameFilmItemTxt = itemView.findViewById(R.id.nameFilmItemTxt);
            timeItemTxt = itemView.findViewById(R.id.timeItemTxt);
            rateItemTxt = itemView.findViewById(R.id.rateItemTxt);
            cateItemTxt = itemView.findViewById(R.id.cateItemTxt);
            imgMovie = itemView.findViewById(R.id.imgFilm);
        }
    }
}