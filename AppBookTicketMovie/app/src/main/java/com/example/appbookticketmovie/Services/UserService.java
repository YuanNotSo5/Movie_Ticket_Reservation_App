package com.example.appbookticketmovie.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appbookticketmovie.Models.ActorItem;
import com.example.appbookticketmovie.Models.Bill;
import com.example.appbookticketmovie.Models.CommentItem;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.ListBill;
import com.example.appbookticketmovie.Models.Schedule;
import com.example.appbookticketmovie.Models.Ticket;
import com.example.appbookticketmovie.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
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

public class UserService {
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private CollectionReference userDB;

    public UserService() {
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userDB = db.collection("Users");
    }

    public interface getUser {
        void getUser(User user);
        void onError(String errorMessage);
        void onFailure(Exception e);
    }
    public interface getUserWithId {
        void getUser(User user);
        void onError(String errorMessage);
        void onFailure(Exception e);
    }

    public interface editUserInfo{
        void updateUser(boolean status);
        void onFailure(Exception e);
    }
    public interface passwordUpdate{
        void updatePassword(boolean status);
        void onFailure(Exception e);
    }
    public interface pointUpdate{
        void updatePoint(boolean status);
        void onFailure(Exception e);
    }

    public interface cardUpdate{
        void updateCard(boolean status);
        void onFailure(Exception e);
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

    public interface checkEmailExisted {
        void onSuccess(boolean status);
        void onFailure(Exception e);
    }


    public void getUserByEmail(getUser callback){
        FirebaseUser currUser = firebaseAuth.getCurrentUser();
        if(currUser == null){
            callback.getUser(null);
        }
        else{
            String email = currUser.getEmail();
            userDB.whereEqualTo("email", email).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(!task.getResult().isEmpty()){
                                    User result = null;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Long id = document.getLong("id");
                                        String username = document.getString("username");
                                        String email = document.getString("email");
                                        String fullname = document.getString("fullname");
                                        String phoneNumber = document.getString("phoneNumber");
                                        String avatar = document.getString("avatar");

                                        result = document.toObject(User.class);
                                    }
                                    callback.getUser(result);
                                }else{
                                    callback.onError("User không tồn tại");
                                }
                            }else{
                                callback.onFailure(task.getException());
                            }
                        }
                    });
        }
    }

    public void checkEmail(String email, checkEmailExisted callback){
        userDB.whereEqualTo("email", email).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if(!task.getResult().isEmpty()){
                            callback.onSuccess(true);
                        }else{
                            callback.onSuccess(false);
                        }
                    }else{
                        callback.onFailure(task.getException());
                    }
                }
            });
    }

    public void updateUser(User user, UserService.editUserInfo callback){
        FirebaseUser currUser = firebaseAuth.getCurrentUser();
        String email = currUser.getEmail();

        userDB.document(email).update("username", user.getUsername(), "email", user.getEmail(),
                        "fullname", user.getFullname(), "phoneNumber", user.getPhoneNumber())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.updateUser(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e);
                    }
                });
    }

    public void updatePassword(String newPwd, UserService.passwordUpdate callback){
        FirebaseUser currUser = firebaseAuth.getCurrentUser();

        currUser.updatePassword(newPwd).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.updatePassword(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e);
                    }
                });
    }

    public void updateCard(Map<String, String> newCard, UserService.cardUpdate callback){
        FirebaseUser currUser = firebaseAuth.getCurrentUser();
        String email = currUser.getEmail();

        userDB.document(email).update("card", newCard)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.updateCard(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e);
                    }
                });
    }
    public void updatePoint(Integer newPoint, UserService.pointUpdate callback){
        FirebaseUser currUser = firebaseAuth.getCurrentUser();
        String email = currUser.getEmail();

        userDB.document(email).update("point", newPoint)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.updatePoint(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e);
                    }
                });
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

    // Buy Ticket
    public void buyTicket(ArrayList<Ticket> tickets, long idUser) {
        for (Ticket item : tickets) {
            Map<String, Object> data = new HashMap<>();
            data.put("idFilm", item.getIdFilm());
            data.put("idUser", idUser);
            data.put("idCinema", item.getIdCinema());
            data.put("NumberSeat", item.getNumberSeat());
            data.put("barcode", item.getBarcode());
            data.put("date", item.getDate());
            data.put("addressCinema", item.getAddressCinema());
            data.put("Cinema", item.getCinema());
            data.put("priceDetail", item.getPriceDetail());
            data.put("Room", item.getRoom());
            data.put("time", item.getTime());
            data.put("paymentMethod", item.getPaymentMethod());
            data.put("paymentStatus", item.isPaymentStatus());
            data.put("total", item.getTotal());
            data.put("point", item.getPoint());
            data.put("idBill", item.getIdBill());
            data.put("Film", item.getFilm());
            data.put("qrcode", item.getQrcode());

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

    // Add Bill
    public interface IdBillReceiver {
        void onSuccess(String idBill);

        void onError(String errorMessage);
    }
    public void addBill(Bill item, IdBillReceiver listener) {
        Map<String, Object> data = new HashMap<>();
        data.put("idFilm", item.getIdFilm());
        data.put("idUser", item.getIdUser());
        data.put("idCinema", item.getIdCinema());
        data.put("nameCinema", item.getNameCinema());
        data.put("addressCinema", item.getAddressCinema());
        data.put("nameFilm", item.getNameFilm());
        data.put("listSeats", item.getListSeats());
        data.put("buyingdate", item.getBuyingdate());
        data.put("method", item.getMethod());
        data.put("total", item.getTotal());
        data.put("point", item.getPoint());

        db.collection("Bill")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        listener.onSuccess(documentReference.getId());
                        Log.d("TEST", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(String.valueOf(e));
                        Log.w("TEST", "Error adding document", e);
                    }
                });

    }

    //Get Bill of User
    public interface HistoryBillReceivedListener {
        void onSuccess(ArrayList<ListBill> listBill);

        void onError(String errorMessage);
    }
    public void getBillById(long idUser, HistoryBillReceivedListener listener) {
        ArrayList<ListBill> listBill = new ArrayList<>();
        db.collection("Bill")
                .whereEqualTo("idUser", idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot billItem : task.getResult()) {
                                Bill item = billItem.toObject(Bill.class);
                                listBill.add(new ListBill(item, billItem.getId()));
                                if (listBill.size() == task.getResult().size()) {
                                    listener.onSuccess(listBill);
                                }
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
    //Get Details Bill of User
    public interface DetailBillReceivedListener {
        void onSuccess(ArrayList<Ticket> details);

        void onError(String errorMessage);
    }

    public void getDetailsOfBill(long idUser, String idBill, DetailBillReceivedListener listener) {
        ArrayList<Ticket> listBill = new ArrayList<>();
        db.collection("User_Ticket")
                .whereEqualTo("idUser", idUser)
                .whereEqualTo("idBill", idBill)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot billItem : task.getResult()) {
                                Ticket item = billItem.toObject(Ticket.class);
                                listBill.add(item);
                                if (listBill.size() == task.getResult().size()) {
                                    listener.onSuccess(listBill);
                                }
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









    public interface FavoriteReceivedListener {
        void onSuccess(boolean isFavoriteListener, String idDocument);

        void onError(String errorMessage);
    }

    // Get favorite
    public void getFavorite(long idUser, long idFilm, FavoriteReceivedListener listener) {
        db.collection("FavoriteFilm")
                .whereEqualTo("idUser", idUser)
                .whereEqualTo("idFilm", idFilm)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                boolean isFavorite = document.getBoolean("isFavorite");
                                String idDocument = document.getId();
                                listener.onSuccess(isFavorite, idDocument);
                                return;
                            }
                            listener.onSuccess(false, "");
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                            listener.onError(String.valueOf(task.getException()));
                        }
                    }
                });
    }


    //Add new favorite
    public void addNewFavFilm(long idFilm, long idUser, FavoriteReceivedListener listener) {

        Map<String, Object> data = new HashMap<>();
        data.put("idFilm", idFilm);
        data.put("idUser", idUser);
        data.put("isFavorite", true);

        db.collection("FavoriteFilm")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TEST", "DocumentSnapshot written with ID: " + documentReference.getId());
                        listener.onSuccess(true, documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TEST", "Error adding document", e);
                        listener.onError(String.valueOf(e));
                    }
                });

    }

    //Update favorite film
    public void updateStateFavFilm (boolean isFavorite, String idDocument, FavoriteReceivedListener listener){
        DocumentReference washingtonRef = db.collection("FavoriteFilm").document(idDocument);

        // Set the "isCapital" field of the city 'DC'
        washingtonRef
                .update("isFavorite", isFavorite)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully updated!");
                        listener.onSuccess(isFavorite, idDocument);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating document", e);
                        listener.onError(String.valueOf(e));
                    }
                });
    }



}
