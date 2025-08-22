package com.sismics.reader.core.dao.jpa.dto;

public class CommentInfo {
    private String commentUrl;
    private Integer commentCount;

    public String getCommentUrl() { return commentUrl; }
    public void setCommentUrl(String commentUrl) { this.commentUrl = commentUrl; }

    public Integer getCommentCount() { return commentCount; }
    public void setCommentCount(Integer commentCount) { this.commentCount = commentCount; }
}