package com.example.gardenmanagmentapp.model;

import java.io.Serializable;

public class User implements Serializable {

    private String fullName;
    private String id;
    private String phone;
    private String email;
    private String password;
    private String pictureLink;

    public User() {}

    public User(String fullName, String id, String phone, String email,String password,String pictureLink) {
        this.fullName = fullName;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.pictureLink = pictureLink;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

}
