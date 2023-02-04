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

    public interface View {

        void setLoadingFooter(boolean isLoading);
        void displayMessage(String message);
        void addItems(List<Status> statuses);
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
        public void addItems(List<Status> statuses) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addItems(statuses);
        }
    }

}
