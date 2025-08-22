package com.sismics.reader.core.dao.jpa.mapper;

import com.sismics.reader.core.dao.jpa.dto.ArticleDto;
import com.sismics.util.jpa.ResultMapper;

public class ArticleMapper extends ResultMapper<ArticleDto> {
    @Override
    public ArticleDto map(Object[] o) {
        int i = 0;
        ArticleDto dto = new ArticleDto();
        
        // Core fields
        dto.setId(stringValue(o[i++]));
        dto.setFeedId(stringValue(o[i++]));
        
        // Metadata fields
        dto.getMetadata().setUrl(stringValue(o[i++]));
        dto.getMetadata().setBaseUri(stringValue(o[i++]));
        dto.getMetadata().setGuid(stringValue(o[i++]));
        
        // Content fields
        dto.getContent().setTitle(stringValue(o[i++]));
        dto.getContent().setCreator(stringValue(o[i++]));
        dto.getContent().setDescription(stringValue(o[i++]));
        
        // Comment fields
        dto.getCommentInfo().setCommentUrl(stringValue(o[i++]));
        dto.getCommentInfo().setCommentCount(intValue(o[i++]));
        
        // Enclosure fields
        dto.getEnclosure().setEnclosureUrl(stringValue(o[i++]));
        dto.getEnclosure().setEnclosureLength(intValue(o[i++]));
        dto.getEnclosure().setEnclosureType(stringValue(o[i++]));
        
        // Date fields
        dto.getDateInfo().setPublicationDate(dateValue(o[i++]));
        dto.getDateInfo().setCreateDate(dateValue(o[i]));

        return dto;
    }
}