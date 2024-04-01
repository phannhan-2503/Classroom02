package com.example.classroom02.Adapter;

public class Classroom {
    private String imageUrl;
    private String name;
    private String part;
    private String room;
    private String theme;

    public Classroom(String imageUrl, String name, String part, String room, String theme) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.part = part;
        this.room = room;
        this.theme = theme;
    }

    public Classroom(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}

