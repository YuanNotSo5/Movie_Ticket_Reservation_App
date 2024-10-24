package com.example.appbookticketmovie.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_ticket,parent,false);
        return new ViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dateTxt.setText(listTicket.get(position).getDate());
        holder.timeTxt.setText(listTicket.get(position).getTime());
        holder.nameFilmTxt.setText(listTicket.get(position).getFilm());
        holder.addressTxt.setText(listTicket.get(position).getCinema());
        holder.roomTxt.setText(listTicket.get(position).getRoom());
        holder.seatTxt.setText(listTicket.get(position).getNumberSeat());
        holder.typeSeatTxt.setText(listTicket.get(position).getTypeSeat());

        holder.dateMainTxt.setText(listTicket.get(position).getDate());
        holder.timeMainTxt.setText(listTicket.get(position).getTime());
        holder.nameFilmMainTxt.setText(listTicket.get(position).getFilm());
        holder.addressMainTxt.setText(listTicket.get(position).getCinema());
        holder.roomMainTxt.setText(listTicket.get(position).getRoom());
        holder.seatMainTxt.setText(listTicket.get(position).getNumberSeat());
        holder.typeSeatMainTxt.setText(listTicket.get(position).getTypeSeat());

        String barcode = listTicket.get(position).getBarcode();
        holder.mainBarcode.setImageBitmap(listTicket.get(position).stringToBitmap(barcode));

        //Qr code
        String qrCode = listTicket.get(position).getQrcode();
        holder.qrcode.setImageBitmap(listTicket.get(position).stringToBitmap(qrCode));

        holder.convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.convertBtn.getText()=="CONVERT TO QRCODE"){
                    holder.convertBtn.setText("CONVERT TO BARCODE");
                    holder.mainBarcode.setVisibility(View.GONE);
                    holder.qrcode.setVisibility(View.VISIBLE);
                }
                else{
                    holder.convertBtn.setText("CONVERT TO QRCODE");
                    holder.qrcode.setVisibility(View.GONE);
                    holder.mainBarcode.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return listTicket.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTxt, timeTxt, nameFilmTxt, addressTxt, roomTxt, seatTxt, typeSeatTxt, dateMainTxt, timeMainTxt, nameFilmMainTxt, addressMainTxt, roomMainTxt, seatMainTxt, typeSeatMainTxt;
        ImageView barcode, mainBarcode, qrcode;
        Button convertBtn;
        CardView cardView, cardview1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            nameFilmTxt = itemView.findViewById(R.id.nameFilmTxt);
            addressTxt = itemView.findViewById(R.id.addressTxt);
            roomTxt = itemView.findViewById(R.id.roomTxt);
            seatTxt = itemView.findViewById(R.id.seatTxt);
            typeSeatTxt = itemView.findViewById(R.id.typeSeatTxt);
            barcode = itemView.findViewById(R.id.barcode);
            mainBarcode = itemView.findViewById(R.id.BarCode_Image);
            dateMainTxt = itemView.findViewById(R.id.ticketDate);
            timeMainTxt = itemView.findViewById(R.id.ticketTime);
            nameFilmMainTxt = itemView.findViewById(R.id.ticketFilmName);
            addressMainTxt = itemView.findViewById(R.id.ticketAddress);
            roomMainTxt = itemView.findViewById(R.id.ticketRoom);
            seatMainTxt = itemView.findViewById(R.id.ticketSeatRow);
            typeSeatMainTxt = itemView.findViewById(R.id.ticketSeatType);
            qrcode = itemView.findViewById(R.id.QrCode_Image);
            convertBtn = itemView.findViewById(R.id.convertBarcode);

            cardView = itemView.findViewById(R.id.cardView);
            cardview1 = itemView.findViewById(R.id.cardView2);
        }
    }
}
