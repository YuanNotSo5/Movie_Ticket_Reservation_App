package com.example.appbookticketmovie.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FilmItem {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("poster")
    @Expose
    private String poster;
    @SerializedName("released")
    @Expose
    private String released;
    @SerializedName("runtime")
    @Expose
    private String runtime;
    @SerializedName("directors")
    @Expose
    private String directors;
    @SerializedName("actors")
    @Expose
    private ArrayList<ActorItem> actors;
    @SerializedName("plot")
    @Expose
    private String plot;
    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("imdb_rating")
    @Expose
    private String imdbRating;

    @SerializedName("genres")
    @Expose
    private ArrayList<GenreItem> genres;

    @SerializedName("trailer")
    @Expose
    private String trailer;

    public FilmItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getDirector() {
        return directors;
    }

    public void setDirector(String directors) {
        this.directors = directors;
    }


    public ArrayList<ActorItem> getActors() {
        return actors;
    }

    public void setActors(ArrayList<ActorItem> actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public ArrayList<GenreItem> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<GenreItem> genres) {
        this.genres = genres;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String youtube) {
        this.trailer = youtube;
    }

    @Override
    public String toString() {
        return "FilmItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", poster='" + poster + '\'' +
                ", released='" + released + '\'' +
                ", runtime='" + runtime + '\'' +
                ", directors='" + directors + '\'' +
                ", actors='" + actors + '\'' +
                ", plot='" + plot + '\'' +
                ", country='" + country + '\'' +
                ", imdbRating='" + imdbRating + '\'' +
                ", genres=" + genres +
                ", trailer='" + trailer + '\'' +
                '}';
    }
}
