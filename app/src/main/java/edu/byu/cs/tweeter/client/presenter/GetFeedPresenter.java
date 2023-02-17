package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFeedPresenter extends PagedPresenter<Status>{
    public GetFeedPresenter(ItemView<Status> view){
        super(view, "feed");
    }
    @Override
    public void loadItems(User user) {
        followService.loadMoreItems(user, PAGE_SIZE, last, "feed", new GetListObserver());
    }
}
