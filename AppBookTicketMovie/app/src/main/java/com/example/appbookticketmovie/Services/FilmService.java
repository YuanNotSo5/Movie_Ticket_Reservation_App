package com.example.appbookticketmovie.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appbookticketmovie.Models.ActorItem;
import com.example.appbookticketmovie.Models.FilmFrequent;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.ListFilm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FilmService {
    FirebaseFirestore db;

    public interface OnFilmDataReceivedListener {
        void onFilmDataReceived(ArrayList<FilmItem> listFilms);
        void onError(String errorMessage);
    }




    public FilmService() {
        db = FirebaseFirestore.getInstance();
    }

    //Get all films is showing
    public void getFilmCloudStore(FilmService.OnFilmDataReceivedListener listener) {
        ArrayList<FilmItem> filmList = new ArrayList<>();
        db.collection("Films")
                .whereEqualTo("isShow", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot filmDoc : task.getResult()) {
                                boolean isShow = filmDoc.getBoolean("isShow");
                                FilmItem film = filmDoc.toObject(FilmItem.class);
                                film.setShow(filmDoc.getBoolean("isShow"));

                                //Get Actor and Cate
                                ActorService actors = new ActorService();
                                GenreService genres = new GenreService();

                                actors.getActorsForFilm(film.getId(), new ActorService.OnActorsDataReceivedListener() {
                                    @Override
                                    public void onDataReceived(ArrayList<ActorItem> listActors) {
                                        film.setActors(listActors);
                                        genres.getGenresForFilm(film.getId(), new GenreService.OnGenreDataReceivedListener() {
                                            @Override
                                            public void onGenreDataReceived(ArrayList<GenreItem> listGenre) {
                                                film.setGenres(listGenre);
                                                Log.d("Item", String.valueOf(film));
                                                filmList.add(film);
                                                if (filmList.size() == task.getResult().size()) {
                                                    listener.onFilmDataReceived(filmList);
                                                }
                                            }
                                            @Override
                                            public void onError(String errorMessage) {

                                            }
                                        });

                                    }
                                    @Override
                                    public void onError(String errorMessage) {

                                    }
                                });

                            }
                        } else {
                            Log.d("TAG", "Error getting films: ", task.getException());
                            if (listener != null) {
                                listener.onError("Error loading data from Firestore");
                            }
                        }
                    }
                });
    }

    //Get all films showing next month
    public void getFilmCommingSoonCloudStore(FilmService.OnFilmDataReceivedListener listener) {
        ArrayList<FilmItem> filmList = new ArrayList<>();
        db.collection("Films")
                .whereEqualTo("isShow", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot filmDoc : task.getResult()) {
                                FilmItem film = filmDoc.toObject(FilmItem.class);
                                //Get Actor and Cate
                                ActorService actors = new ActorService();
                                GenreService genres = new GenreService();

                                actors.getActorsForFilm(film.getId(), new ActorService.OnActorsDataReceivedListener() {
                                    @Override
                                    public void onDataReceived(ArrayList<ActorItem> listActors) {
                                        film.setActors(listActors);
                                        genres.getGenresForFilm(film.getId(), new GenreService.OnGenreDataReceivedListener() {
                                            @Override
                                            public void onGenreDataReceived(ArrayList<GenreItem> listGenre) {
                                                film.setGenres(listGenre);
                                                Log.d("Item", String.valueOf(film));
                                                filmList.add(film);
                                                if (filmList.size() == task.getResult().size()) {
                                                    listener.onFilmDataReceived(filmList);
                                                }
                                            }
                                            @Override
                                            public void onError(String errorMessage) {

                                            }
                                        });

                                    }
                                    @Override
                                    public void onError(String errorMessage) {

                                    }
                                });

                            }
                        } else {
                            Log.d("TAG", "Error getting films: ", task.getException());
                            if (listener != null) {
                                listener.onError("Error loading data from Firestore");
                            }
                        }
                    }
                });
    }

    //Get film by Id
    public void getFilmById(long idFilm, FilmService.OnFilmDataReceivedListener listener) {
        ArrayList<FilmItem> filmList = new ArrayList<>();
        db.collection("Films")
                .whereEqualTo("id", idFilm)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot filmDoc : task.getResult()) {
                                FilmItem film = filmDoc.toObject(FilmItem.class);
                                //Get Actor and Cate
                                ActorService actors = new ActorService();
                                GenreService genres = new GenreService();

                                actors.getActorsForFilm(film.getId(), new ActorService.OnActorsDataReceivedListener() {
                                    @Override
                                    public void onDataReceived(ArrayList<ActorItem> listActors) {
                                        film.setActors(listActors);

                                        genres.getGenresForFilm(film.getId(), new GenreService.OnGenreDataReceivedListener() {
                                            @Override
                                            public void onGenreDataReceived(ArrayList<GenreItem> listGenre) {
                                                film.setGenres(listGenre);
                                                filmList.add(film);
                                                if (filmList.size() == task.getResult().size()) {
                                                    listener.onFilmDataReceived(filmList);
                                                }
                                            }
                                            @Override
                                            public void onError(String errorMessage) {

                                            }
                                        });

                                    }
                                    @Override
                                    public void onError(String errorMessage) {

                                    }
                                });
                            }
                        } else {
                            Log.d("TAG", "Error getting films: ", task.getException());
                            if (listener != null) {
                                listener.onError("Error loading data from Firestore");
                            }
                        }
                    }
                });
    }



    //Get Film For Genre
    public void getFilmForGenre(long idGenre, OnFilmDataReceivedListener listener) {
        ArrayList<FilmItem> listFilmFollowCate = new ArrayList<>();
        db.collection("Film_Genre")
                .whereEqualTo("idGenre", idGenre)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Long idFilm = document.getLong("idFilm");
                            getFilmById(idFilm, new OnFilmDataReceivedListener() {
                                @Override
                                public void onFilmDataReceived(ArrayList<FilmItem> listFilms) {
                                    listFilmFollowCate.add(listFilms.get(0));
                                    if (listFilmFollowCate.size() == task.getResult().size()) {
                                        listener.onFilmDataReceived(listFilmFollowCate);
                                    }
                                }

                                @Override
                                public void onError(String errorMessage) {

                                }
                            });
                        }
                    } else {
                        listener.onError("Error loading Film_Genre data");
                    }
                });
    }

    //Get Films For Actors
    public void getFilmForActor(long idActor, OnFilmDataReceivedListener listener) {
        ArrayList<FilmItem> listFilmFollowCate = new ArrayList<>();
        db.collection("Film_Actor")
                .whereEqualTo("idActor", idActor)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Long idFilm = document.getLong("idFilm");
                            getFilmById(idFilm, new OnFilmDataReceivedListener() {
                                @Override
                                public void onFilmDataReceived(ArrayList<FilmItem> listFilms) {
                                    listFilmFollowCate.add(listFilms.get(0));
                                    if (listFilmFollowCate.size() == task.getResult().size()) {
                                        listener.onFilmDataReceived(listFilmFollowCate);
                                    }
                                }

                                @Override
                                public void onError(String errorMessage) {

                                }
                            });
                        }
                    } else {
                        listener.onError("Error loading Film_Genre data");
                    }
                });
    }
    public interface OnFilmDataReceivedListenerSearch {
        void onSuccess (int frequent);
        void onError(String errorMessage);
    }
    public void countFrequence (long idFilm, OnFilmDataReceivedListenerSearch listener) {
        Query query = db.collection("FavoriteFilm").whereEqualTo("idFilm", idFilm).whereEqualTo("isFavorite", true);
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    AggregateQuerySnapshot snapshot = task.getResult();
                    if (listener != null) {
                        listener.onSuccess((int) snapshot.getCount());
                    }
                } else {
                    if (listener != null) {
                        listener.onError(String.valueOf(task.getException()));
                    }
                }
            }
        });

    }

    public interface OnFilmFrequentDataReceivedListener {
        void onSuccess (ArrayList<FilmFrequent> listFrequentFilm);
        void onError(String errorMessage);
    }

    //Get list film recommendation
    public void getFilmRecommend(FilmService.OnFilmFrequentDataReceivedListener listener) {
        ArrayList<FilmFrequent> frequenetFilmList = new ArrayList<>();
        db.collection("Films")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot filmDoc : task.getResult()) {

                                FilmItem film = filmDoc.toObject(FilmItem.class);
                                film.setShow(filmDoc.getBoolean("isShow"));

                                //Get Actor and Cate
                                ActorService actors = new ActorService();
                                GenreService genres = new GenreService();

                                actors.getActorsForFilm(film.getId(), new ActorService.OnActorsDataReceivedListener() {
                                    @Override
                                    public void onDataReceived(ArrayList<ActorItem> listActors) {
                                        film.setActors(listActors);
                                        genres.getGenresForFilm(film.getId(), new GenreService.OnGenreDataReceivedListener() {
                                            @Override
                                            public void onGenreDataReceived(ArrayList<GenreItem> listGenre) {
                                                film.setGenres(listGenre);
                                                countFrequence(film.getId(), new OnFilmDataReceivedListenerSearch() {
                                                    @Override
                                                    public void onSuccess(int frequent) {


                                                        frequenetFilmList.add(new FilmFrequent(film, frequent));


                                                        if (frequenetFilmList.size() == task.getResult().size()) {
                                                            listener.onSuccess(frequenetFilmList);
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(String errorMessage) {

                                                    }
                                                });


                                            }
                                            @Override
                                            public void onError(String errorMessage) {

                                            }
                                        });

                                    }
                                    @Override
                                    public void onError(String errorMessage) {

                                    }
                                });

                            }
                        } else {
                            Log.d("TAG", "Error getting films: ", task.getException());
                            if (listener != null) {
                                listener.onError("Error loading data from Firestore");
                            }
                        }
                    }
                });
    }

}