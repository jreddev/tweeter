package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

import java.util.List;

public interface SimpleListObserver<T> extends ServiceObserver {
    void handleSuccess(List<T> list, boolean hasMorePages);
}
