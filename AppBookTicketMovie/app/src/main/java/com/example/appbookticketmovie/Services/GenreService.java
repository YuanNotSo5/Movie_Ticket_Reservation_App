package com.example.appbookticketmovie.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appbookticketmovie.Models.GenreItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GenreService {
    //https://www.youtube.com/watch?v=0ofkvm97i0s
    private DatabaseReference mDatabase;
    FirebaseFirestore db;
    ArrayList<GenreItem> listGenresForFilm;

    public interface OnGenreDataReceivedListener {
        void onGenreDataReceived(ArrayList<GenreItem> listGenre);
        void onError(String errorMessage);
    }

    public GenreService() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
    }

    //Get All Genres
    public void getGenre(OnGenreDataReceivedListener listener) {
        ArrayList<GenreItem> genreList = new ArrayList<>();
        mDatabase.child("Genres").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    if (listener != null) {
                        listener.onError("Error loading data from Firebase");
                    }
                } else {
                    for (DataSnapshot genreSnapshot : task.getResult().getChildren()) {
                        GenreItem genre = genreSnapshot.getValue(GenreItem.class);
                        genreList.add(genre);
                    }
                    if (listener != null) {
                        listener.onGenreDataReceived(genreList);
                    }
                }
            }
        });
    }
    public void getGenreCloudStore(OnGenreDataReceivedListener listener) {
        ArrayList<GenreItem> genreList = new ArrayList<>();
        db.collection("Genres")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            long id = document.getLong("id");
                            GenreItem genre = new GenreItem(name,id);
                            genreList.add(genre);
                        }
                        if (listener != null) {
                            listener.onGenreDataReceived(genreList);
                        }
                    } else {
                        if (listener != null) {
                            listener.onError("Error loading data from Firestore");
                        }
                    }
                }
            });
    }

    //Get Genre Of Film
    public interface OnGenreItemCallback {
        void onSuccess(GenreItem genreItem);
        void onFailure(Exception e);
    }

    //Async Task: https://www.youtube.com/watch?v=uKx0FuVriqA
    public void getSpecificGenre(Long idGenre, OnGenreItemCallback callback) {
        DocumentReference docRef = db.collection("Genres").document(String.valueOf(idGenre));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String name = document.getString("name");
                        long id = document.getLong("id");
                        GenreItem genreItem = new GenreItem(name, id);
                        callback.onSuccess(genreItem);
                    } else {
                        callback.onFailure(new Exception("No such document"));
                    }
                } else {
                    callback.onFailure(task.getException());
                }
            }
        });
    }

//    public void getGenresForFilm(int filmId, OnGenreDataReceivedListener listener) {
//        ArrayList<GenreItem> listGenre = new ArrayList<>();
//
//        // Tạo một List chứa tất cả các Task<DocumentSnapshot>
//        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
//
//        // Truy vấn Film_Genre để lấy danh sách idGenre của một bộ phim
//        db.collection("Film_Genre")
//                .whereEqualTo("idFilm", filmId)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    // Lặp qua kết quả của truy vấn Film_Genre
//                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                        Long idGenre = document.getLong("idGenre");
//
//                        // Thực hiện truy vấn Genres và thêm Task vào danh sách tasks
//                        Task<DocumentSnapshot> genreTask = db.collection("Genres")
//                                .document(String.valueOf(idGenre))
//                                .get();
//                        tasks.add(genreTask);
//                    }
//
//                    // Sử dụng Tasks.whenAllSuccess để đợi tất cả các Task hoàn tất
//                    Tasks.whenAllSuccess(tasks)
//                            .addOnSuccessListener(documentSnapshots -> {
//                                // Xử lý kết quả của từng truy vấn Genres
//                                for (Object result : documentSnapshots) {
//                                    DocumentSnapshot documentSnapshot = (DocumentSnapshot) result;
//
//                                    String genreName = documentSnapshot.getString("name");
//                                    Long id = documentSnapshot.getLong("id");
//                                    listGenre.add(new GenreItem(genreName, id));
//                                }
//
//                                // Gọi listener khi tất cả truy vấn đã hoàn tất
//                                if (listener != null) {
//                                    listener.onGenreDataReceived(listGenre);
//                                }
//                            })
//                            .addOnFailureListener(e -> {
//                                // Xử lý lỗi nếu có
//                                if (listener != null) {
//                                    listener.onError("Error loading data from Firebase");
//                                }
//                            });
//                })
//                .addOnFailureListener(e -> {
//                    // Xử lý lỗi nếu có
//                    if (listener != null) {
//                        listener.onError("Error loading data from Firebase");
//                    }
//                });
//    }

    public void getGenresForFilm(Integer idFilm, OnGenreDataReceivedListener listener) {
        ArrayList<GenreItem> genresForFilm = new ArrayList<>();
        db.collection("Film_Genre")
                .whereEqualTo("idFilm", idFilm)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Long idGenre = document.getLong("idGenre");
                            getSpecificGenre(idGenre, new OnGenreItemCallback() {
                                @Override
                                public void onSuccess(GenreItem genreItem) {
                                    genresForFilm.add(genreItem);
                                    if (genresForFilm.size() == task.getResult().size()) {
                                        listener.onGenreDataReceived(genresForFilm);
                                    }
                                }
                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                        }
                    } else {
                        listener.onError("Error loading Film_Genre data");
                    }
                });
    }




}