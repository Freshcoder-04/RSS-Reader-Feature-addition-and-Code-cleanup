package com.sismics.reader.core.model.jpa;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Date;

/**
 * Article entity.
 * 
 * @author jtremeaux
 */
@Entity
@Table(name = "T_ARTICLE")
public class Article {
    /**
     * Article ID.
     */
    @Id
    @Column(name = "ART_ID_C", length = 36)
    private String id;
    
    /**
     * Feed ID.
     */
    @Column(name = "ART_IDFEED_C", nullable = false, length = 36)
    private String feedId;

    /**
     * Article metadata.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "url", column = @Column(name = "ART_URL_C", length = 2000)),
        @AttributeOverride(name = "baseUri", column = @Column(name = "ART_BASEURI_C", length = 2000)),
        @AttributeOverride(name = "guid", column = @Column(name = "ART_GUID_C", nullable = false, length = 2000))
    })
    private ArticleMetadata metadata;

    /**
     * Article content.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "title", column = @Column(name = "ART_TITLE_C", length = 4000)),
        @AttributeOverride(name = "creator", column = @Column(name = "ART_CREATOR_C", length = 200)),
        @AttributeOverride(name = "description", column = @Column(name = "ART_DESCRIPTION_C"))
    })
    private ArticleContent content;

    /**
     * Article enclosure.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "url", column = @Column(name = "ART_ENCLOSUREURL_C", length = 2000)),
        @AttributeOverride(name = "length", column = @Column(name = "ART_ENCLOSURELENGTH_N")),
        @AttributeOverride(name = "type", column = @Column(name = "ART_ENCLOSURETYPE_C", length = 2000))
    })
    private ArticleEnclosure enclosure;

    /**
     * Comment URL.
     */
    @Column(name = "ART_COMMENTURL_C", length = 2000)
    private String commentUrl;

    /**
     * Comment count.
     */
    @Column(name = "ART_COMMENTCOUNT_N")
    private Integer commentCount;

    /**
     * Publication date.
     */
    @Column(name = "ART_PUBLICATIONDATE_D", nullable = false)
    private Date publicationDate;
    
    /**
     * Creation date.
     */
    @Column(name = "ART_CREATEDATE_D", nullable = false)
    private Date createDate;
    
    /**
     * Deletion date.
     */
    @Column(name = "ART_DELETEDATE_D")
    private Date deleteDate;

    public Article() {
        this.metadata = new ArticleMetadata();
        this.content = new ArticleContent();
        this.enclosure = new ArticleEnclosure();
    }

    public Article(String id) {
        this();
        this.id = id;
    }

    // Delegate methods for ArticleMetadata
    public String getUrl() {
        return metadata.getUrl();
    }

    public void setUrl(String url) {
        metadata.setUrl(url);
    }

    public String getBaseUri() {
        return metadata.getBaseUri();
    }

    public void setBaseUri(String baseUri) {
        metadata.setBaseUri(baseUri);
    }

    public String getGuid() {
        return metadata.getGuid();
    }

    public void setGuid(String guid) {
        metadata.setGuid(guid);
    }

    // Delegate methods for ArticleContent
    public String getTitle() {
        return content.getTitle();
    }

    public void setTitle(String title) {
        content.setTitle(title);
    }

    public String getCreator() {
        return content.getCreator();
    }

    public void setCreator(String creator) {
        content.setCreator(creator);
    }

    public String getDescription() {
        return content.getDescription();
    }

    public void setDescription(String description) {
        content.setDescription(description);
    }

    // Delegate methods for ArticleEnclosure
    public String getEnclosureUrl() {
        return enclosure.getUrl();
    }

    public void setEnclosureUrl(String url) {
        enclosure.setUrl(url);
    }

    public Integer getEnclosureLength() {
        return enclosure.getLength();
    }

    public void setEnclosureLength(Integer length) {
        enclosure.setLength(length);
    }

    public String getEnclosureType() {
        return enclosure.getType();
    }

    public void setEnclosureType(String type) {
        enclosure.setType(type);
    }

    // Regular getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getCommentUrl() {
        return commentUrl;
    }

    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("metadata", metadata)
                .add("content", content)
                .add("enclosure", enclosure)
                .toString();
    }
}