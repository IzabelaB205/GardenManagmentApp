package com.example.gardenmanagmentapp.model;

import java.io.Serializable;

public class Notification implements Serializable {

    private String content;
    private String date;
    private String sender;
    private String title;

    public Notification() {}

    public Notification(String content, String date, String sender, String title) {
        this.content = content;
        this.date = date;
        this.sender = sender;
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
