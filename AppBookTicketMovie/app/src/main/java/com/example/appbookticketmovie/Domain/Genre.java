
package com.example.appbookticketmovie.Domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Genre {
    private List<GenreItem> genres;

    public List<GenreItem> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreItem> genres) {
        this.genres = genres;
    }
}
