package com.example.appbookticketmovie.Models;

public class GenreItem {
    private String name;
    private Long id;

    public GenreItem(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GenreItem{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
