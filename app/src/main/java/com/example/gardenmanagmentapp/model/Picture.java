package com.example.gardenmanagmentapp.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Picture implements Serializable{

    private String pictureName;
    private String pictureUrl;
    private String key;

    public Picture() {}

    public Picture(String pictureName, String pictureUrl) {
        this.pictureName = pictureName;
        this.pictureUrl = pictureUrl;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
