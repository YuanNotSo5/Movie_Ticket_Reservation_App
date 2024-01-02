package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.appbookticketmovie.Adapter.ETicketAdapter;
import com.example.appbookticketmovie.Models.Ticket;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.UserService;

import java.util.ArrayList;

public class DetailBill extends AppCompatActivity {

    private long idUser;
    private String idBill;
    private UserService userService = new UserService();
    private RecyclerView.Adapter adapterTicket;
    private RecyclerView recyclerViewTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);

        idUser = getIntent().getLongExtra("idUser",0);
        idBill = getIntent().getStringExtra("idBill");
        init();
        userService.getDetailsOfBill(idUser, idBill, new UserService.DetailBillReceivedListener() {
            @Override
            public void onSuccess(ArrayList<Ticket> details) {
                adapterTicket = new ETicketAdapter(details);

                Log.d("llllll", String.valueOf(details));

                recyclerViewTicket.setAdapter(adapterTicket);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }

    private void init(){
        recyclerViewTicket = findViewById(R.id.containerTicketDetailBill);
        recyclerViewTicket.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
}