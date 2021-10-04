package com.example.gardenmanagmentapp.model;

public class Notification {

    private String context;
    private String date;
    private String sender;
    private String title;

    public Notification() {}

    public Notification(String context, String date, String sender, String title) {
        this.context = context;
        this.date = date;
        this.sender = sender;
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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
