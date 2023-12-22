package com.example.appbookticketmovie.Services;

import androidx.annotation.NonNull;

import com.example.appbookticketmovie.Models.ActorItem;
import com.example.appbookticketmovie.Models.GenreItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ActorService {
    FirebaseFirestore db;

    public ActorService() {
        db = FirebaseFirestore.getInstance();
    }

    public interface OnActorsDataReceivedListener {
        void onDataReceived(ArrayList<ActorItem> listActors);
        void onError(String errorMessage);
    }
    public interface OnActorItemCallback {
        void onSuccess(ActorItem actorItem);
        void onFailure(Exception e);
    }
    //Get All Actor In An Movie
    public void getSpecificActor(Long idActor, ActorService.OnActorItemCallback callback) {
        DocumentReference docRef = db.collection("Actors").document(String.valueOf(idActor));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String name = document.getString("name");
                        long id = document.getLong("id");
                        String photoUrl = document.getString("photo");
                        ActorItem actorItem = new ActorItem(id, name, photoUrl);
                        callback.onSuccess(actorItem);
                    } else {
                        callback.onFailure(new Exception("No such document"));
                    }
                } else {
                    callback.onFailure(task.getException());
                }
            }
        });
    }

    public void getActorsForFilm(Integer idFilm, ActorService.OnActorsDataReceivedListener listener) {
        ArrayList<ActorItem> actorsForFilms = new ArrayList<>();
        db.collection("Film_Actor")
                .whereEqualTo("idFilm", idFilm)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Long idActor = document.getLong("idActor");
                            getSpecificActor(idActor, new ActorService.OnActorItemCallback() {
                                @Override
                                public void onSuccess(ActorItem actorItem) {
                                    actorsForFilms.add(actorItem);
                                    if (actorsForFilms.size() == task.getResult().size()) {
                                        listener.onDataReceived(actorsForFilms);
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
