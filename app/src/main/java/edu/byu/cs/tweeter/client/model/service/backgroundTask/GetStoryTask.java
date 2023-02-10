package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedTask<Status> {
    private static final String LOG_TAG = "GetStoryTask";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastStatus;
        super.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            Pair<List<Status>, Boolean> pageOfStatus = getStory();

            List<Status> statuses = pageOfStatus.getFirst();
            boolean hasMorePages = pageOfStatus.getSecond();

            sendSuccessMessage(statuses, hasMorePages);

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    private Pair<List<Status>, Boolean> getStory() {
        Pair<List<Status>, Boolean> pageOfStatus = getFakeData().getPageOfStatus(lastItem, limit);
        return pageOfStatus;
    }

    private void sendSuccessMessage(List<Status> statuses, boolean hasMorePages) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(STATUSES_KEY, (Serializable) statuses);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }
}