package com.example.codesteembullyguard.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllLocation {

    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("child_id")
    @Expose
    private Integer childId;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("user_lat")
    @Expose
    private String userLat;
    @SerializedName("user_lon")
    @Expose
    private String userLon;
    @SerializedName("child_user_image")
    @Expose
    private String childUserImage;
    @SerializedName("child_first_name")
    @Expose
    private String childFirstName;
    @SerializedName("child_last_name")
    @Expose
    private String childLastName;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserLat() {
        return userLat;
    }

    public void setUserLat(String userLat) {
        this.userLat = userLat;
    }

    public String getUserLon() {
        return userLon;
    }

    public void setUserLon(String userLon) {
        this.userLon = userLon;
    }

    public String getChildUserImage() {
        return childUserImage;
    }

    public void setChildUserImage(String childUserImage) {
        this.childUserImage = childUserImage;
    }

    public String getChildFirstName() {
        return childFirstName;
    }

    public void setChildFirstName(String childFirstName) {
        this.childFirstName = childFirstName;
    }

    public String getChildLastName() {
        return childLastName;
    }

    public void setChildLastName(String childLastName) {
        this.childLastName = childLastName;
    }

}
