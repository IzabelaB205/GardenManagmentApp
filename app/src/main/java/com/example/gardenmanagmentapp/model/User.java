package com.example.gardenmanagmentapp.model;

import java.io.Serializable;

public class User implements Serializable {

    private String fullName;
    private String id;
    private String phone;
    private String email;

    public User(String fullName, String id, String phone, String email) {
        this.fullName = fullName;
        this.id = id;
        this.phone = phone;
        this.email = email;
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
}
