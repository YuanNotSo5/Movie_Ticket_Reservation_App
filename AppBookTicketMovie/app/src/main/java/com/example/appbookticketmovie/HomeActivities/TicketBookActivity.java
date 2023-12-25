package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbookticketmovie.Adapter.CinemaListAdapter;
import com.example.appbookticketmovie.Adapter.InvoiceInfoAdapter;
import com.example.appbookticketmovie.HomeActivities.ChooseCinemaActivity;
import com.example.appbookticketmovie.Models.User;
import com.example.appbookticketmovie.Models.seatInfo;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.UserService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketBookActivity extends AppCompatActivity {
    private ArrayList<seatInfo> seatInfos;
    private InvoiceInfoAdapter invoiceInfoAdapter;
    private UserService userService;
    private int pay = -1;
    private User currUser;
    private String nameFilm, nameCinema, date, time, idRoom, addressCinema;
    private long total, idFilm;
    RecyclerView recyclerView;
    LinearLayout paymentSection;
    Button cardPaymentBtn, checkOutBtn, cashPaymentBtn;
    TextInputEditText cardName, cardNumber;
    TextView tvPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_book);

        Intent book = getIntent();
        idFilm = book.getLongExtra("idFilm",0);
        nameFilm = book.getStringExtra("nameFilm");
        nameCinema = book.getStringExtra("nameCinema");
        addressCinema = book.getStringExtra("addressCinema");
        date = book.getStringExtra("date");
        time = book.getStringExtra("time");
        idRoom = book.getStringExtra("idRoom");
        total = book.getLongExtra("total",0);
        seatInfos = book.getParcelableArrayListExtra("bookseat");

        userService = new UserService();

        cardPaymentBtn = findViewById(R.id.button2);
        paymentSection = findViewById(R.id.card_payment_section);
        checkOutBtn = findViewById(R.id.button5);
        cashPaymentBtn = findViewById(R.id.button3);
        cardName = findViewById(R.id.cardName);
        cardNumber = findViewById(R.id.cardNumber);
        tvPrice = findViewById(R.id.tvPrice);

        recyclerView = findViewById(R.id.recyclerViewTicketInvoice);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        invoiceInfoAdapter = new InvoiceInfoAdapter(this, seatInfos, book);
        recyclerView.setAdapter(invoiceInfoAdapter);

        tvPrice.setText(String.valueOf(total) + "VND");
        getCard();
        cardPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentSection.setVisibility(View.VISIBLE);
                checkOutBtn.setVisibility(View.VISIBLE);
                pay = 1;
            }
        });

        cashPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutBtn.setVisibility(View.VISIBLE);
                paymentSection.setVisibility(View.GONE);
                pay = 0;
            }
        });

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, number;
                boolean status = true;
                if(pay == 1){
                    System.out.println("Payment Method: " + pay);
                    name = cardName.getText().toString();
                    number = cardNumber.getText().toString();
                    if(name.equals("")){
                        cardName.setError("Please Enter Your Card Name");
                        cardName.setEnabled(true);
                        status = false;
                    }
                    if(number.equals("")){
                        cardNumber.setError("Please Enter Your Card Number");
                        cardNumber.setEnabled(true);
                        status = false;
                    }
                    if(status) {
                        cardName.setEnabled(false);
                        cardNumber.setEnabled(false);

                        Map<String, String> newCard = new HashMap<>();
                        newCard.put("name", name);
                        newCard.put("number", number);

                        userService.updateCard(newCard, new UserService.cardUpdate() {
                            @Override
                            public void updateCard(boolean status) {
                                if (status) {
                                    Toast.makeText(TicketBookActivity.this, "Update New Card Successfully", Toast.LENGTH_SHORT).show();
                                    getETicketAct();
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.d("Update Card Error", e.getMessage());
                            }
                        });
                    }
                }else{
                    getETicketAct();
                }
            }
        });
    }
    public void getETicketAct(){
        Intent checkout = new Intent(TicketBookActivity.this, ETicket.class);

        checkout.putExtra("idFilm", idFilm);
        checkout.putExtra("paymentMethod", pay);
        checkout.putExtra("nameFilm", nameFilm);
        checkout.putExtra("nameCinema", nameCinema);
        checkout.putExtra("addressCinema", addressCinema);
        checkout.putExtra("date", date);
        checkout.putExtra("time", time);
        checkout.putExtra("idRoom", idRoom);
        checkout.putExtra("total", total);
        checkout.putParcelableArrayListExtra("bookseat", (ArrayList<? extends Parcelable>) seatInfos);
        startActivity(checkout);
    }

    public void getCard(){
        userService.getUserByEmail(new UserService.getUser() {
            @Override
            public void getUser(User user) {
                if(user != null){
                    currUser = user;
                    Map<String, String> card = user.getCard();
                    if(card.get("name") != null && card.get("number") != null){
                        cardName.setText(card.get("name"));
                        cardNumber.setText(card.get("number"));
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}