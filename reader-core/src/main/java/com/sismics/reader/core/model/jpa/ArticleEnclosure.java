package com.sismics.reader.core.model.jpa;

import javax.persistence.Embeddable;

@Embeddable
public class ArticleEnclosure {
    private String url;
    private Integer length;
    private String type;
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public Integer getLength() { return length; }
    public void setLength(Integer length) { this.length = length; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    @Override
    public String toString() {
        return String.format("ArticleEnclosure[url=%s, type=%s, length=%d]",
                url, type, length);
    }
}