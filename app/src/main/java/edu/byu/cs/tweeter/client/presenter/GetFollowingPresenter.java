package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleListObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserAuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingPresenter extends PagedPresenter<User>{
    //REFACTORED FollowService & UserService Functions
    public GetFollowingPresenter(ItemView<User> view) {
        super(view, "following");
    }
    @Override
    public void loadItems(User user) {
        followService.loadMoreItems(user, PAGE_SIZE, last, "following", new GetListObserver());
    }
}
