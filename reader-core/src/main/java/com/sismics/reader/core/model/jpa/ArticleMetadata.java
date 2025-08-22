package com.sismics.reader.core.model.jpa;

import javax.persistence.Embeddable;

@Embeddable
public class ArticleMetadata {
    private String url;
    private String baseUri;
    private String guid;
    private String commentUrl;
    private Integer commentCount;
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public String getBaseUri() { return baseUri; }
    public void setBaseUri(String baseUri) { this.baseUri = baseUri; }
    
    public String getGuid() { return guid; }
    public void setGuid(String guid) { this.guid = guid; }
    
    public String getCommentUrl() { return commentUrl; }
    public void setCommentUrl(String commentUrl) { this.commentUrl = commentUrl; }
    
    public Integer getCommentCount() { return commentCount; }
    public void setCommentCount(Integer commentCount) { this.commentCount = commentCount; }
    
    @Override
    public String toString() {
        return String.format("ArticleMetadata[url=%s, guid=%s, commentCount=%d]",
                url, guid, commentCount);
    }
}