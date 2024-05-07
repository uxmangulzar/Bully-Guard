package com.example.codesteembullyguard.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationsModel {

    @SerializedName("notification_id")
    @Expose
    private Integer notificationId;
    @SerializedName("child_id")
    @Expose
    private Integer childId;
    @SerializedName("date_time")
    @Expose
    private String notificationDateTime;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("app_icon")
    @Expose
    private String appIcon;
    @SerializedName("notifications_type")
    @Expose
    private String notificationsType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("child_user_image")
    @Expose
    private String childUserImage;
    @SerializedName("child_first_name")
    @Expose
    private String childFirstName;
    @SerializedName("child_last_name")
    @Expose
    private String childLastName;



    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public String getNotificationDateTime() {
        return notificationDateTime;
    }

    public void setNotificationDateTime(String notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getNotificationsType() {
        return notificationsType;
    }

    public void setNotificationsType(String notificationsType) {
        this.notificationsType = notificationsType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
