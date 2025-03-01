package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask {
    private static final String LOG_TAG = "PostStatusTask";
    /**
     * The new status being sent. Contains all properties of the status,
     * including the identity of the user sending the status.
     */
    private Status status;

    public PostStatusTask(AuthToken authToken, Status status, Handler messageHandler) {
        super(authToken,messageHandler);
        this.status = status;
    }
}
