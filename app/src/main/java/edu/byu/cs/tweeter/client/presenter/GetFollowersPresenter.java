package edu.byu.cs.tweeter.client.presenter;

import android.content.ClipData;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleListObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserAuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersPresenter extends Presenter {
    private static final int PAGE_SIZE = 10;
    private User lastFollower;
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

    public GetFollowersPresenter(ItemView<User> view) {
        this.view = view;
        userService = new UserService();
        followService = new FollowService();
    }

    public void loadMoreFollowers(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(isLoading);
            followService.loadMoreItems(user, PAGE_SIZE, lastFollower, "followers", new GetFollowersObserver());
        }
    }

    public void getProfile(String userAlias) {
        userService.getProfile(userAlias, new GetUserObserver());
    }

    public class GetFollowersObserver implements SimpleListObserver<User> {

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage("Failed to get followers: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage("Failed to get followers because of exception: " + ex.getMessage());
        }
        @Override
        public void handleSuccess(List<User> followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(followers);
        }
    }

    public class GetUserObserver implements UserAuthObserver {
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
