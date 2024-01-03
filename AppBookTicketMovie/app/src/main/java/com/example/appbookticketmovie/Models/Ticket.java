package com.example.appbookticketmovie.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Ticket {

    private long idFilm;
    private String idBill;

    private int idCinema;
    private long idUser;
    private String Film;
    private String Time;
    private String Date;
    private String Cinema;
    private String addressCinema;

    //Seat
    private String NumberSeat;
    private String Room;
    private String TypeSeat;
    private long priceDetail;

    private String barcode;

    private String qrcode;
    private boolean paymentStatus = false;
    private String paymentMethod = "CASH";

    private long total;

    private int point;

    public Ticket(long idFilm, String film, String time, String date, String cinema, String numberSeat, String room, String typeSeat, long priceDetail, String barcode) {
        this.idFilm = idFilm;
        this.Film = film;
        this.Time = time;
        this.Date = date;
        this.Cinema = cinema;
        this.NumberSeat = numberSeat;
        this.Room = room;
        this.TypeSeat = typeSeat;
        this.priceDetail = priceDetail;
        this.barcode = barcode;
        this.paymentStatus = false;
        this.paymentMethod = "CASH";
    }

    public Ticket(long idFilm, long idUser, String film, String time, String date, String cinema, String numberSeat, String room, String typeSeat, long priceDetail, String barcode) {
        this.idFilm = idFilm;
        this.idUser = idUser;
        this.Film = film;
        this.Time = time;
        this.Date = date;
        this.Cinema = cinema;
        this.NumberSeat = numberSeat;
        this.Room = room;
        this.TypeSeat = typeSeat;
        this.priceDetail = priceDetail;
        this.barcode = barcode;
        this.paymentStatus = false;
        this.paymentMethod = "CASH";
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

    public Ticket(long idFilm, String idBill, int idCinema, long idUser, String film, String time, String date, String cinema, String addressCinema, String numberSeat, String room, String typeSeat, long priceDetail, String barcode, boolean paymentStatus, String paymentMethod, long total, int point) {
        this.idFilm = idFilm;
        this.idBill = idBill;
        this.idCinema = idCinema;
        this.idUser = idUser;
        Film = film;
        Time = time;
        Date = date;
        Cinema = cinema;
        this.addressCinema = addressCinema;
        NumberSeat = numberSeat;
        Room = room;
        TypeSeat = typeSeat;
        this.priceDetail = priceDetail;
        this.barcode = barcode;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.point = point;
    }

    public Ticket(long idFilm, String idBill, int idCinema, long idUser, String film, String time, String date, String cinema, String addressCinema, String numberSeat, String room, String typeSeat, long priceDetail, String barcode, String qrcode, boolean paymentStatus, String paymentMethod, long total, int point) {
        this.idFilm = idFilm;
        this.idBill = idBill;
        this.idCinema = idCinema;
        this.idUser = idUser;
        Film = film;
        Time = time;
        Date = date;
        Cinema = cinema;
        this.addressCinema = addressCinema;
        NumberSeat = numberSeat;
        Room = room;
        TypeSeat = typeSeat;
        this.priceDetail = priceDetail;
        this.barcode = barcode;
        this.qrcode = qrcode;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.point = point;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public Ticket() {
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public Ticket(long idFilm, int idCinema, long idUser, String film, String time, String date, String cinema, String addressCinema, String numberSeat, String room, String typeSeat, long priceDetail, String barcode, boolean paymentStatus, String paymentMethod, long total, int point) {
        this.idFilm = idFilm;
        this.idCinema = idCinema;
        this.idUser = idUser;
        Film = film;
        Time = time;
        Date = date;
        Cinema = cinema;
        this.addressCinema = addressCinema;
        NumberSeat = numberSeat;
        Room = room;
        TypeSeat = typeSeat;
        this.priceDetail = priceDetail;
        this.barcode = barcode;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.point = point;
    }

    public int getIdCinema() {
        return idCinema;
    }

    public void setIdCinema(int idCinema) {
        this.idCinema = idCinema;
    }

    public String getAddressCinema() {
        return addressCinema;
    }

    public void setAddressCinema(String addressCinema) {
        this.addressCinema = addressCinema;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public long getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(long idFilm) {
        this.idFilm = idFilm;
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

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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
