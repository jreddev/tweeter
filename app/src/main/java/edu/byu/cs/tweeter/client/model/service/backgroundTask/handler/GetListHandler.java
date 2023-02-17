package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetListObserver;

public class GetListHandler<T> extends BackgroundTaskHandler<GetListObserver> {
    private final String type;

    public GetListHandler(GetListObserver observer, String type) {
        super(observer);
        this.type = type;
    }

    @Override
    protected void handleSuccess(Bundle data, GetListObserver observer) {
        List<T> items = (List<T>) data.getSerializable(BackgroundTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(BackgroundTask.MORE_PAGES_KEY);
        observer.handleSuccess(items, hasMorePages);
    }
}
