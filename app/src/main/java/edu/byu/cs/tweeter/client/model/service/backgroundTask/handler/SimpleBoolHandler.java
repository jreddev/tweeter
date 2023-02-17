package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleBoolObserver;

public class SimpleBoolHandler extends BackgroundTaskHandler<SimpleBoolObserver> {
    private SimpleBoolObserver observer;

    public SimpleBoolHandler(SimpleBoolObserver observer) {
        super(observer);
    }
    @Override
    protected void handleSuccess(Bundle data, SimpleBoolObserver observer) {
        observer.handleSuccess(!data.getBoolean(BackgroundTask.IS_FOLLOWER_KEY));
    }
}
