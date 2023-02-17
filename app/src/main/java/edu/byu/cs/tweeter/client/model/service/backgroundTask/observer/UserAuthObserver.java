package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface UserAuthObserver extends ServiceObserver{
    void handleSuccess(User user, AuthToken authToken);
    void setErrorViewText(Exception e);
}
