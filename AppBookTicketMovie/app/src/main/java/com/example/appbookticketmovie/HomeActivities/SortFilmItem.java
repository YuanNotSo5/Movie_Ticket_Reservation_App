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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appbookticketmovie.Adapter.FilmListAdapter;
import com.example.appbookticketmovie.Adapter.SearchAdapter;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.Models.ListFilm;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.FilmService;

import java.util.ArrayList;

public class SortFilmItem extends AppCompatActivity {

    ImageView backImg;
    RecyclerView containerFilm;
    EditText search;
    TextView searchResultTxt;
    private RecyclerView.Adapter adapterFilm;
    private long idCategory;

    private FilmService filmService = new FilmService();
    private ListFilm listTemp;
    ArrayList<FilmItem> searchResults = new ArrayList<>();
    ArrayList<FilmItem> originalList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_film_item);
        getSupportActionBar().hide();
        init();
        idCategory = getIntent().getLongExtra("id",0);
        searchResultTxt.setText(getIntent().getStringExtra("name").toString());
        getListFilm();

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            containerFilm.setAdapter(adapterFilm);
        }
    }
    private void getListFilm() {
        filmService.getFilmForGenre(idCategory, new FilmService.OnFilmDataReceivedListener() {
            @Override
            public void onFilmDataReceived(ArrayList<FilmItem> listFilms) {
                listTemp = new ListFilm(listFilms);
                originalList = listFilms;
                adapterFilm = new SearchAdapter(listTemp);
                containerFilm.setAdapter(adapterFilm);
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("My tag", errorMessage);
            }
        });
    }

    private void init() {
        containerFilm = findViewById(R.id.containerFilmSorted);
        containerFilm.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        searchResultTxt = findViewById(R.id.searchResultTxt);
        search = findViewById(R.id.searchItem);
        backImg = findViewById(R.id.backImg);

    }
}