package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appbookticketmovie.Adapter.SearchAdapter;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.Models.ListFilm;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.FilmService;

import java.util.ArrayList;

public class SortFilmActors extends AppCompatActivity {

    RecyclerView containerFilmActor;
    EditText search;

    TextView searchResultTxt;
    private RecyclerView.Adapter adapterFilm;
    private long idActor;

    private FilmService filmService = new FilmService();
    private ListFilm listTemp;
    ArrayList<FilmItem> searchResults = new ArrayList<>();
    ArrayList<FilmItem> originalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_film_actors);
        getSupportActionBar().hide();
        init();
        idActor = getIntent().getLongExtra("idActor",0);
        searchResultTxt.setText(getIntent().getStringExtra("nameActor").toString());
        getListFilm();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                performSearch(charSequence.toString());


            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    private void performSearch(String query) {
        searchResults.clear();
        for (FilmItem item : originalList) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(item);
            }
        }
        if(searchResults.size()==0){
            for (FilmItem item : originalList) {
                if (item.getPlot().toLowerCase().contains(query.toLowerCase())) {
                    searchResults.add(item);
                }
            }
        }
        if(searchResults.size()!=0){
            listTemp = new ListFilm(searchResults);
            adapterFilm = new SearchAdapter(listTemp);
            containerFilmActor.setAdapter(adapterFilm);
        }
    }
    private void getListFilm() {
        filmService.getFilmForActor(idActor, new FilmService.OnFilmDataReceivedListener() {
            @Override
            public void onFilmDataReceived(ArrayList<FilmItem> listFilms) {
                listTemp = new ListFilm(listFilms);
                originalList = listFilms;
                adapterFilm = new SearchAdapter(listTemp);
                containerFilmActor.setAdapter(adapterFilm);
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("My tag", errorMessage);
            }
        });
    }

    private void init() {
        containerFilmActor = findViewById(R.id.containerFilmSortedActor);
        containerFilmActor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        searchResultTxt = findViewById(R.id.searchResultTxt);
        search = findViewById(R.id.searchItem);
    }
}