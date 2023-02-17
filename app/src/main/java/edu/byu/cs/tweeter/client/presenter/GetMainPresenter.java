package edu.byu.cs.tweeter.client.presenter;

import java.util.Objects;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleBoolObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleIntObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetMainPresenter {

    public interface View {
        void displayMessage(String message);
        void logout();
        void updateFollowersCount(int count);
        void updateFolloweeCount(int count);
        void updateFollowButton();
        void updateFollowButton(boolean follow, boolean toast);
        void cancelPostingToast();
    }

    private View view;
    private UserService userService;
    private FollowService followService;
    private StatusService statusService;

    public GetMainPresenter(View view) {
        this.view = view;
        userService = new UserService();
        followService = new FollowService();
        statusService = new StatusService();
    }

    public void onOptionsItemSelected() {
        userService.onOptionsItemSelected(new GetUserObserver());
    }

    public void updateFollowingAndFollowers(User selectedUser) {
        followService.updateFollowingAndFollowers(selectedUser, new CountObserver());
    }

    public void onClickUnfollow(User selectedUser) {
        followService.onClickUnfollow(selectedUser, new FollowObserver());
    }

    public void onClickFollow(User selectedUser) {
        followService.onClickFollow(selectedUser, new FollowObserver());
    }
    public void isFollower(User selectedUser) {
        followService.isFollower(selectedUser, new IsFollowingObserver());
    }

    public void postStatusTask(Status newStatus) {
        statusService.postStatusTask(newStatus, new GetStatusObserver());
    }

    public class GetUserObserver implements SimpleNotificationObserver {

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to logout because of exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess() {
            view.logout();
        }
    }

    public class CountObserver implements SimpleIntObserver {
        @Override
        public void handleSuccess(int count, String followType){
            if (Objects.equals(followType, "followers")){
                view.updateFollowersCount(count);
            }
            else {
                view.updateFolloweeCount(count);
            }
        }
        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage(ex.getMessage());
        }

    }

    public class GetFollowingObserver implements FollowService.MainObserver {

        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleException(Exception ex, String message) {
            view.displayMessage(message + ex.getMessage());
        }
        @Override
        public void updateFollowersCount(int count) {
            view.updateFollowersCount(count);
        }

        @Override
        public void updateFolloweeCount(int count) {
            view.updateFolloweeCount(count);
        }

        @Override
        public void updateFollowButton() {
            view.updateFollowButton();
        }

    }

    public class FollowObserver implements SimpleNotificationObserver {

        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage(ex.getMessage());
        }

        @Override
        public void handleSuccess() {
            view.updateFollowButton();
        }
    }

    public class IsFollowingObserver implements SimpleBoolObserver {

        @Override
        public void handleSuccess(boolean isFollower) {
            view.updateFollowButton(isFollower, false);
        }
        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }
    }

    public class GetStatusObserver implements StatusService.StatusObserver {

        @Override
        public void handleSuccess() {
            view.cancelPostingToast();
            view.displayMessage("Successfully Posted!");
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleException(Exception e) {
            view.displayMessage(e.getMessage());
        }
    }
}
