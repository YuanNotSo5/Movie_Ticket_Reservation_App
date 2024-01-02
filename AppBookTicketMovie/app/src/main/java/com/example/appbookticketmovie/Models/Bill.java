package com.example.appbookticketmovie.Models;

import java.util.ArrayList;

public class Bill {

    private ArrayList<seatInfo> listSeats;
    private long idFilm;
    private String nameFilm;
    private int idCinema;
    private long total;
    private String nameCinema;
    private String addressCinema;
    private String buyingdate;
    private String method;
    private int point;

    private long idUser;

    public Bill() {
    }

    public Bill(ArrayList<seatInfo> listSeats, long idFilm, String nameFilm, int idCinema, long total, String nameCinema, String addressCinema, String buyingdate, String method, int point, long idUser) {
        this.listSeats = listSeats;
        this.idFilm = idFilm;
        this.nameFilm = nameFilm;
        this.idCinema = idCinema;
        this.total = total;
        this.nameCinema = nameCinema;
        this.addressCinema = addressCinema;
        this.buyingdate = buyingdate;
        this.method = method;
        this.point = point;
        this.idUser = idUser;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public ArrayList<seatInfo> getListSeats() {
        return listSeats;
    }

    public void setListSeats(ArrayList<seatInfo> listSeats) {
        this.listSeats = listSeats;
    }

    public long getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(long idFilm) {
        this.idFilm = idFilm;
    }

    public String getNameFilm() {
        return nameFilm;
    }

    public void setNameFilm(String nameFilm) {
        this.nameFilm = nameFilm;
    }

    public int getIdCinema() {
        return idCinema;
    }

    public void setIdCinema(int idCinema) {
        this.idCinema = idCinema;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getNameCinema() {
        return nameCinema;
    }

    public void setNameCinema(String nameCinema) {
        this.nameCinema = nameCinema;
    }

    public String getAddressCinema() {
        return addressCinema;
    }

    public void setAddressCinema(String addressCinema) {
        this.addressCinema = addressCinema;
    }

    public String getBuyingdate() {
        return buyingdate;
    }

    public void setBuyingdate(String buyingdate) {
        this.buyingdate = buyingdate;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "listSeats=" + listSeats +
                ", idFilm=" + idFilm +
                ", nameFilm='" + nameFilm + '\'' +
                ", idCinema=" + idCinema +
                ", total=" + total +
                ", nameCinema='" + nameCinema + '\'' +
                ", addressCinema='" + addressCinema + '\'' +
                ", buyingdate='" + buyingdate + '\'' +
                ", method='" + method + '\'' +
                ", point=" + point +
                '}';
    }
}
