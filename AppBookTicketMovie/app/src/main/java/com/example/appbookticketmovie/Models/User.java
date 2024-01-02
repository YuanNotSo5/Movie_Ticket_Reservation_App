package com.example.appbookticketmovie.Models;


import java.util.HashMap;
import java.util.Map;

public class User {
    private Long id;
    private String username;
    private String email;
    private String fullname;
    private String phoneNumber;
    private String avatar;
    private Integer point;

    private Map<String, String> card;
    public User(){

    }

    public User(Long id, String username, String email, String fullname, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.avatar = "https://firebasestorage.googleapis.com/v0/b/android-movie-ticket-booking.appspot.com/o/Users%2Favatar.jpg?alt=media&token=a6d87d7b-fea7-4fd6-8cc0-f164e1314d57";
        this.point = 0;
        this.card = new HashMap<>();
    }

    public User(Long id, String username, String email, String fullname) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.phoneNumber = null;
        this.avatar = "https://firebasestorage.googleapis.com/v0/b/android-movie-ticket-booking.appspot.com/o/Users%2Favatar.jpg?alt=media&token=a6d87d7b-fea7-4fd6-8cc0-f164e1314d57";
        this.point = 0;
        this.card = new HashMap<>();
    }
    public User(String username, String email, String fullname, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.avatar = null;
    }
    public User(String username, String email, String fullname, String phoneNumber, String avatar) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Map<String, String> getCard() {
        return card;
    }

    public void setCard(Map<String, String> card) {
        this.card = card;
    }
}
