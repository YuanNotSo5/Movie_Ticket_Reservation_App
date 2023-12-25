package com.example.appbookticketmovie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbookticketmovie.Models.seatInfo;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.FilmService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InvoiceInfoAdapter extends RecyclerView.Adapter<InvoiceInfoAdapter.ViewHolder> {
    private ArrayList<seatInfo> data;
    private Context context;
    private String currDate = null;
    private Intent intent;
    private String nameFilm, nameCinema, date, time, idRoom, addressCinema;
    private long total;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView ticketCinemaName, ticketFilmName, ticketTime, ticketDate, ticketSeatRow, ticketSeatType, ticketRoom, ticketAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketCinemaName = itemView.findViewById(R.id.ticketCinemaName);
            ticketFilmName = itemView.findViewById(R.id.ticketFilmName);
            ticketTime = itemView.findViewById(R.id.ticketTime);
            ticketDate = itemView.findViewById(R.id.ticketDate);
            ticketSeatRow = itemView.findViewById(R.id.ticketSeatRow);
            ticketSeatType = itemView.findViewById(R.id.ticketSeatType);
            ticketRoom = itemView.findViewById(R.id.ticketRoom);
            ticketAddress = itemView.findViewById(R.id.ticketAddress);
        }
    }

    public InvoiceInfoAdapter(Context context, ArrayList<seatInfo> data, Intent intent) {
        this.context = context;
        this.data = data;
        this.intent = intent;

        nameFilm = intent.getStringExtra("nameFilm");
        nameCinema = intent.getStringExtra("nameCinema");
        addressCinema = intent.getStringExtra("addressCinema");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        idRoom = intent.getStringExtra("idRoom");
        total = intent.getLongExtra("total", 0);
    }

    @NonNull
    @Override
    public InvoiceInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_ticket_info, parent, false);
        return new InvoiceInfoAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull InvoiceInfoAdapter.ViewHolder holder, int position) {
        holder.ticketCinemaName.setText(nameCinema);
        holder.ticketFilmName.setText(nameFilm);
        holder.ticketTime.setText(time);
        holder.ticketDate.setText(date);

        holder.ticketSeatRow.setText(data.get(position).getSeat());
        holder.ticketSeatType.setText(data.get(position).getType());
        holder.ticketRoom.setText(idRoom);

        holder.ticketAddress.setText(addressCinema);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
