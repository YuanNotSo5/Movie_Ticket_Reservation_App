
package com.example.appbookticketmovie.Models;

import java.util.List;

public class Genre {
    private List<GenreItem> genres;

    public List<GenreItem> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreItem> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "genres=" + genres +
                '}';
    }
}
