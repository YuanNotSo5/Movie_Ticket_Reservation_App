package com.example.appbookticketmovie.Models;

public class ListBill {

    private Bill data;
    private String idBill;

    public ListBill(Bill data, String idBill) {
        this.data = data;
        this.idBill = idBill;
    }

    public Bill getData() {
        return data;
    }

    public void setData(Bill data) {
        this.data = data;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    @Override
    public String toString() {
        return "ListBill{" +
                "data=" + data +
                ", idBill='" + idBill + '\'' +
                '}';
    }
}
