package com.example.codesteembullyguard.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainResponseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("allchilds")
    @Expose
    private List<ChildrenModel> allchilds;

    @SerializedName("childmoderation")
    @Expose
    private List<ModerationModel> childmoderation;

    @SerializedName("allappstats")
    @Expose
    private List<AppsModel> allappstats;

    @SerializedName("allNotifications")
    @Expose
    private List<NotificationsModel> allNotifications;

    @SerializedName("allLocations")
    @Expose
    private List<AllLocation> allLocations;



    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<ChildrenModel> getAllchilds() {
        return allchilds;
    }

    public void setAllchilds(List<ChildrenModel> allchilds) {
        this.allchilds = allchilds;
    }

    public List<NotificationsModel> getAllNotifications() {
        return allNotifications;
    }

    public void setAllNotifications(List<NotificationsModel> allNotifications) {
        this.allNotifications = allNotifications;
    }

    public List<AppsModel> getAllappstats() {
        return allappstats;
    }

    public void setAllappstats(List<AppsModel> allappstats) {
        this.allappstats = allappstats;
    }

    public List<AllLocation> getAllLocations() {
        return allLocations;
    }

    public void setAllLocations(List<AllLocation> allLocations) {
        this.allLocations = allLocations;
    }

    public List<ModerationModel> getChildmoderation() {
        return childmoderation;
    }

    public void setChildmoderation(List<ModerationModel> childmoderation) {
        this.childmoderation = childmoderation;
    }
}
