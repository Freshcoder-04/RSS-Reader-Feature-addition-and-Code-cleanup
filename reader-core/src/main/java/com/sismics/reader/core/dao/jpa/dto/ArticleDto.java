package com.sismics.reader.core.dao.jpa.dto;

public class ArticleDto {
    private String id;
    private String feedId;
    private Metadata metadata;
    private Content content;
    private CommentInfo commentInfo;
    private Enclosure enclosure;
    private DateInfo dateInfo;

    public ArticleDto() {
        this.metadata = new Metadata();
        this.content = new Content();
        this.commentInfo = new CommentInfo();
        this.enclosure = new Enclosure();
        this.dateInfo = new DateInfo();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFeedId() { return feedId; }
    public void setFeedId(String feedId) { this.feedId = feedId; }

    public Metadata getMetadata() { return metadata; }
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }

    public Content getContent() { return content; }
    public void setContent(Content content) { this.content = content; }

    public CommentInfo getCommentInfo() { return commentInfo; }
    public void setCommentInfo(CommentInfo commentInfo) { this.commentInfo = commentInfo; }

    public Enclosure getEnclosure() { return enclosure; }
    public void setEnclosure(Enclosure enclosure) { this.enclosure = enclosure; }

    public DateInfo getDateInfo() { return dateInfo; }
    public void setDateInfo(DateInfo dateInfo) { this.dateInfo = dateInfo; }

    public void clear() {
        this.metadata = null;
        this.content = null;
        this.commentInfo = null;
        this.enclosure = null;
        this.dateInfo = null;
    }
}