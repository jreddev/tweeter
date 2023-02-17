package edu.byu.cs.tweeter.client.presenter;

import android.content.ClipData;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleListObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserAuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingPresenter extends Presenter{
    private static final int PAGE_SIZE = 10;
    private User lastFollowee;
    private boolean hasMorePages;
    public boolean hasMorePages() {
        return hasMorePages;
    }
    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    public boolean isLoading() {
        return isLoading;
    }
    private boolean isLoading = false;
    private final ItemView<User> view;
    //REFACTORED FollowService & UserService Functions
    private final FollowService followService;
    private final UserService userService;
    public GetFollowingPresenter(ItemView<User> view) {
        this.view = view;
        followService = new FollowService();
        userService = new UserService();
    }
    public void loadMoreFollowing(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(isLoading);
            followService.loadMoreItems(user, PAGE_SIZE, lastFollowee, "following", new GetFollowingObserver());
        }
    }
    public void getProfile(String userAlias) {
        userService.getProfile(userAlias, new GetUserObserver());
    }

    public class GetFollowingObserver implements SimpleListObserver<User> {
        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage("Failed to get following: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage("Failed to get following because of exception: " + ex.getMessage());

        }
        @Override
        public void handleSuccess(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(followees);
        }
    }

    public class GetUserObserver implements UserAuthObserver
    {
        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
        }
        @Override
        public void handleException(Exception ex) {
            view.displayMessage(ex.getMessage());
        }
        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            view.startIntentActivity(user);
        }
        @Override
        public void setErrorViewText(Exception e) {
            view.displayMessage(e.getMessage());
        }
    }
}
