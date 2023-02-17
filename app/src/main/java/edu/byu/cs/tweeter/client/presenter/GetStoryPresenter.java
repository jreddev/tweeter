package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleListObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserAuthObserver;
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

    public void loadMoreStories(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(isLoading);
            followService.loadMoreItems(user, PAGE_SIZE, lastStatus, "story", new GetStoryObserver());
        }
    }

    public void getProfile(String userAlias) {
        userService.getProfile(userAlias, new GetUserObserver() );
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

    public class GetStoryObserver implements SimpleListObserver<Status> {
        //TODO:: Observer specific messages and exceptions could be put here in all of the presensters.
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
            setHasMorePages(hasMorePages);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addItems(statuses);
        }
    }

}
