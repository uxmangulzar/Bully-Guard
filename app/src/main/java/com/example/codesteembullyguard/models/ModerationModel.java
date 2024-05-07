package com.example.codesteembullyguard.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModerationModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("insults")
    @Expose
    private String insults;
    @SerializedName("racism")
    @Expose
    private String racism;
    @SerializedName("homophobie")
    @Expose
    private String homophobie;
    @SerializedName("moral")
    @Expose
    private String moral;
    @SerializedName("body_shame")
    @Expose
    private String body_shame;
    @SerializedName("sexual_harassment")
    @Expose
    private String sexual_harassment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInsults() {
        return insults;
    }

    public void setInsults(String insults) {
        this.insults = insults;
    }

    public String getRacism() {
        return racism;
    }

    public void setRacism(String racism) {
        this.racism = racism;
    }

    public String getHomophobie() {
        return homophobie;
    }

    public void setHomophobie(String homophobie) {
        this.homophobie = homophobie;
    }

    public String getMoral() {
        return moral;
    }

    public void setMoral(String moral) {
        this.moral = moral;
    }

    public String getBody_shame() {
        return body_shame;
    }

    public void setBody_shame(String body_shame) {
        this.body_shame = body_shame;
    }

    public String getSexual_harassment() {
        return sexual_harassment;
    }

    public void setSexual_harassment(String sexual_harassment) {
        this.sexual_harassment = sexual_harassment;
    }
}
