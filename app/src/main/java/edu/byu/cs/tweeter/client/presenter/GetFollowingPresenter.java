package edu.byu.cs.tweeter.client.presenter;

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
