package com.example.codesteembullyguard.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppsModel {

    @SerializedName("app_id")
    @Expose
    private Integer app_id;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("app_image")
    @Expose
    private String app_image;
    @SerializedName("app_name")
    @Expose
    private String app_name;
    @SerializedName("set_time")
    @Expose
    private String set_time;

    @SerializedName("from_time")
    @Expose
    private String from_time;

    @SerializedName("to_time")
    @Expose
    private String to_time;

    @SerializedName("blocked")
    @Expose
    private String blocked;

    @SerializedName("package_name")
    @Expose
    private String package_name;

    public Integer getApp_id() {
        return app_id;
    }

    public void setApp_id(Integer app_id) {
        this.app_id = app_id;
    }

    public String getApp_image() {
        return app_image;
    }

    public void setApp_image(String app_image) {
        this.app_image = app_image;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getSet_time() {
        return set_time;
    }

    public void setSet_time(String set_time) {
        this.set_time = set_time;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }
}
