package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleListObserver;

public class SimpleListHandler<T> extends BackgroundTaskHandler<SimpleListObserver> {
    private final String type;

    public SimpleListHandler(SimpleListObserver observer, String type) {
        super(observer);
        this.type = type;
    }

    @Override
    protected void handleSuccess(Bundle data, SimpleListObserver observer) {
        List<T> items = (List<T>) data.getSerializable(BackgroundTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(BackgroundTask.MORE_PAGES_KEY);
        observer.handleSuccess(items, hasMorePages);
    }
}
