package com.example.hitcapp;

public class Notification {
    private String title;
    private String content;
    private String date;
    private int icon;

    public Notification(String title, String content, String date, int icon) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public int getIcon() {
        return icon;
    }
}
