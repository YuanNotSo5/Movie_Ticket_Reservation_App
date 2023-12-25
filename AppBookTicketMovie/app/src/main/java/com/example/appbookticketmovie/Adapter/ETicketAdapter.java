package com.example.appbookticketmovie.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbookticketmovie.Models.ActorItem;
import com.example.appbookticketmovie.Models.Ticket;
import com.example.appbookticketmovie.R;

import java.util.List;

public class ETicketAdapter extends RecyclerView.Adapter <ETicketAdapter.ViewHolder>{
    List<Ticket> listTicket;
    Context context;

    public ETicketAdapter(List<Ticket> listTicket) {
        this.listTicket = listTicket;
    }

    @NonNull
    @Override
    public ETicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_ticket,parent,false);
        return new ETicketAdapter.ViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ETicketAdapter.ViewHolder holder, int position) {
        holder.dateTxt.setText(listTicket.get(position).getDate());
        holder.timeTxt.setText(listTicket.get(position).getTime());
        holder.nameFilmTxt.setText(listTicket.get(position).getFilm());
        holder.addressTxt.setText(listTicket.get(position).getCinema());
        holder.roomTxt.setText(listTicket.get(position).getRoom());
        holder.seatTxt.setText(listTicket.get(position).getNumberSeat());
        holder.typeSeatTxt.setText(listTicket.get(position).getTypeSeat());

    }

    @Override
    public int getItemCount() {
        return listTicket.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTxt, timeTxt, nameFilmTxt, addressTxt, roomTxt, seatTxt, typeSeatTxt, dateMainTxt, timeMainTxt, nameFilmMainTxt, addressMainTxt, roomMainTxt, seatMainTxt, typeSeatMainTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            nameFilmTxt = itemView.findViewById(R.id.nameFilmTxt);
            addressTxt = itemView.findViewById(R.id.addressTxt);
            roomTxt = itemView.findViewById(R.id.roomTxt);
            seatTxt = itemView.findViewById(R.id.seatTxt);
            typeSeatTxt = itemView.findViewById(R.id.typeSeatTxt);
        }
    }
}
