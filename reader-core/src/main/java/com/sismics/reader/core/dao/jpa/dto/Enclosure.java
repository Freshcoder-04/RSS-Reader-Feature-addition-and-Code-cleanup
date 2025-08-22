package com.sismics.reader.core.dao.jpa.dto;

public class Enclosure {
    private String enclosureUrl;
    private Integer enclosureLength;
    private String enclosureType;

    public String getEnclosureUrl() { return enclosureUrl; }
    public void setEnclosureUrl(String enclosureUrl) { this.enclosureUrl = enclosureUrl; }

    public Integer getEnclosureLength() { return enclosureLength; }
    public void setEnclosureLength(Integer enclosureLength) { this.enclosureLength = enclosureLength; }

    public String getEnclosureType() { return enclosureType; }
    public void setEnclosureType(String enclosureType) { this.enclosureType = enclosureType; }
}