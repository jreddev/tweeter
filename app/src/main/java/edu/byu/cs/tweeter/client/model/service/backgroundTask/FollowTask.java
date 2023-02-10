package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends BackgroundTask {
    private static final String LOG_TAG = "FollowTask";

    /**
     * The user that is being followed.
     */
    private User followee;

    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super.authToken = authToken;
        this.followee = followee;
        super.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {

            sendSuccessMessage();

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }


}
