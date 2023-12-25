package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appbookticketmovie.Adapter.FilmListAdapter;
import com.example.appbookticketmovie.Adapter.SearchAdapter;
import com.example.appbookticketmovie.Models.ActorItem;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.ListFilm;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.FilmService;

import java.util.ArrayList;

public class SpecificList extends AppCompatActivity {

    EditText search;
    TextView cancelResult;
    ArrayList<FilmItem> searchResults = new ArrayList<>();

    private RecyclerView.Adapter adapterSearch;
    private RecyclerView recyclerViewResult;
    ListFilm items;

    private FilmService filmService = new FilmService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_list);
        getSupportActionBar().hide();
        init();

        ArrayList<FilmItem> filmItemList = new ArrayList<>();

        FilmItem film1 = new FilmItem();
        film1.setId(1);
        film1.setTitle("Inception");
        film1.setPoster("inception_poster.jpg");
        film1.setReleased("2022-01-01");
        film1.setRuntime("148 min");
        film1.setDirector("Christopher Nolan");

        // Add actors
        ArrayList<ActorItem> actors1 = new ArrayList<>();
        actors1.add(new ActorItem(1L, "Leonardo DiCaprio", "Cobb"));
        actors1.add(new ActorItem(1L, "Leonardo DiCaprio", "Cobb"));
        film1.setActors(actors1);

        film1.setPlot("A thief who enters the dreams of others to steal their secrets.");

        film1.setCountry("USA");
        film1.setImdbRating("8.8");

        // Add genres
        ArrayList<GenreItem> genres1 = new ArrayList<>();
        genres1.add(new GenreItem("Action",1L));

        film1.setGenres(genres1);

        film1.setTrailer("https://www.youtube.com/watch?v=YoHD9XEInc0");

        filmItemList.add(film1);

//        items = new ListFilm(filmItemList);


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

        cancelResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void performSearch(final String searchString) {

        Log.d("Eyyyy",searchString);
        filmService.getFilmsByName(searchString, new FilmService.OnFilmDataReceivedListener() {
            @Override
            public void onFilmDataReceived(final ArrayList<FilmItem> listFilms) {
                searchResults.clear();
                searchResults.addAll(listFilms);
                items = new ListFilm(searchResults);
                adapterSearch = new SearchAdapter(items);
                recyclerViewResult.setAdapter(adapterSearch);
                adapterSearch.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("Error", errorMessage);
            }
        });
    }


    public void init(){
        recyclerViewResult = findViewById(R.id.searchResultContainer);
        recyclerViewResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        search = findViewById(R.id.searchItem);
        cancelResult = findViewById(R.id.cancelItem);
    }
}