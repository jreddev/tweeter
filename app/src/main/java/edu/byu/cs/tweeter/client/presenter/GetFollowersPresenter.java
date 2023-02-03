package edu.byu.cs.tweeter.client.presenter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersPresenter {
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

    public interface View {

        void setLoadingFooter(boolean isLoading);

        void displayMessage(String message);

        void startIntentActivity(User user);
    }

    private View view;
    //REFACTORED FollowService & UserService Functions
    private FollowService followService;
    private UserService userService;

    public GetFollowersPresenter(View view) {
        this.view = view;
        userService = new UserService();
        followService = new FollowService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(isLoading);
            followService.loadMoreItems(user, PAGE_SIZE, lastFollowee, new GetFollowersObserver());
        }
    }

    public void onClick(String userAlias) {
        userService.onClick(userAlias, new GetUserObserver());
    }

    public class GetFollowersObserver implements FollowService.Observer {

        @Override
        public void displayError(String message) {

        }

        @Override
        public void displayException(Exception ex, String message) {

        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {

        }
    }

    private class GetUserObserver implements UserService.Observer {
        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex, String message) {
            view.displayMessage(message + ex.getMessage());
        }

        @Override
        public void startActivity(User user) {
            view.startIntentActivity(user);
        }
    }
}
