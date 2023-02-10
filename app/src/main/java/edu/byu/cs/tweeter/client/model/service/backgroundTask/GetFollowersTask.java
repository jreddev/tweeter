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
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowersTask";
    public static final String FOLLOWERS_KEY = "followers";

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastFollower;
        super.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            Pair<List<User>, Boolean> pageOfUsers = getFollowers();

            List<User> followers = pageOfUsers.getFirst();
            boolean hasMorePages = pageOfUsers.getSecond();

            sendSuccessMessage(followers, hasMorePages);

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    private Pair<List<User>, Boolean> getFollowers() {
        Pair<List<User>, Boolean> pageOfUsers = getFakeData().getPageOfUsers(lastItem, limit, targetUser);
        return pageOfUsers;
    }

    private void sendSuccessMessage(List<User> followers, boolean hasMorePages) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(FOLLOWERS_KEY, (Serializable) followers);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }
}
