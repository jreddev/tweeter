package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

public interface SimpleBoolObserver extends ServiceObserver {
    void handleSuccess(boolean isFollower);
}
