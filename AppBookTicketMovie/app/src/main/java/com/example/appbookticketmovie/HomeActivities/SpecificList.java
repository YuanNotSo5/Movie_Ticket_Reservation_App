package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appbookticketmovie.Adapter.CinemaAdapter;
import com.example.appbookticketmovie.Adapter.FilmListAdapter;
import com.example.appbookticketmovie.Adapter.SearchAdapter;
import com.example.appbookticketmovie.Models.ActorItem;
import com.example.appbookticketmovie.Models.Cinema;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.ListFilm;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.CinemaService;
import com.example.appbookticketmovie.Services.FilmService;

import java.util.ArrayList;

public class SpecificList extends AppCompatActivity {

    EditText search;
    TextView cancelResult, waitingTxt, findCinema, findFilm;
    ArrayList<FilmItem> searchResults = new ArrayList<>();
    ArrayList<FilmItem> listFilm;
    ArrayList<Cinema> searchCinemaResults = new ArrayList<>();
    ArrayList<Cinema> listCinema;

    private RecyclerView.Adapter adapterSearch;
    private RecyclerView recyclerViewResult;
    ListFilm items;
    boolean isFindCinema = false;

    private FilmService filmService = new FilmService();
    private CinemaService cinemaService = new CinemaService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_list);
        getSupportActionBar().hide();

        init();
        getList();
        getListCinema();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    waitingTxt.setVisibility(View.VISIBLE);
                    recyclerViewResult.setVisibility(View.GONE);
                } else {
                    if(isFindCinema){
                        resultFindCinema(charSequence.toString());
                    }else{
                        performSearch(charSequence.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    search.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.search, 0);
                    search.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_color)));
                } else {
                    search.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
                    search.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_color)));
                }
            }
        });


        cancelResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findCinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable newBackground = getResources().getDrawable(R.drawable.schedule_background);
                findCinema.setBackground(newBackground);
                Drawable originalBackground = getResources().getDrawable(R.drawable.cinema_background);
                findFilm.setBackground(originalBackground);
                isFindCinema = true;
                waitingTxt.setVisibility(View.VISIBLE);
                recyclerViewResult.setVisibility(View.GONE);
            }
        });
        findFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable newBackground = getResources().getDrawable(R.drawable.schedule_background);
                findFilm.setBackground(newBackground);
                Drawable originalBackground = getResources().getDrawable(R.drawable.cinema_background);
                findCinema.setBackground(originalBackground);
                isFindCinema = false;
                waitingTxt.setVisibility(View.VISIBLE);
                recyclerViewResult.setVisibility(View.GONE);
            }
        });
    }

    private void resultFindCinema(String query) {
        recyclerViewResult.setVisibility(View.VISIBLE);
        searchCinemaResults.clear();
        for (Cinema item : listCinema) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                searchCinemaResults.add(item);
            }
        }
        if(searchCinemaResults.size()==0){
            for (Cinema item : listCinema) {
                if (item.getAddress().toLowerCase().contains(query.toLowerCase())) {
                    searchCinemaResults.add(item);
                }
            }
        }

        if(searchCinemaResults.size()!=0){
            adapterSearch = new CinemaAdapter(searchCinemaResults);
            recyclerViewResult.setAdapter(adapterSearch);
            waitingTxt.setVisibility(View.GONE);

        }else{
            waitingTxt.setVisibility(View.VISIBLE);
        }
    }

    private void performSearch(String query) {
        recyclerViewResult.setVisibility(View.VISIBLE);
        searchResults.clear();
        for (FilmItem item : listFilm) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(item);
            }
        }
        if(searchResults.size()==0){
            for (FilmItem item : listFilm) {
                if (item.getPlot().toLowerCase().contains(query.toLowerCase())) {
                    searchResults.add(item);
                }
            }
        }
        if(searchResults.size()==0){
            for (FilmItem item : listFilm) {
                for (ActorItem actorItem : item.getActors()){
                    if (actorItem.getName().toLowerCase().contains(query.toLowerCase())) {
                        searchResults.add(item);
                    }
                }
            }
        }
        if(searchResults.size()!=0){
            items = new ListFilm(searchResults);
            adapterSearch = new SearchAdapter(items);
            recyclerViewResult.setAdapter(adapterSearch);
            waitingTxt.setVisibility(View.GONE);

        }else{
            waitingTxt.setVisibility(View.VISIBLE);
        }
    }
    private void getList() {
        listFilm = new ArrayList<>();
        filmService.getFilmCloudStore(new FilmService.OnFilmDataReceivedListener() {
            @Override
            public void onFilmDataReceived(ArrayList<FilmItem> listFilms) {
                listFilm = listFilms;
            }
            @Override
            public void onError(String errorMessage) {
                Log.d("Error", errorMessage);
            }
        });
    }

    private void getListCinema(){
        listCinema = new ArrayList<>();
        cinemaService.getAllCinema(new CinemaService.getCinemas() {
            @Override
            public void getCinemas(ArrayList<Cinema> cinemas) {
                listCinema = cinemas;
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


    public void init(){
        recyclerViewResult = findViewById(R.id.searchResultContainer);
        recyclerViewResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        search = findViewById(R.id.searchItem);
        cancelResult = findViewById(R.id.cancelItem);
        waitingTxt = findViewById(R.id.waitingTxt);
        findCinema = findViewById(R.id.findCinema);
        findFilm = findViewById(R.id.findFilm);

    }
}