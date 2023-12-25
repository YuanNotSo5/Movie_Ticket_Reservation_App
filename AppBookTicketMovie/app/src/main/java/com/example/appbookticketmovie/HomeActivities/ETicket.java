package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.appbookticketmovie.Adapter.ActorsListAdapter;
import com.example.appbookticketmovie.Adapter.ETicketAdapter;
import com.example.appbookticketmovie.Models.Ticket;
import com.example.appbookticketmovie.R;

import java.util.ArrayList;

public class ETicket extends AppCompatActivity {
    private RecyclerView.Adapter adapterTicket;
    private RecyclerView recyclerViewTicket;
    ArrayList<Ticket> ticketInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eticket);
        init();
        ticketInfo = new ArrayList<>();
        ticketInfo.add(new Ticket("Film1", "12:00", "2023-01-01", "Cinema1", "A1", "Room1", "Standard", 10000));
        ticketInfo.add(new Ticket("Film2", "15:30", "2023-01-02", "Cinema2", "B3", "Room2", "VIP", 30000));
        ticketInfo.add(new Ticket("Film2", "15:30", "2023-01-02", "Cinema2", "B3", "Room2", "VIP", 30000));
        ticketInfo.add(new Ticket("Film2", "15:30", "2023-01-02", "Cinema2", "B3", "Room2", "VIP", 30000));
        ticketInfo.add(new Ticket("Film2", "15:30", "2023-01-02", "Cinema2", "B3", "Room2", "VIP", 30000));
        ticketInfo.add(new Ticket("Film2", "15:30", "2023-01-02", "Cinema2", "B3", "Room2", "VIP", 30000));
        ticketInfo.add(new Ticket("Film2", "15:30", "2023-01-02", "Cinema2", "B3", "Room2", "VIP", 30000));


        adapterTicket=new ETicketAdapter(ticketInfo);
        recyclerViewTicket.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTicket.setAdapter(adapterTicket);

    }

    public void init() {
        recyclerViewTicket = findViewById(R.id.ticketContainer);
    }

}