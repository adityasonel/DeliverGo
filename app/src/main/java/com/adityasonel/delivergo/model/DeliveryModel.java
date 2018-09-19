package com.adityasonel.delivergo.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class DeliveryModel implements Serializable {
    private int id;
    private String description, imageUrl, lat, lng, address;
    private JSONObject location;

    public DeliveryModel() {

    }

    public DeliveryModel(JSONObject object) throws JSONException {
        id = object.optInt("id");
        description = object.optString("description");
        imageUrl = object.optString("imageUrl");

        location = object.optJSONObject("location");
        lat = location.optString("lat");
        lng = location.optString("lng");
        address = location.optString("address");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public JSONObject getLocation() {
        return location;
    }

    public void setLocation(JSONObject location) {
        this.location = location;
    }
}
