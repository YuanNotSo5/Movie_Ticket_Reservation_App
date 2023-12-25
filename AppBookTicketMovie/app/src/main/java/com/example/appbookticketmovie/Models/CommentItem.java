package com.example.appbookticketmovie.Models;

public class CommentItem {
    private String userName;
    private String date;
    private String commnet;
    private String rating;
    private String photo;

    public CommentItem(String userName, String date, String commnet, String rating, String photo) {
        this.userName = userName;
        this.date = date;
        this.commnet = commnet;
        this.rating = rating;
        this.photo = photo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommnet() {
        return commnet;
    }

    public void setCommnet(String commnet) {
        this.commnet = commnet;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "CommentItem{" +
                "userName='" + userName + '\'' +
                ", date='" + date + '\'' +
                ", commnet='" + commnet + '\'' +
                ", rating='" + rating + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
