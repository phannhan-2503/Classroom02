package com.example.classroom02.Adapter;

public class Classroom {
    private String id; // Thêm biến id
    private String imageUrl;
    private String name;
    private String part;
    private String room;
    private String theme;
    private String creatorId;
    private boolean archived;

    public Classroom() {
        // Empty constructor required for Firebase
    }

    // Constructor
    public Classroom(String id, String imageUrl, String name, String part, String room, String theme, String creatorId, boolean archived) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.part = part;
        this.room = room;
        this.theme = theme;
        this.creatorId = creatorId;
        this.archived = archived;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
