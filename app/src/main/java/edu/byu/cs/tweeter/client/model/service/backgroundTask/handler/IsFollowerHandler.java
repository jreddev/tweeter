package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.IsFollowerObserver;

public class IsFollowerHandler extends BackgroundTaskHandler<IsFollowerObserver> {
    private IsFollowerObserver observer;

    public IsFollowerHandler(IsFollowerObserver observer) {
        super(observer);
    }
    @Override
    protected void handleSuccess(Bundle data, IsFollowerObserver observer) {
        observer.handleSuccess(!data.getBoolean(BackgroundTask.IS_FOLLOWER_KEY));
    }
}
