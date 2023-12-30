package com.example.appbookticketmovie.Models;

public class FavoriteFilm {
    private long idFilm;
    private long idUser;
    private boolean isFavorite;

    public FavoriteFilm(long idFilm, long idUser, boolean isFavorite) {
        this.idFilm = idFilm;
        this.idUser = idUser;
        this.isFavorite = isFavorite;
    }

    public long getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(long idFilm) {
        this.idFilm = idFilm;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public String toString() {
        return "FavoriteFilm{" +
                "idFilm=" + idFilm +
                ", idUser=" + idUser +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
