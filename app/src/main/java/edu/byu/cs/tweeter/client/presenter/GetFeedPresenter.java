package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFeedPresenter {
    private Status lastStatus;
    private static final int PAGE_SIZE = 10;
    private boolean hasMorePages;
    private boolean isLoading = false;
    public boolean isLoading() {
        return isLoading;
    }
    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    public boolean hasMorePages() {
        return hasMorePages;
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
    public GetFeedPresenter(View view){
        this.view = view;
        userService = new UserService();
        followService = new FollowService();
    }

    public void getProfile(String userAlias) {
        userService.getProfile(userAlias, new GetUserObserver());
    }

    public void loadMoreFeeds(User user) {
        if (!isLoading){
            isLoading = true;
            view.setLoadingFooter(isLoading);
            followService.loadMoreItems(user, PAGE_SIZE, lastStatus, "feed", new GetFeedObserver());
        }
    }

    public class GetUserObserver implements UserService.Observer {

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
        }

        @Override
        public void handleException(Exception ex, String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
        }

        @Override
        public void startIntentActivity(User user, AuthToken authToken) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.startIntentActivity(user);
        }
    }

    public class GetFeedObserver implements FollowService.FeedStoryObserver {
        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage(message);
        }
        @Override
        public void handleException(Exception ex, String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage(message + ex.getMessage());
        }
        @Override
        public void addItems(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(statuses);
        }
    }
}
