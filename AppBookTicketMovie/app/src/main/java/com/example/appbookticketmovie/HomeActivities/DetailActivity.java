package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.appbookticketmovie.Adapter.ActorsListAdapter;
import com.example.appbookticketmovie.Adapter.CategoryEachFilmListAdapter;
import com.example.appbookticketmovie.MainActivity;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.FilmService;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar progressBar;
    private TextView titleTxt, movieRateTxt, movieTimeTxt, movieSumInfo, movieActorInfo;
    private int idFilm;
    private ImageView pic2, backImg;
    private RecyclerView.Adapter adapterActorList, adapterCategory;
    private RecyclerView recyclerViewActors, recyclerViewCategory;
    private NestedScrollView scrollView;
    private WebView trailerContainer;

    private Button bookTicketBtn, shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();

        idFilm = getIntent().getIntExtra("id",0);
        initView();
        sendRequest();

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailActivity.this, "Haha", Toast.LENGTH_SHORT).show();
                shareApp(DetailActivity.this);
            }
        });

    }

    public static void shareApp(Context context)
    {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    private void sendRequest(){
        FilmService film = new FilmService();
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        film.getFilmById(idFilm, new FilmService.OnFilmDataReceivedListener() {
            @Override
            public void onFilmDataReceived(ArrayList<FilmItem> listFilms) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                FilmItem item = listFilms.get(0);
                Glide.with(DetailActivity.this)
                        .load(item.getPoster())
                        .into(pic2);
                titleTxt.setText(item.getTitle());
                movieRateTxt.setText(item.getImdbRating());
                movieTimeTxt.setText(item.getRuntime());
                movieSumInfo.setText(item.getPlot());

                String videoLink = item.getTrailer();
                String video =
                       "<iframe width=\"100%\" " +
                               "height=\"100%\" " +
                               "src=\""+videoLink+"\" " +
                               "title=\"YouTube video player\" frameborder=\"0\" " +
                               "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";

                trailerContainer.loadData(video, "text/html", "utf-8");
                trailerContainer.getSettings().setJavaScriptEnabled(true);
                trailerContainer.getSettings().setLoadWithOverviewMode(true);
                trailerContainer.getSettings().setUseWideViewPort(true);
                trailerContainer.getSettings().setDomStorageEnabled(true);

                trailerContainer.loadData(video, "text/html", "utf-8");
                trailerContainer.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                        Log.e("WebView", "Error: " + error.getDescription());
                    }
                });

                if(item.getActors()!=null){
                    adapterActorList=new ActorsListAdapter(item.getActors());
                    recyclerViewActors.setAdapter(adapterActorList);
                }
                if(item.getGenres()!=null){
                    adapterCategory=new CategoryEachFilmListAdapter(item.getGenres());
                    recyclerViewCategory.setAdapter(adapterCategory);
                }
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

//    private void sendRequest(){
//        mRequestQueue = Volley.newRequestQueue(this);
//        progressBar.setVisibility(View.VISIBLE);
//        scrollView.setVisibility(View.GONE);
//        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/m-4YnwBwaGk?si=1YOO6k29uzkk4wSZ\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
//        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies/" + idFilm, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Gson gson = new Gson();
//                progressBar.setVisibility(View.GONE);
//                scrollView.setVisibility(View.VISIBLE);
//                FilmItem item = gson.fromJson(response, FilmItem.class);
//
//                Glide.with(DetailActivity.this)
//                        .load(item.getPoster())
//                        .into(pic2);
//                titleTxt.setText(item.getTitle());
//                movieRateTxt.setText(item.getImdbRating());
//                movieTimeTxt.setText(item.getRuntime());
//                movieSumInfo.setText(item.getPlot());
////                movieActorInfo.setText(item.getActors());
//                //Set trailer
//                trailerContainer.loadData(video, "text/html","uft-8");
//                trailerContainer.getSettings().setJavaScriptEnabled(true);
//                trailerContainer.setWebChromeClient(new WebChromeClient());
//
//
//                if(item.getImages()!=null){
//                    adapterActorList=new ActorsListAdapter(item.getImages());
//                    recyclerViewActors.setAdapter(adapterActorList);
//                }
//                if(item.getGenres()!=null){
//                    adapterCategory=new CategoryEachFilmListAdapter(item.getGenres());
//                    recyclerViewCategory.setAdapter(adapterCategory);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//        mRequestQueue.add(mStringRequest);
//    }

    private void initView(){
        titleTxt = findViewById(R.id.movieNameTxt);
        progressBar = findViewById(R.id.progressBarDetail);
        scrollView = findViewById(R.id.scrollView2);
        pic2 = findViewById(R.id.detailPic);
        movieRateTxt = findViewById(R.id.movieStar);
        movieTimeTxt = findViewById(R.id.movieTime);
        movieSumInfo = findViewById(R.id.movieSummaryContextTxt);
        backImg = findViewById(R.id.backImg);
        recyclerViewCategory = findViewById(R.id.genreView);
        recyclerViewActors = findViewById(R.id.imagesRecycler);
        recyclerViewActors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Btn
        bookTicketBtn = findViewById(R.id.bookTicketBtn);
        shareBtn = findViewById(R.id.sharingBtn);

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent backIntent = new Intent(DetailActivity.this, MainActivity.class);
//                startActivity(backIntent);
            }
        });
        trailerContainer = findViewById(R.id.trailerContainer);
    }
    //https://fireship.io/lessons/firestore-nosql-data-modeling-by-example/
}