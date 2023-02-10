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
public class GetFollowersTask extends BackgroundTask {
    private static final String LOG_TAG = "GetFollowersTask";
    public static final String FOLLOWERS_KEY = "followers";
    public static final String MORE_PAGES_KEY = "more-pages";
    /**
     * The user whose followers are being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;
    /**
     * Maximum number of followers to return (i.e., page size).
     */
    private int limit;
    /**
     * The last follower returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    private User lastFollower;

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastFollower = lastFollower;
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

    private FakeData getFakeData() {
        return FakeData.getInstance();
    }

    private Pair<List<User>, Boolean> getFollowers() {
        Pair<List<User>, Boolean> pageOfUsers = getFakeData().getPageOfUsers(lastFollower, limit, targetUser);
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
