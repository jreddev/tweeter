package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowingTask";
    public static final String FOLLOWEES_KEY = "followees";


    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastFollowee;
        super.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            Pair<List<User>, Boolean> pageOfUsers = getFollowees();

            List<User> followees = pageOfUsers.getFirst();
            boolean hasMorePages = pageOfUsers.getSecond();

            sendSuccessMessage(followees, hasMorePages);

        } catch (Exception ex) {
            Log.e(LOG_TAG, "Failed to get followees", ex);
            sendExceptionMessage(ex);
        }
    }

    private Pair<List<User>, Boolean> getFollowees() {
        return getFakeData().getPageOfUsers((User) lastItem, limit, targetUser);
    }


    private void sendSuccessMessage(List<User> followees, boolean hasMorePages) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(FOLLOWEES_KEY, (Serializable) followees);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }
}
