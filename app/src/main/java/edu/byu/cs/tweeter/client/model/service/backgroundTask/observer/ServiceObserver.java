package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

import java.util.concurrent.ExecutorService;

public interface ServiceObserver {
    void handleFailure(String message);
    void handleException(Exception ex, String message);
}
