package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {
    private static final String LOG_TAG = "LogoutTask";

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(authToken,messageHandler);
    }
}
