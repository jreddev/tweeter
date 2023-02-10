package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.User;

public abstract class GetCountTask extends AuthenticatedTask {
    public static final String COUNT_KEY = "count";
    /**
     * The user whose following count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected User targetUser;

    protected void sendSuccessMessage(int count) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putInt(COUNT_KEY, count);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }
}
