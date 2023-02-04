package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StoryService;
import edu.byu.cs.tweeter.client.model.service.UserService;
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
    private StoryService storyService;

    public GetStoryPresenter(View view) {
        this.view = view;
        userService = new UserService();
        storyService = new StoryService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(isLoading);
            storyService.loadMoreItems(user, PAGE_SIZE, lastStatus, new GetStoryObserver());
        }
    }

    public void onClick(String userAlias) {
        userService.onClick(userAlias, new GetUserObserver() );
    }

    public class GetUserObserver implements UserService.Observer{

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

    public class GetStoryObserver implements StoryService.Observer {

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
        public void addItems(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            setHasMorePages(hasMorePages);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addItems(statuses);
        }
    }

}
