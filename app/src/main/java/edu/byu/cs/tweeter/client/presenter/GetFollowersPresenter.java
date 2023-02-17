package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowersPresenter extends PagedPresenter<User> {
    //REFACTORED FollowService & UserService Functions
    public GetFollowersPresenter(ItemView<User> view) {
        super(view, "followers");
    }
    @Override
    public void loadItems(User user) {
        followService.loadMoreItems(user, PAGE_SIZE, last, "followers", new GetListObserver());
    }
}
