package com.sismics.reader.core.dao.jpa.dto;

import java.util.Date;

public class DateInfo {
    private Date publicationDate;
    private Date createDate;

    public Date getPublicationDate() { return publicationDate; }
    public void setPublicationDate(Date publicationDate) { this.publicationDate = publicationDate; }

    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
}