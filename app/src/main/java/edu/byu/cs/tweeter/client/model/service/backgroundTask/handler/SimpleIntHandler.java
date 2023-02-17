package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleIntObserver;

public class SimpleIntHandler extends BackgroundTaskHandler<SimpleIntObserver> {
    private String followType;
    public SimpleIntHandler(SimpleIntObserver observer, String followType) {
        super(observer);
        this.followType = followType;
    }
    @Override
    protected void handleSuccess(Bundle data, SimpleIntObserver observer) {
        int count = data.getInt(BackgroundTask.COUNT_KEY);
        observer.handleSuccess(count, followType);
    }
}
