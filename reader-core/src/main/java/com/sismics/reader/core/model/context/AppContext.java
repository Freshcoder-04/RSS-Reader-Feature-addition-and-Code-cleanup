package com.sismics.reader.core.model.context;

import com.sismics.reader.core.dao.jpa.ConfigDao;
import com.sismics.reader.core.event.EventBusManager;
import com.sismics.reader.core.service.FeedService;
import com.sismics.reader.core.service.IndexingService;
import com.sismics.reader.core.service.ServiceInitializer;

/**
 * Global application context that provides access to core services and event buses.
 */
public class AppContext {
    private static volatile AppContext instance;
    
    private final EventBusManager eventBusManager;
    private final FeedService feedService;
    private final IndexingService indexingService;
    
    private AppContext(ConfigDao configDao) {
        this.eventBusManager = new EventBusManager();
        ServiceInitializer serviceInitializer = new ServiceInitializer(configDao);
        this.feedService = serviceInitializer.getFeedService();
        this.indexingService = serviceInitializer.getIndexingService();
    }

    public static AppContext getInstance(ConfigDao configDao) {
        if (instance == null) {
            synchronized (AppContext.class) {
                if (instance == null) {
                    instance = new AppContext(configDao);
                }
            }
        }
        return instance;
    }

    public EventBusManager getEventBusManager() {
        return eventBusManager;
    }

    public FeedService getFeedService() {
        return feedService;
    }

    public IndexingService getIndexingService() {
        return indexingService;
    }

    public void shutdown() {
        eventBusManager.shutdown();
    }
}
