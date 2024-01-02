package com.example.appbookticketmovie.Services;

import androidx.annotation.NonNull;

import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.Models.Schedule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleService {
    private FirebaseFirestore db;
    private CollectionReference scheduleDB;
    private FilmService filmService;

    public ScheduleService() {
        this.db = FirebaseFirestore.getInstance();
        this.scheduleDB = db.collection("Schedule");
        this.filmService = new FilmService();
    }

    public interface getSchedules {
        void getCinemaSchedules(ArrayList<Schedule> schedules);
        void onFailure(Exception e);
    }

    public interface getTimeSchedules {
        void getTimeSchedules(String filmName, String timeStart, String timeEnd);
        void onFailure(String errMess);
    }
    public void getCinemaSchedules (Long idCinema, Long idFilm, String date, getSchedules callback){
        scheduleDB
                .whereEqualTo("date", date)
                .whereEqualTo("idFilm", idFilm)
                .whereEqualTo("idCinema", idCinema)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Schedule> schedules = new ArrayList<>();
                            if(task.getResult().size() == 0){
                                callback.getCinemaSchedules(schedules);
                            }else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Long idCinema = document.getLong("idCinema");
                                    Long idFilm = document.getLong("idFilm");
                                    Timestamp timestamp = document.getTimestamp("timestamp");
                                    Double price = document.getDouble("price");
                                    String room = document.getString("room");

                                    Schedule schedule = new Schedule(idCinema, idFilm, timestamp, price, room);
                                    schedules.add(schedule);

                                    if(schedules.size() == task.getResult().size()){
                                        callback.getCinemaSchedules(schedules);
                                    }
                                }
                            }
//                            System.out.println("schedules: "+schedules);
//                            System.out.println("size: "+task.getResult().size());

                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    public void getTime(Long idFilm, Schedule schedule, getTimeSchedules callback){
        filmService.getFilmById(idFilm, new FilmService.OnFilmDataReceivedListener() {
            @Override
            public void onFilmDataReceived(ArrayList<FilmItem> listFilms) {
//                System.out.println("film: " + listFilms);
                if(!listFilms.isEmpty()){
                    FilmItem item = listFilms.get(0);
                    Timestamp timestamp = schedule.getTimestamp();

                    String duration = item.getRuntime().split(" min")[0];
                    int dur = Integer.parseInt(duration);

                    Date timeStart = schedule.getTimestamp().toDate();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(timeStart);

                    calendar.add(Calendar.MINUTE, dur);
                    Date timeEnd = calendar.getTime();

                    callback.getTimeSchedules(item.getTitle() ,new SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeStart), new SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeEnd));
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onFailure(errorMessage);
            }
        });
    }


}
