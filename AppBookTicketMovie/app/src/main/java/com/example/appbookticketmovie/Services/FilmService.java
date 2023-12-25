package com.example.appbookticketmovie.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appbookticketmovie.Models.ActorItem;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.ListFilm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FilmService {
    FirebaseFirestore db;

    public interface OnFilmDataReceivedListener {
        void onFilmDataReceived(ArrayList<FilmItem> listFilms);
        void onError(String errorMessage);
    }

    public interface OnFilmDataReceivedListenerSearch {
        void onFilmDataReceived(ArrayList<ListFilm> listFilms);
        void onError(String errorMessage);
    }


    public FilmService() {
        db = FirebaseFirestore.getInstance();
    }

    //Get all films
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

    //Get film by Id or search films by name
    public void getFilmsByName(String searchText, FilmService.OnFilmDataReceivedListener listener) {
        ArrayList<FilmItem> filmList = new ArrayList<>();
        db.collection("Films")
                .whereGreaterThanOrEqualTo("title", searchText)
                .whereLessThanOrEqualTo("title", searchText + '\uf8ff')
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
                                                // Handle error
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(String errorMessage) {
                                        // Handle error
                                    }
                                });
                            }
                        } else {
                            if (listener != null) {
                                listener.onError("Error loading data from Firestore");
                            }
                        }
                    }
                });
    }


}
