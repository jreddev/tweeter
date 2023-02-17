package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.CountObserver;

public class CountHandler extends BackgroundTaskHandler<CountObserver> {
    private String followType;
    public CountHandler(CountObserver observer, String followType) {
        super(observer);
        this.followType = followType;
    }
    @Override
    protected void handleSuccess(Bundle data, CountObserver observer) {
        int count = data.getInt(BackgroundTask.COUNT_KEY);
        observer.handleSuccess(count, followType);
    }
}
