package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Message;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedTask <T> extends AuthenticatedTask {
    public static final String MORE_PAGES_KEY = "more-pages";
    public static final String STATUSES_KEY = "statuses";
    /**
     * The user whose feed is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected User targetUser;
    /**
     * Maximum number of statuses/users to return (i.e., page size).
     */
    protected int limit;
    /**
     * The last status returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    protected T lastItem;

}
