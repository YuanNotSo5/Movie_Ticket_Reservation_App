package com.example.appbookticketmovie.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appbookticketmovie.Models.ActorItem;
import com.example.appbookticketmovie.Models.CommentItem;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.Ticket;
import com.example.appbookticketmovie.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class UserService {
    FirebaseFirestore db;
    public UserService() {
        db = FirebaseFirestore.getInstance();
    }

    public interface OnCmtDataReceivedListener {
        void onCmtDataReceived(CommentItem newCmt);
        void onError(String errorMessage);
    }

    public interface CmtListListener {
        void onCmtDataReceived(ArrayList<CommentItem> listCommentsOfFilm);
        void onError(String errorMessage);
    }

    public interface userInfoListener {
        void onUserDataReceived(User userInfo);
        void onError(String errorMessage);
    }

    public interface AddTicketInfoListener {
        void onSuccess();

        void onError(String errorMessage);
    }

    public void postComment(String userName, String avatar, String cmt, long idUser, long idFilm, String rate, UserService.OnCmtDataReceivedListener listener){
        // Get current timestamp
        Timestamp timestamp = Timestamp.now();

        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("comment", cmt);
        data.put("date", timestamp);
        data.put("idFilm", idFilm);
        data.put("idUser", idUser);
        data.put("rate", rate);

        db.collection("Rate")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TEST", "DocumentSnapshot written with ID: " + documentReference.getId());
                        if (listener != null) {
                            CommentItem cmtNew = new CommentItem(userName, formatDate(timestamp), cmt, rate, avatar);
                            listener.onCmtDataReceived(cmtNew);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TEST", "Error adding document", e);
                        if (listener != null) {
                            listener.onError(String.valueOf(e));
                        }
                    }
                });
    }

    //Get all comment of a film
    public void LoadAllCmtOfFilm(long idFilm, CmtListListener listener) {
        ArrayList<CommentItem> listCmt = new ArrayList<>();
        db.collection("Rate")
                .whereEqualTo("idFilm", idFilm)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listCmt.clear();
                                String comment = document.getString("comment");
                                Timestamp timestamp = document.getTimestamp("date");
                                String date = formatDate(timestamp);

                                long idFilm = document.getLong("idFilm");
                                long idUser = document.getLong("idUser");
                                String rate = document.getString("rate");

                                CommentItem newCmt = new CommentItem("", date, comment, rate, "");

                                getUser(idUser, new userInfoListener() {
                                    @Override
                                    public void onUserDataReceived(User userInfo) {
                                        newCmt.setPhoto(userInfo.getAvatar());
                                        newCmt.setUserName(userInfo.getUsername());
                                        listCmt.add(newCmt);

                                        if (listCmt.size() == task.getResult().size()) {
                                            listener.onCmtDataReceived(listCmt);
                                        }
                                    }

                                    @Override
                                    public void onError(String errorMessage) {
                                        // Xử lý lỗi
                                    }
                                });
                            }
                        } else {
                            if (listener != null) {
                                listener.onError("Error loading data from Firestore" +  task.getException());
                            }
                        }
                    }
                });
    }

    // Phương thức để chuyển đổi Timestamp thành chuỗi ngày tháng năm
    private String formatDate(Timestamp timestamp) {
        if (timestamp != null) {
            Date date = timestamp.toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.format(date);
        }
        return "";
    }


    //Get info user by id
    public void getUser (long idUser, userInfoListener callback){
        db.collection("Users")
                .whereEqualTo("id", idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                User userInfo = document.toObject(User.class);
                                callback.onUserDataReceived(userInfo);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                            callback.onError(String.valueOf(task.getException()));
                        }
                    }
                });
    }

    //Buy Ticket
    public void buyTicket(ArrayList<Ticket> tickets, long idUser) {
        // Buy Ticket

        for (Ticket item : tickets) {
            Map<String, Object> data = new HashMap<>();
            data.put("idFilm", item.getIdFilm());
            data.put("idUser", idUser);
            data.put("seat", item.getNumberSeat());
            data.put("barcode", item.getBarcode());
            data.put("date", item.getDate());
            data.put("idCinema", item.getCinema());
            data.put("price", item.getPriceDetail());
            data.put("room", item.getRoom());
            data.put("time", item.getTime());

            db.collection("User_Ticket")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("TEST", "DocumentSnapshot written with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TEST", "Error adding document", e);
                        }
                    });
        }
    }



}
