package com.example.appbookticketmovie.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appbookticketmovie.Models.Cinema;
import com.example.appbookticketmovie.Models.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CinemaService {
    private FirebaseFirestore db;
    private CollectionReference cinemaDB;

    public CinemaService() {
        this.db = FirebaseFirestore.getInstance();
        this.cinemaDB = db.collection("Cinema");
    }

    public interface getCinemas {
        void getCinemas(ArrayList<Cinema> cinemas);
        void onFailure(Exception e);
    }

    public interface OnCinemaDataReceivedListener2 {
        void onCinemaDataReceived(Room room);
        void onError(String errorMessage);
    }
    public void getAllCinema (CinemaService.getCinemas callback){
        ArrayList<Cinema> cinemas = new ArrayList<>();
        cinemaDB.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Long id = document.getLong("id");
                            String name = document.getString("name");
                            String address = document.getString("address");

                            Cinema cinema = new Cinema(id, name, address);
                            cinemas.add(cinema);

                            if (cinemas.size() == task.getResult().size()) {
                                callback.getCinemas(cinemas);
                            }
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void getMapRoom(String idRoom, int idCinema, CinemaService.OnCinemaDataReceivedListener2 listener){
        DocumentReference roomDocRef = db.collection("Cinema")
                .document(String.valueOf(idCinema))
                .collection("room")
                .document(idRoom);

        roomDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Room cinemaRoom = document.toObject(Room.class);

                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        if (listener!=null) {
                            listener.onCinemaDataReceived(cinemaRoom);
                        }
                    } else {
                        if (listener!=null) {
                            listener.onError("No such document");
                        }
                    }
                } else {
                    if (listener!=null) {
                        listener.onError(String.valueOf(task.getException()));
                    }
                }
            }
        });
    }

    public void updateSeatMap(String newMap, int idCinema, String idRoom, CinemaService.OnCinemaDataReceivedListener2 listener){
        DocumentReference messageRef = db
                .collection("Cinema").document(String.valueOf(idCinema))
                .collection("room").document(idRoom);

        messageRef
                .update("map", newMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully updated!");
                        if (listener!=null) {
                            listener.onCinemaDataReceived(new Room("",0,0,""));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating document", e);
                        if (listener!=null) {
                            listener.onError(String.valueOf(e));
                        }
                    }
                });
    }


}
