package com.example.gardenmanagmentapp;

public class Notification {

    private String date;
    private String title;
    private int image;
    private String sender;
    private String context;

    public Notification() {}

    public Notification(String date, String title, int image, String sender, String context) {
        this.date = date;
        this.title = title;
        this.image = image;
        this.sender = sender;
        this.context = context;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setTitle(int image) {
        this.image = image;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
