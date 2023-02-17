package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.Objects;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserHandler extends BackgroundTaskHandler<UserObserver> {
    private final String type;
    public UserHandler(UserObserver observer, String type) {
        super(observer);
        this.type = type;
    }

    @Override
    protected void handleSuccess(Bundle data, UserObserver observer) {
        User user = (User) data.getSerializable(BackgroundTask.USER_KEY);
        AuthToken authToken = null;
        if (!Objects.equals(type, "get_user"))
            authToken = (AuthToken) data.getSerializable(BackgroundTask.AUTH_TOKEN_KEY);
        if (Objects.equals(type, "login")) {
            // Cache user session information
            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authToken);
        }
        observer.handleSuccess(user, authToken);
    }
}
