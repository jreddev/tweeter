package edu.byu.cs.tweeter.client.presenter;

import android.content.ClipData;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleListObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserAuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFeedPresenter extends Presenter{
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
    private final ItemView<Status> view;
    private final UserService userService;
    private final FollowService followService;
    public GetFeedPresenter(ItemView<Status> view){
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

    public class GetUserObserver implements UserAuthObserver {

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
        }

        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.startIntentActivity(user);
        }

        @Override
        public void setErrorViewText(Exception e) {
            view.displayMessage(e.getMessage());
        }
    }

    public class GetFeedObserver implements SimpleListObserver<Status> {
        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage(message);
        }
        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage(ex.getMessage());
        }
        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(statuses);
        }
    }
}
