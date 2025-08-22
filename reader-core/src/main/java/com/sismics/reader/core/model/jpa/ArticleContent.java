package com.sismics.reader.core.model.jpa;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class ArticleContent {
    private String title;
    @Lob
    private String description;
    private String creator;
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }
    
    @Override
    public String toString() {
        return String.format("ArticleContent[title=%s, creator=%s]",
                title, creator);
    }
}