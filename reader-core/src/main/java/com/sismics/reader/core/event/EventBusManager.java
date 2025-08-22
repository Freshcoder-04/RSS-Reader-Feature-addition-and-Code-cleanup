package com.sismics.reader.core.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.sismics.reader.core.listener.async.*;
import com.sismics.reader.core.listener.sync.DeadEventListener;
import com.sismics.util.EnvironmentUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Manages all event buses for the application.
 */
public class EventBusManager {
    private final EventBus eventBus;
    private final EventBus asyncEventBus;
    private final EventBus mailEventBus;
    private final EventBus importEventBus;
    private final List<ExecutorService> asyncExecutorList = new ArrayList<>();

    public EventBusManager() {
        this.eventBus = new EventBus();
        this.asyncEventBus = newAsyncEventBus();
        this.mailEventBus = newAsyncEventBus();
        this.importEventBus = newAsyncEventBus();
        
        registerListeners();
    }

    private void registerListeners() {
        eventBus.register(new DeadEventListener());
        asyncEventBus.register(new ArticleCreatedAsyncListener());
        asyncEventBus.register(new ArticleUpdatedAsyncListener());
        asyncEventBus.register(new ArticleDeletedAsyncListener());
        asyncEventBus.register(new RebuildIndexAsyncListener());
        asyncEventBus.register(new FaviconUpdateRequestedAsyncListener());
        importEventBus.register(new SubscriptionImportAsyncListener());
    }

    private EventBus newAsyncEventBus() {
        if (EnvironmentUtil.isUnitTest()) {
            return new EventBus();
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        asyncExecutorList.add(executor);
        return new AsyncEventBus(executor);
    }

    public void shutdown() {
        for (ExecutorService executor : asyncExecutorList) {
            executor.shutdown();
            try {
                executor.awaitTermination(60, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {}
        }
    }

    public EventBus getEventBus() { return eventBus; }
    public EventBus getAsyncEventBus() { return asyncEventBus; }
    public EventBus getMailEventBus() { return mailEventBus; }
    public EventBus getImportEventBus() { return importEventBus; }
}
