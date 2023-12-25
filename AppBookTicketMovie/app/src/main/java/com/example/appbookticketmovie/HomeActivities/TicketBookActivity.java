package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.appbookticketmovie.HomeActivities.ChooseCinemaActivity;
import com.example.appbookticketmovie.R;

public class TicketBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_book);

        Intent book = getIntent();
    }
}