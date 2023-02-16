package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedTask <T> extends AuthenticatedTask {
    /**
     * The user whose feed is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private final User targetUser;
    /**
     * Maximum number of statuses/users to return (i.e., page size).
     */
    private final int limit;
    /**
     * The last status returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    private final T lastItem;

    private List<T> items;
    private boolean hasMorePages;

    protected PagedTask(AuthToken authToken, Handler messageHandler, User targetUser, int limit, T lastItem) {
        super(authToken,messageHandler);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    protected User getTargetUser() {return targetUser;}
    protected int getLimit() {return limit;}
    protected T getLastItem() {return lastItem;}

    protected final void runTask() throws IOException {
        Pair<List<T>, Boolean> pageOfItems = getItems();
        items = pageOfItems.getFirst();
        hasMorePages = pageOfItems.getSecond();
    }

    protected abstract Pair<List<T>, Boolean> getItems();
    protected abstract List<User> getUsersForItems(List<T> items);

    @Override
    protected final void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }
}
