package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public class StatusService {
    public interface StatusObserver extends SimpleNotificationObserver { }

    public void postStatusTask(Status newStatus, StatusObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new SimpleNotificationHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(statusTask);
    }


}
