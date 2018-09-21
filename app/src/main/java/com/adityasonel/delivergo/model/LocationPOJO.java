package com.adityasonel.delivergo.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LocationPOJO extends RealmObject {
    @PrimaryKey
    private int id;
    private double lat, lng;
    private String address;

    public LocationPOJO() {
    }

    public LocationPOJO(int id, double lat, double lng, String address) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
