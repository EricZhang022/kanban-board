package com.kanbanboard.backend.dto;

public class CreateCardRequest {
    private String title;
    private String description;
    private String color;
    private String link;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
}