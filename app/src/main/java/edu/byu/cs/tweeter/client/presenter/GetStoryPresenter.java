package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetStoryPresenter {

    private static final int PAGE_SIZE = 10;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public interface View {

        void setLoadingFooter(boolean isLoading);
        void displayMessage(String message);
        void addItems(List<Status> statuses);
        void startIntentActivity(User user);
    }

    private View view;
    private UserService userService;
    private FollowService followService;

    public GetStoryPresenter(View view) {
        this.view = view;
        userService = new UserService();
        followService = new FollowService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(isLoading);
            followService.loadMoreItems(user, PAGE_SIZE, lastStatus, "story", new GetStoryObserver());
        }
    }

    public void onClick(String userAlias) {
        userService.onClick(userAlias, new GetUserObserver() );
    }

    public class GetUserObserver implements UserService.Observer{

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
            view.startIntentActivity(registeredUser);
        }
        @Override
        public void logout() {
        }
    }

    public class GetStoryObserver implements FollowService.Observer {

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
        public void addFollowees(List<User> followees, boolean hasMorePages) {
        }
        @Override
        public void addItems(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            setHasMorePages(hasMorePages);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addItems(statuses);
        }
        @Override
        public void updateFollowersCount(int count) {
        }
        @Override
        public void updateFolloweeCount(int count) {
        }

        @Override
        public void updateFollowButton(boolean b) {

        }

        @Override
        public void updateFollow(boolean success, boolean updateFollow) {

        }
    }

}
