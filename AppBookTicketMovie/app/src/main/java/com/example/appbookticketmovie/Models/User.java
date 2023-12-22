package com.example.appbookticketmovie.Models;


public class User {
    private String fullname;
    private String username;
    private String password;

    public User(){

    }

    public User(String fullname, String username, String password){
        this.fullname = fullname;
        this.username = username;
        this.password = password;
    }
}
