package com.sismics.reader.core.service;

import com.sismics.reader.core.constant.ConfigType;
import com.sismics.reader.core.dao.jpa.ConfigDao;
import com.sismics.reader.core.model.jpa.Config;

/**
 * Initializes services required by the application.
 */
public class ServiceInitializer {
    private final FeedService feedService;
    private final IndexingService indexingService;

    public ServiceInitializer(ConfigDao configDao) {
        this.feedService = new FeedService();
        this.feedService.startAndWait();

        Config luceneStorageConfig = configDao.getById(ConfigType.LUCENE_DIRECTORY_STORAGE);
        this.indexingService = new IndexingService(luceneStorageConfig != null ? luceneStorageConfig.getValue() : null);
        this.indexingService.startAndWait();
    }

    public FeedService getFeedService() {
        return feedService;
    }

    public IndexingService getIndexingService() {
        return indexingService;
    }
}
