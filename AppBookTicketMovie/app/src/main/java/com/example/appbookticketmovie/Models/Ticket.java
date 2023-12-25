package com.example.appbookticketmovie.Models;

public class Ticket {
    private String Film;
    private String Time;
    private String Date;
    private String Cinema;

    //Seat
    private String NumberSeat;
    private String Room;
    private String TypeSeat;
    private long priceDetail;

    public Ticket(String film, String time, String date, String cinema, String numberSeat, String room, String typeSeat, long priceDetail) {
        Film = film;
        Time = time;
        Date = date;
        Cinema = cinema;
        NumberSeat = numberSeat;
        Room = room;
        TypeSeat = typeSeat;
        this.priceDetail = priceDetail;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getFilm() {
        return Film;
    }

    public void setFilm(String film) {
        Film = film;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCinema() {
        return Cinema;
    }

    public void setCinema(String cinema) {
        Cinema = cinema;
    }

    public String getNumberSeat() {
        return NumberSeat;
    }

    public void setNumberSeat(String numberSeat) {
        NumberSeat = numberSeat;
    }

    public String getTypeSeat() {
        return TypeSeat;
    }

    public void setTypeSeat(String typeSeat) {
        TypeSeat = typeSeat;
    }

    public long getPriceDetail() {
        return priceDetail;
    }

    public void setPriceDetail(long priceDetail) {
        this.priceDetail = priceDetail;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "Film='" + Film + '\'' +
                ", Time='" + Time + '\'' +
                ", Date='" + Date + '\'' +
                ", Cinema='" + Cinema + '\'' +
                ", NumberSeat='" + NumberSeat + '\'' +
                ", Room='" + Room + '\'' +
                ", TypeSeat='" + TypeSeat + '\'' +
                ", priceDetail=" + priceDetail +
                '}';
    }
}
