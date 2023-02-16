package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFeedHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowersHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetStoryHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.GetMainPresenter;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    public interface Observer {
        void handleFailure(String message);
        void handleException(Exception ex, String message);


    }
    public interface FolloweeObserver extends Observer {
        void addFollowees(List<User> followees, boolean hasMorePages);
    }
    public interface FeedStoryObserver extends Observer{
        void addItems(List<Status> statuses, boolean hasMorePages);
    }
    public interface MainObserver extends Observer{
        void updateFollowersCount(int count);
        void updateFolloweeCount(int count);
        void updateFollowButton();
    }

    public interface IsFollowingObserver extends Observer {
        void handleSuccess(boolean b);
    }

    public interface FollowObserver extends SimpleNotificationObserver {
    }

    public void loadMoreItems(User user, int pageSize, User lastFollow, String type, FolloweeObserver observer) {
        if (Objects.equals(type, "following")){
            GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                    user, pageSize, lastFollow, new GetFollowingHandler(observer));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(getFollowingTask);
        }
        else if (Objects.equals(type, "followers")){
            GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                    user, pageSize, lastFollow, new GetFollowersHandler(observer));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(getFollowersTask);
        }
        else{
            throw new RuntimeException("Wrong input: following or followers in FollowService");
        }
    }
    public void loadMoreItems(User user, int pageSize, Status lastStatus, String type, FeedStoryObserver observer) {
        if (Objects.equals(type, "story")){
            GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                    user, pageSize, lastStatus, new GetStoryHandler(observer));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(getStoryTask);
        }
        else if (Objects.equals(type, "feed")){
            GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                    user, pageSize, lastStatus, new GetFeedHandler(observer));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(getFeedTask);
        }
        else{
            throw new RuntimeException("Wrong input: feed or story in FollowService");
        }
    }
    public void updateFollowingAndFollowers(User selectedUser, MainObserver observer) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(observer));
        executor.execute(followersCountTask);
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(observer));
        executor.execute(followingCountTask);
    }
    public void onClickUnfollow(User selectedUser, GetMainPresenter.FollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }
    public void onClickFollow(User selectedUser, FollowObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }
    public void isFollower(User selectedUser, IsFollowingObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

}
