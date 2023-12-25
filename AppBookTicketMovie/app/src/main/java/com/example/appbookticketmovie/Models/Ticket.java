package com.example.appbookticketmovie.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Ticket {

    private long idFilm;
    private String Film;
    private String Time;
    private String Date;
    private String Cinema;

    //Seat
    private String NumberSeat;
    private String Room;
    private String TypeSeat;
    private long priceDetail;

    private String barcode;

    public Ticket(long idFilm, String film, String time, String date, String cinema, String numberSeat, String room, String typeSeat, long priceDetail, String barcode) {
        this.idFilm = idFilm;
        Film = film;
        Time = time;
        Date = date;
        Cinema = cinema;
        NumberSeat = numberSeat;
        Room = room;
        TypeSeat = typeSeat;
        this.priceDetail = priceDetail;
        this.barcode = barcode;
    }

    public long getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(long idFilm) {
        this.idFilm = idFilm;
    }

    public Ticket(String film, String time, String date, String cinema, String numberSeat, String room, String typeSeat, long priceDetail, String barcode) {
        Film = film;
        Time = time;
        Date = date;
        Cinema = cinema;
        NumberSeat = numberSeat;
        Room = room;
        TypeSeat = typeSeat;
        this.priceDetail = priceDetail;
        this.barcode = barcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    // Convert Bitmap to String
    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public Bitmap stringToBitmap(String encodedString) {
        byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
