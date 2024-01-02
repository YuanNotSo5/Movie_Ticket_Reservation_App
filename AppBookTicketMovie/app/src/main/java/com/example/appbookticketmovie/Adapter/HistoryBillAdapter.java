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
import com.example.appbookticketmovie.HomeActivities.DetailBill;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.ListBill;
import com.example.appbookticketmovie.Models.ListFilmFavorite;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.UserService;

import java.util.ArrayList;

public class HistoryBillAdapter extends RecyclerView.Adapter <HistoryBillAdapter.ViewHolder>{
    ArrayList<ListBill> items;
    Context context;

    public HistoryBillAdapter(ArrayList<ListBill> item) {
        this.items = item;
    }

    @NonNull
    @Override
    public HistoryBillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_bill,parent, false);
        return new HistoryBillAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameFilmItemBillTxt.setText(items.get(position).getData().getNameFilm());
        holder.quantityFilmBill.setText(String.valueOf(items.get(position).getData().getListSeats().size()));
        holder.retailPriceBillTxt.setText(String.valueOf(items.get(position).getData().getListSeats().get(0).getPriceDetail()));
        holder.nameCinemaBillTxt.setText(items.get(position).getData().getNameCinema());
        holder.addressCinemaBillTxt.setText(items.get(position).getData().getAddressCinema());
        holder.buyingDateBillTxt.setText(items.get(position).getData().getBuyingdate());
        holder.methodBillTxt.setText(items.get(position).getData().getMethod());
        holder.pointBillTxt.setText(String.valueOf(items.get(position).getData().getPoint()));
        holder.totalBillTxt.setText(String.valueOf(items.get(position).getData().getTotal()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailBill.class);
            intent.putExtra("idBill", items.get(position).getIdBill());
            intent.putExtra("idUser", items.get(position).getData().getIdUser());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView nameFilmItemBillTxt, quantityFilmBill, retailPriceBillTxt, nameCinemaBillTxt, addressCinemaBillTxt, buyingDateBillTxt, methodBillTxt, pointBillTxt, totalBillTxt;
        ImageView imgMovie, isFavorite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameFilmItemBillTxt = itemView.findViewById(R.id.tvNameFilmBill);
            quantityFilmBill = itemView.findViewById(R.id.tvQuantityBill);
            retailPriceBillTxt = itemView.findViewById(R.id.tvPerPriceBill);
            nameCinemaBillTxt = itemView.findViewById(R.id.tvNameCinemaBill);
            addressCinemaBillTxt = itemView.findViewById(R.id.tvAddressBill);
            buyingDateBillTxt = itemView.findViewById(R.id.tvDateBuyBill);
            methodBillTxt = itemView.findViewById(R.id.tvPayBillMethod);
            pointBillTxt = itemView.findViewById(R.id.tvPointBill);
            totalBillTxt = itemView.findViewById(R.id.tvTotalAmountBill);
        }
    }
}
