package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersPresenter {
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

    public interface View {

        void setLoadingFooter(boolean isLoading);
        void displayMessage(String message);
        void startIntentActivity(User user);
        void addMoreItems(List<User> followers);
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
            followService.loadMoreItems(user, PAGE_SIZE, lastFollower, "follower", new GetFollowersObserver());
        }
    }

    public void onClick(String userAlias) {
        userService.onClick(userAlias, new GetUserObserver());
    }

    public class GetFollowersObserver implements FollowService.Observer {

        @Override
        public void displayMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex, String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage(message + ex.getMessage());
        }

        @Override
        public void addFollowees(List<User> followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addMoreItems(followers);
        }

        @Override
        public void addItems(List<Status> statuses, boolean hasMorePages) {
            //Not Needed here
        }
    }

    public class GetUserObserver implements UserService.Observer {
        @Override
        public void displayMessage(String message) {
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
        @Override
        public void startIntentActivity(User registeredUser, AuthToken authToken) {
        }
    }
}
