package com.example.appbookticketmovie.Models;

public class Room {
    private String map;
    private Integer available;
    private Integer seat;
    private String idRoom;

    public Room() {
    }
    public Room(String map, Integer available, Integer seat, String idRoom) {
        this.map = map;
        this.available = available;
        this.seat = seat;
        this.idRoom = idRoom;
    }


    public String getMap() {
        return this.map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    @Override
    public String toString() {
        return "Cinema{" +
                "map='" + map + '\'' +
                ", available=" + available +
                ", seat=" + seat +
                ", idRoom='" + idRoom + '\'' +
                '}';
    }
}
