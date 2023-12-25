package com.example.appbookticketmovie.Models;

import com.google.firebase.Timestamp;

public class Schedule {
    private Long idCinema;
    private Long idFilm;
    private Timestamp timestamp;
    private Double price;
    private String room;

    public Schedule(Long idCinema, Long idFilm, Timestamp timestamp, Double price, String room) {
        this.idCinema = idCinema;
        this.idFilm = idFilm;
        this.timestamp = timestamp;
        this.price = price;
        this.room = room;
    }

    public Long getIdCinema() {
        return idCinema;
    }

    public void setIdCinema(Long idCinema) {
        this.idCinema = idCinema;
    }

    public Long getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(Long idFilm) {
        this.idFilm = idFilm;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
