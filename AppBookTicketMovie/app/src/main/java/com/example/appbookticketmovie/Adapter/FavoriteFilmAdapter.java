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
import com.example.appbookticketmovie.HomeActivities.SortFilmItem;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.ListFilm;
import com.example.appbookticketmovie.Models.ListFilmFavorite;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.UserService;

import java.util.ArrayList;

public class FavoriteFilmAdapter extends RecyclerView.Adapter <FavoriteFilmAdapter.ViewHolder>{
    ArrayList<ListFilmFavorite> items;
    Context context;

    public FavoriteFilmAdapter(ArrayList<ListFilmFavorite> item) {
        this.items = item;
    }

    @NonNull
    @Override
    public FavoriteFilmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_item_search,parent, false);
        return new FavoriteFilmAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameFilmItemTxt.setText(items.get(position).getData().get(0).getTitle());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
        Glide.with(context)
                .load(items.get(position).getData().get(0).getPoster())
                .apply(requestOptions)
                .into(holder.imgMovie);

        ArrayList<GenreItem> listGenre = items.get(position).getData().get(0).getGenres();
        String nameCate = "";
        for (GenreItem item : listGenre) {
            nameCate = nameCate + "," + item.getName();
        }

        holder.cateItemTxt.setText(nameCate);
        holder.timeItemTxt.setText(items.get(position).getData().get(0).getRuntime());
        holder.rateItemTxt.setText(items.get(position).getData().get(0).getImdbRating());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("id", items.get(position).getData().get(0).getId());
            context.startActivity(intent);
        });

        holder.isFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserService userService = new UserService();
                userService.updateStateFavFilm(false, items.get(position).getIdDocument(), new UserService.FavoriteReceivedListener() {
                    @Override
                    public void onSuccess(boolean isFavoriteListener, String idDocument) {
                        items.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView nameFilmItemTxt, timeItemTxt, rateItemTxt, cateItemTxt;
        ImageView imgMovie, isFavorite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameFilmItemTxt = itemView.findViewById(R.id.nameFilmItemTxt);
            timeItemTxt = itemView.findViewById(R.id.timeItemTxt);
            rateItemTxt = itemView.findViewById(R.id.rateItemTxt);
            cateItemTxt = itemView.findViewById(R.id.cateItemTxt);
            imgMovie = itemView.findViewById(R.id.imgFilm);
            isFavorite = itemView.findViewById(R.id.isFavorite);
            isFavorite.setVisibility(View.VISIBLE);

        }
    }
}
