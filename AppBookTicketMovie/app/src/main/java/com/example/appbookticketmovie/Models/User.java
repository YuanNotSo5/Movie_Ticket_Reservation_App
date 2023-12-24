package com.example.appbookticketmovie.Models;


public class User {
    private Long id;
    private String username;
    private String email;
    private String fullname;
    private String phoneNumber;
    private String avatar;
    public User(){

    }

    public User(Long id, String username, String email, String fullname, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.avatar = "https://firebasestorage.googleapis.com/v0/b/android-movie-ticket-booking.appspot.com/o/Users%2Favatar.jpg?alt=media&token=a6d87d7b-fea7-4fd6-8cc0-f164e1314d57";
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
}
