package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

public interface SimpleIntObserver extends ServiceObserver {
    void handleSuccess(int count, String followType);
}
