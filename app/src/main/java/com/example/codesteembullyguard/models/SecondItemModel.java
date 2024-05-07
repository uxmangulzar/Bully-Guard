package com.example.codesteembullyguard.models;

public class SecondItemModel {

    String name,time,device;
    int image;

    public SecondItemModel(String name, String time, String device, int image) {
        this.name = name;
        this.time = time;
        this.device = device;
        this.image = image;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
