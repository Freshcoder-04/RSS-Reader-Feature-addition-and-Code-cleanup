package com.sismics.reader.core.dao.jpa.dto;

public class Content {
    private String title;
    private String creator;
    private String description;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}