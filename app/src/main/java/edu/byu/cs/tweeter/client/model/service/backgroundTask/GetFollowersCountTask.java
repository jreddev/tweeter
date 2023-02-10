package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {
    private static final String LOG_TAG = "GetFollowersCountTask";
    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super.authToken = authToken;
        this.targetUser = targetUser;
        super.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {

            sendSuccessMessage(20);

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }


}
