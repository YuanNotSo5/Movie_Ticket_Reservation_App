package com.example.appbookticketmovie.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class Cinema {
    private Long id;
    private String name;
    private String address;
    private GeoPoint location;

    public Cinema(Long id, String name, String address, GeoPoint location) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
    }
    public Cinema(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GeoPoint getLocation() {
        return this.location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
