
package com.example.appbookticketmovie.Models;


import java.util.ArrayList;
import java.util.List;

import com.example.appbookticketmovie.Domain.Metadata;
import com.example.appbookticketmovie.Models.FilmItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ListFilm {

    @SerializedName("data")
    @Expose
    private ArrayList<FilmItem> data;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    public ListFilm(ArrayList<FilmItem> data) {
        this.data = data;
    }

    public ArrayList<FilmItem> getData() {
        return data;
    }

    public void setData(ArrayList<FilmItem> data) {
        this.data = data;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

}
