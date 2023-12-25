package com.example.appbookticketmovie.Adapter;

import android.content.Context;
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
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.ListFilm;
import com.example.appbookticketmovie.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter <SearchAdapter.ViewHolder> {
    ListFilm items;
    Context context;

    public SearchAdapter(ListFilm item) {
        this.items = item;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_item_search,parent, false);
        return new SearchAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.nameFilmItemTxt.setText(items.getData().get(position).getTitle());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(context)
                .load(items.getData().get(position).getPoster())
                .apply(requestOptions)
                .into(holder.imgMovie);

        ArrayList<GenreItem> listGenre = items.getData().get(position).getGenres();
        String nameCate = "";
        for (GenreItem item : listGenre) {
            nameCate = nameCate + "," + item.getName();
        }

        holder.cateItemTxt.setText(nameCate);
        holder.timeItemTxt.setText(items.getData().get(position).getRuntime());
        holder.rateItemTxt.setText(items.getData().get(position).getImdbRating());
    }

    @Override
    public int getItemCount() {
        return items.getData().size();
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
