package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetMainPresenter {

    public interface View {
        void displayMessage(String message);
        void logout();
        void updateFollowersCount(int count);
        void updateFolloweeCount(int count);
        void updateFollowButton(boolean b);
        void updateFollow(boolean success, boolean updateFollow);
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

    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        followService.updateSelectedUserFollowingAndFollowers(selectedUser, new GetFollowObserver());
    }

    public void onClickUnfollow(User selectedUser) {
        followService.onClickUnfollow(selectedUser, new GetFollowObserver());
    }

    public void onClickFollow(User selectedUser) {
        followService.onClickFollow(selectedUser, new GetFollowObserver());
    }
    public void isFollower(User selectedUser) {
        followService.isFollower(selectedUser, new GetFollowObserver());
    }

    public void postStatusTask(Status newStatus) {
        statusService.postStatusTask(newStatus, new GetStatusObserver());
    }

    public class GetUserObserver implements UserService.MainObserver {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex, String message) {
            view.displayMessage(message + ex.getMessage());
        }

        @Override
        public void startIntentActivity(User registeredUser, AuthToken authToken) {

        }

        @Override
        public void logout() {
            view.logout();
        }
    }

    public class GetFollowObserver implements FollowService.MainObserver {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex, String message) {
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
        public void updateFollowButton(boolean b) {
            view.updateFollowButton(b);
        }

        @Override
        public void updateFollow(boolean success, boolean updateFollow) {
            view.updateFollow(success, updateFollow);
        }
    }

    public class GetStatusObserver implements StatusService.Observer {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void cancelPostingToast() {
            view.cancelPostingToast();
        }
    }
}
