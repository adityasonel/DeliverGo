package com.adityasonel.delivergo.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DeliveryItemPOJO extends RealmObject {
    @PrimaryKey
    private int id;
    private String description, imageUrl;
    private RealmList<LocationPOJO> location = new RealmList<>();

    public DeliveryItemPOJO() {
    }

    public DeliveryItemPOJO(int id, String description, String imageUrl, RealmList<LocationPOJO> location) {
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.location = location;
    }

    public DeliveryItemPOJO(JSONObject object) throws JSONException {
        id = object.optInt("id");
        description = object.optString("description");
        imageUrl = object.optString("imageUrl");
        location.add(parseLocation(id, object.optJSONObject("location")));
    }

    private LocationPOJO parseLocation(int id, JSONObject object) {
        double lat =  object.optDouble("lat");
        double lng =  object.optDouble("lng");
        String address =  object.optString("address");
        return new LocationPOJO(id, lat, lng, address);
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

    public RealmList<LocationPOJO> getLocation() {
        return location;
    }

    public void setLocation(RealmList<LocationPOJO> location) {
        this.location = location;
    }
}
