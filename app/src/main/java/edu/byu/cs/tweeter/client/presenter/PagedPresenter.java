package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleListObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserAuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class

PagedPresenter<T> extends Presenter {
    protected static final int PAGE_SIZE = 10;
    protected T last;
    protected boolean hasMorePages;
    protected boolean isLoading = false;
    public boolean hasMorePages() {
        return hasMorePages;
    }
    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    public boolean isLoading() {
        return isLoading;
    }
    protected final ItemView<T> view;
    protected final FollowService followService;
    protected final UserService userService;
    private final String type;
    public PagedPresenter(ItemView<T> view, String type) {
        this.view = view;
        this.type = type;
        userService = new UserService();
        followService = new FollowService();
    }
    public void loadMoreItems(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingFooter(isLoading);
            loadItems(user);
        }
    }
    public abstract void loadItems(User user);

    public void getProfile(String userAlias) {
        userService.getProfile(userAlias, new GetUserObserver() );
    }
    public class GetListObserver implements SimpleListObserver<T> {

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage("Failed to get " + type + ": " + message);
        }
        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            view.displayMessage("Failed to get " + type + " because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(List<T> list, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(isLoading);
            last = (list.size() > 0) ? list.get(list.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(list);
        }
    }
    public class GetUserObserver implements UserAuthObserver {

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


}
