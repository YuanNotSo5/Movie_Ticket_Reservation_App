package com.example.appbookticketmovie.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbookticketmovie.Adapter.ActorsListAdapter;
import com.example.appbookticketmovie.Adapter.CategoryListAdapter;
import com.example.appbookticketmovie.Adapter.FilmListAdapter;
import com.example.appbookticketmovie.Adapter.SliderAdapters;
import com.example.appbookticketmovie.HomeActivities.DetailActivity;
import com.example.appbookticketmovie.HomeActivities.SpecificList;
import com.example.appbookticketmovie.Models.ActorItem;
import com.example.appbookticketmovie.Models.FilmFrequent;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.ListFilm;
import com.example.appbookticketmovie.Domain.SliderItems;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.ActorService;
import com.example.appbookticketmovie.Services.FilmService;
import com.example.appbookticketmovie.Services.GenreService;
import com.example.appbookticketmovie.databinding.FragmentHomeBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView.Adapter adapterBestMovies, adapterUpComing, adaterCategory, adaterRecommend, adaterActor;
    private RecyclerView recyclerViewBestMovies, recyclerViewUpComing, recyclerViewCategory, recyclerViewRecommend, recyclerViewActor;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest, mStringRequest2, mStringRequest3;
    private ProgressBar loading1, loading2, loading3, loading4, loading5;

    private FragmentHomeBinding binding;
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    private EditText search;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //Slider
        viewPager2 = binding.viewpagerSlider;

        recyclerViewBestMovies = binding.recyclerView2;
        recyclerViewBestMovies.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        loading1 = binding.progressBar;

        //Recycler Category
        recyclerViewCategory = binding.recyclerViewCate;
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        loading2 = binding.progressBar2;

        //Recycler comming up
        recyclerViewUpComing = binding.recyclerViewCommingSoon;
        recyclerViewUpComing.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        loading3 = binding.progressBar3;

        //Recycler Actor
        recyclerViewActor = binding.recyclerViewActors;
        recyclerViewActor.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        loading4 = binding.progressBar4;

        //Recycler Recommend
        recyclerViewRecommend = binding.recyclerViewRecommended;
        recyclerViewRecommend.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        loading5 = binding.progressBar5;

        banners();
        sendRequest();
        sendRequestCategory();
        sendRequestUpComing();
        sendRequestActors();
        sendRequestRecomend();


        //Search
        search = binding.searchItemHome;

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), SpecificList.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void sendRequestRecomend() {
        loading5.setVisibility(View.VISIBLE);
        ArrayList <FilmItem> listRecommend = new ArrayList<>();
        FilmService filmService= new FilmService();

        filmService.getFilmRecommend(new FilmService.OnFilmFrequentDataReceivedListener() {
            @Override
            public void onSuccess(ArrayList<FilmFrequent> listFrequentFilm) {
                ArrayList <FilmFrequent> tmp = listFrequentFilm;
                Collections.sort(tmp, new Comparator<FilmFrequent>() {
                    @Override
                    public int compare(FilmFrequent film1, FilmFrequent film2) {
                        return Integer.compare(film2.getFrequency(), film1.getFrequency());
                    }
                });

                int bound = 10;
                if(tmp.size()<10) {
                    bound = tmp.size();
                }
                for (int i = 0; i < bound; i++) {
                    if(tmp.get(i).getFrequency()!=0){
                        listRecommend.add(tmp.get(i).getFilm());
                    }

                }

                loading5.setVisibility(View.GONE);
                ListFilm listFilm = new ListFilm(listRecommend);
                adaterRecommend = new FilmListAdapter(listFilm);
                recyclerViewRecommend.setAdapter((adaterRecommend));

            }

            @Override
            public void onError(String errorMessage) {
                loading5.setVisibility(View.GONE);
                Log.i("UiLover", "OnError"+errorMessage);
            }
        });
    }

    private void sendRequestActors() {
        loading4.setVisibility(View.VISIBLE);

        ActorService listActors = new ActorService();
        listActors.getActors(new ActorService.OnActorsDataReceivedListener() {
            @Override
            public void onDataReceived(ArrayList<ActorItem> listActors) {
                loading4.setVisibility(View.GONE);
                adaterActor = new ActorsListAdapter(listActors);
                recyclerViewActor.setAdapter((adaterActor));
            }

            @Override
            public void onError(String errorMessage) {
                loading4.setVisibility(View.GONE);
                Log.i("UiLover", "OnError"+errorMessage);
            }
        });
    }

    private void sendRequestUpComing() {
        loading3.setVisibility(View.VISIBLE);

        FilmService listFilm = new FilmService();
        listFilm.getFilmCommingSoonCloudStore(new FilmService.OnFilmDataReceivedListener() {
            @Override
            public void onFilmDataReceived(ArrayList<FilmItem> listFilms) {
                loading3.setVisibility(View.GONE);
                ListFilm items = new ListFilm(listFilms);
                adapterUpComing = new FilmListAdapter(items);
                recyclerViewUpComing.setAdapter((adapterUpComing));
            }
            @Override
            public void onError(String errorMessage) {
                loading3.setVisibility(View.GONE);
                Log.i("UiLover", "OnError"+errorMessage);
            }
        });
    }

//    private void sendRequest() {
//        mRequestQueue = Volley.newRequestQueue(requireContext());
//        loading1.setVisibility(View.VISIBLE);
//        mStringRequest=new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=1", new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Gson gson = new Gson();
//                loading1.setVisibility(View.GONE);
//                ListFilm items = gson.fromJson(response,ListFilm.class);
//                adapterBestMovies = new FilmListAdapter(items);
//                recyclerViewBestMovies.setAdapter((adapterBestMovies));
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                loading1.setVisibility(View.GONE);
//                Log.i("UiLover", "OnError"+error.toString());
//            }
//        });
//
//        mRequestQueue.add(mStringRequest);
//    }

    private void sendRequest() {
        mRequestQueue = Volley.newRequestQueue(requireContext());
        loading1.setVisibility(View.VISIBLE);

        FilmService listFilm = new FilmService();
        listFilm.getFilmCloudStore(new FilmService.OnFilmDataReceivedListener() {
            @Override
            public void onFilmDataReceived(ArrayList<FilmItem> listFilms) {
                loading1.setVisibility(View.GONE);
                ListFilm items = new ListFilm(listFilms);
                adapterBestMovies = new FilmListAdapter(items);
                recyclerViewBestMovies.setAdapter((adapterBestMovies));
            }
            @Override
            public void onError(String errorMessage) {
                loading1.setVisibility(View.GONE);
                Log.i("UiLover", "OnError"+errorMessage);
            }
        });
    }

    //Get Category
//    private void sendRequestCategory() {
//        mRequestQueue = Volley.newRequestQueue(requireContext());
//        loading2.setVisibility(View.VISIBLE);
//
//
//        GenreService genreService = new GenreService();
//        genreService.getGenre(new GenreService.OnGenreDataReceivedListener() {
//            @Override
//            public void onGenreDataReceived(ArrayList<GenreItem> listGenre) {
//                Log.d("My Tag", String.valueOf(listGenre));
//                adaterCategory = new CategoryListAdapter(listGenre);
//                recyclerViewCategory.setAdapter((adaterCategory));
//
//            }
//        });

    //        mStringRequest2=new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/genres", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Gson gson = new Gson();
//                loading2.setVisibility(View.GONE);
//                ArrayList<GenreItem> catList = gson.fromJson(response,new TypeToken<ArrayList<GenreItem>>() {}.getType());
//                adaterCategory = new CategoryListAdapter(catList);
//                recyclerViewCategory.setAdapter((adaterCategory));
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                loading2.setVisibility(View.GONE);
//                Log.i("UiLover", "OnError"+error.toString());
//            }
//        });
//        mRequestQueue.add(mStringRequest2);
//    }
    private void sendRequestCategory() {
        mRequestQueue = Volley.newRequestQueue(requireContext());
        loading2.setVisibility(View.VISIBLE);

        GenreService genreService = new GenreService();
        genreService.getGenreCloudStore(new GenreService.OnGenreDataReceivedListener() {
            @Override
            public void onGenreDataReceived(ArrayList<GenreItem> listGenre) {
                adaterCategory = new CategoryListAdapter(listGenre);
                recyclerViewCategory.setAdapter((adaterCategory));
                loading2.setVisibility(View.GONE);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("My Tag", errorMessage);
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Get Banner Slider
    private void banners(){
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.wide1));
        sliderItems.add(new SliderItems(R.drawable.wide));
        sliderItems.add(new SliderItems(R.drawable.wide3));

        viewPager2.setAdapter(new SliderAdapters(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
            }
        });
    }


    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };
    @Override
    public void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}