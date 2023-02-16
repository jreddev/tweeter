package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class GetCountTask extends AuthenticatedTask {
    public static final int COUNT = 20;
    /**
     * The user whose following count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private final User targetUser;

    protected GetCountTask(AuthToken authToken, Handler messageHandler, User targetUser) {
        super(authToken, messageHandler);
        this.targetUser = targetUser;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, COUNT);
    }
}
