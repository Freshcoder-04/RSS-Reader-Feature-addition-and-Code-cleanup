package com.sismics.reader.core.dao.jpa.dto;

public class Metadata {
    private String url;
    private String baseUri;
    private String guid;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getBaseUri() { return baseUri; }
    public void setBaseUri(String baseUri) { this.baseUri = baseUri; }

    public String getGuid() { return guid; }
    public void setGuid(String guid) { this.guid = guid; }
}