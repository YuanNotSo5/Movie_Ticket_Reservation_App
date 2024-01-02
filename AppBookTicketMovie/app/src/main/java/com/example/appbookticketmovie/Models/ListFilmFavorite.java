package com.example.appbookticketmovie.Models;

import com.example.appbookticketmovie.Domain.Metadata;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListFilmFavorite {

    private ArrayList<FilmItem> data;
    private String idDocument;

    public ListFilmFavorite(ArrayList<FilmItem> data, String idDocument) {
        this.data = data;
        this.idDocument = idDocument;
    }

    public ArrayList<FilmItem> getData() {
        return data;
    }

    public void setData(ArrayList<FilmItem> data) {
        this.data = data;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    @Override
    public String toString() {
        return "ListFilmFavorite{" +
                "data=" + data +
                ", idDocument='" + idDocument + '\'' +
                '}';
    }
}
