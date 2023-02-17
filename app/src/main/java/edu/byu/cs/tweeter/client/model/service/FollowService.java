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
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleListHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleBoolHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleIntHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleIntObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleListObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleBoolObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
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

    public void loadMoreItems(User user, int pageSize, User lastFollow, String type, SimpleListObserver<User> observer) {
        if (Objects.equals(type, "following")){
            GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                    user, pageSize, lastFollow, new SimpleListHandler<User>(observer, "following"));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(getFollowingTask);
        }
        else if (Objects.equals(type, "followers")){
            GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                    user, pageSize, lastFollow, new SimpleListHandler<User>(observer, "followers"));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(getFollowersTask);
        }
        else{
            throw new RuntimeException("Wrong input: following or followers in FollowService");
        }
    }
    public void loadMoreItems(User user, int pageSize, Status lastStatus, String type, SimpleListObserver<Status> observer) {
        if (Objects.equals(type, "story")){
            GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                    user, pageSize, lastStatus, new SimpleListHandler<Status>(observer, "story"));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(getStoryTask);
        }
        else if (Objects.equals(type, "feed")){
            GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                    user, pageSize, lastStatus, new SimpleListHandler<Status>(observer, "feed"));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(getFeedTask);
        }
        else{
            throw new RuntimeException("Wrong input: feed or story in FollowService");
        }
    }
    public void updateFollowingAndFollowers(User selectedUser, SimpleIntObserver observer) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleIntHandler(observer, "followers"));
        executor.execute(followersCountTask);
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleIntHandler(observer, "following"));
        executor.execute(followingCountTask);
    }
    public void onClickUnfollow(User selectedUser, SimpleNotificationObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);
    }
    public void onClickFollow(User selectedUser, SimpleNotificationObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }
    public void isFollower(User selectedUser, SimpleBoolObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new SimpleBoolHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

}
