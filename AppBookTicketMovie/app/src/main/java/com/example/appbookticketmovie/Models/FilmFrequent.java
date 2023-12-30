package com.example.appbookticketmovie.Models;

public class FilmFrequent {
    private FilmItem film;
    private int frequency;

    public FilmFrequent(FilmItem film, int frequency) {
        this.film = film;
        this.frequency = frequency;
    }

    public FilmItem getFilm() {
        return film;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFilm(FilmItem film) {
        this.film = film;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "FilmFrequent{" +
                "film=" + film +
                ", frequency=" + frequency +
                '}';
    }
}
